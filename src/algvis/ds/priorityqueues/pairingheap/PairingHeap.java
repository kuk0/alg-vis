/*******************************************************************************
 * Copyright (c) 2012 Jakub Kov��, Katar�na Kotrlov�, Pavol Luk�a, Viktor Tomkovi�, Tatiana T�thov�
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.ds.priorityqueues.pairingheap;

import algvis.core.Node;
import algvis.core.Pair;
import algvis.core.history.HashtableStoreSupport;
import algvis.core.DataStructure;
import algvis.ds.priorityqueues.MeldablePQ;
import algvis.ds.priorityqueues.MeldablePQButtons;
import algvis.ds.priorityqueues.pairingheap.PairHeapAlg.Pairing;
import algvis.ui.VisPanel;
import algvis.ui.view.ClickListener;
import algvis.ui.view.View;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

public class PairingHeap extends MeldablePQ implements ClickListener{
	public static final String dsName = "pairingheap";
	PairHeapNode root[] = null;
	public Pairing pairState = Pairing.NAIVE;
	//public PairHeapNode children[] = null;
	
	public PairingHeap(VisPanel M) {
		super(M);
		root = new PairHeapNode[numHeaps + 1];
		M.screen.V.setDS(this); 
	}
	
	@Override
	public void mouseClicked(int x, int y) {
		int h = 0;
		PairHeapNode v = null;
		for (int i = 1; i <= numHeaps; ++i) {
			if (root[i] != null) {
				v = (PairHeapNode) root[i].find(x, y);
				if (v != null) {
					h = i;
					break;
				}
			}
		}
		if (v != null) {
			if (v.marked) {
				v.unmark();
				chosen = null;
			} else {
				if (chosen != null)
					chosen.unmark();
				if (h == active) {
					v.mark();
					chosen = v;
				} else {
					((MeldablePQButtons) panel.buttons).activeHeap.setValue(h);
					// lowlight();
					// highlight(h);
				}
			}
		}
		
	}

	@Override
	public void insert(int x) {
		start(new PairHeapInsert(this, x));
		
	}

	@Override
	public void delete() {
		start(new PairHeapDelete(this));
	}

	
	@Override
	public void meld(int i, int j) {
		Pair p = chooseHeaps(i, j);
		i = p.first;
		j = p.second;
		((MeldablePQButtons) panel.buttons).activeHeap.setValue(i);
		start(new PairHeapMeld(this, i, j));
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		if (v == null) {
			// TODO: vypindat
		} else {
			start(new PairHeapDecrKey(this, (PairHeapNode) v, delta));
		}
		
	}

	@Override
	public String getName() {
		return "pairingheap";
	}

	@Override
	public String stats() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void clear() {
		for(int i = 0; i <= numHeaps; i++){
			root[i] = null;
		}
		
	}

	@Override
	public void draw(View V) {
		for (int i = 0; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i].drawTree(V);
			}
		}		 
	}

	@Override
	protected void move() {
		for (int i = 0; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i].moveTree();
			}
		}
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		Rectangle2D retVal = null;
		if (root != null) {
			for (int i = 0; i <= numHeaps; ++i) {
				if (root[i] != null) {
					Rectangle2D riBB = root[i].getBoundingBox();
					if (retVal == null) {
						retVal = riBB;
					} else if (riBB != null) {
						retVal = retVal.createUnion(riBB);
					}
				}
			}
		}
		return retVal;
	}

	@Override
	protected void endAnimation() {
		if (root != null) {
			for (int i = 0; i <= numHeaps; ++i) {
				if (root[i] != null) root[i].endAnimation();
			}
		}
	}

	@Override
	protected boolean isAnimationDone() {
		if (root != null) {
			for (int i = 0; i <= numHeaps; ++i) {
				if (root[i] != null && !root[i].isAnimationDone()) return false;
			}
		}
		return true;
	}

	@Override
	public void highlight(int i) {
		active = i;
		if (root[active] != null) {
			root[active].highlightTree();
		}
	}

	@Override
	public void lowlight() {
		if (root[active] != null) {
			root[active].lowlightTree();
		}
	}
	
	Pair chooseHeaps(int i, int j) {
		if (i < 1 || i > numHeaps) {
			i = -1;
		}
		if (j < 1 || j > numHeaps) {
			j = -1;
		}
		if (i == -1 || j == -1) {
			j = i;
			i = active;
			if (i == j || j == -1) {
				j = (active == 1) ? 2 : 1;
				for (int k = 0; k <= numHeaps; ++k) {
					if (k != active && root[k] != null) {
						j = k;
						break;
					}
				}
			}
		}
		return new Pair(i, j);
	}
	
	public void reposition() {
		int sumx = 0;
		for (int i = 1; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i].reposition();
				root[i].reboxTree();
				//sumx += root[i].leftw; 
				//root[i].reposition();			//(sumx, root[i].toy);
				if (root[i].state == -1){
					root[i].shift(sumx, -1*(DataStructure.minsepy));
				}else{
					root[i].shift(sumx, 0);
				}
				sumx += root[i].rightw + 20;	// 20 + minsepx = vzdialenost medzi haldami
			}
			if (i+1 <= numHeaps){
				if (root[i+1] != null) {
					sumx += root[i+1].leftw;
				}
			}
			
			if (i == active) {
				if (root[0] != null) {
					root[0].reposition();
					root[0].reboxTree();
					sumx += root[0].leftw + 20;	//20
					/*
					if (root[0].y >= 0) { 		// nie je na zaciatku vkladania
						//root[0].reposition();	//(sumx, root[0].y);
						root[0].shift(sumx, 0);
					} else {
						//root[0].reposition();	//(sumx, root[0].toy);
						root[0].shift(sumx, 0);
					}
					*/
					if (root[0].state == -1){
						root[0].shift(sumx, -1*(DataStructure.minsepy));
					}else{
						root[0].shift(sumx, 0);
					}
					sumx += root[0].rightw;
				}
			}
			
		}
		panel.screen.V.setBounds(0, 0, sumx, y2);
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "root", root.clone());
		for (int i = 0; i <= numHeaps; ++i) {
			if (root[i] != null) root[i].storeState(state);
		}
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object root = state.get(hash + "root");
		if (root != null) this.root = (PairHeapNode[]) HashtableStoreSupport.restore(root);
		for (int i = 0; i <= numHeaps; ++i) {
			if (this.root[i] != null) this.root[i].restoreState(state);
		}
	}
}
