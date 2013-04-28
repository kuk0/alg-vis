/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ds.priorityqueues.binomialheap;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import algvis.core.Node;
import algvis.core.Pair;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.priorityqueues.MeldablePQ;
import algvis.ds.priorityqueues.MeldablePQButtons;
import algvis.internationalization.Languages;
import algvis.ui.Fonts;
import algvis.ui.VisPanel;
import algvis.ui.view.Alignment;
import algvis.ui.view.ClickListener;
import algvis.ui.view.View;

public class BinomialHeap extends MeldablePQ implements ClickListener {
	public static final String dsName = "binheap";
	public BinHeapNode[] root; // root[0] je pomocny, prave meldujuci
	public BinHeapNode[] min;

	// void debug() {
	// for (int i = 0, rootLength = root.length; i < rootLength; i++) {
	// BinHeapNode node = root[i];
	// System.out.println("Debugging root " + i + ":");
	// if (node == null) {
	// System.out.println("null");
	// } else {
	// node.debugTree();
	// }
	// }
	// for (int i = 0, minLength = min.length; i < minLength; i++) {
	// BinHeapNode node = min[i];
	// System.out.println("Debugging min " + i + ":");
	// if (node == null) {
	// System.out.println("null");
	// } else {
	// node.debugTree();
	// }
	// }
	// }

	@Override
	public String getName() {
		return "binheap";
	}

	public BinomialHeap(VisPanel M) {
		super(M);
		root = new BinHeapNode[numHeaps + 1];
		min = new BinHeapNode[numHeaps + 1];

		M.screen.V.setDS(this);
		M.screen.V.align = Alignment.LEFT;
	}

	@Override
	public void insert(int x) {
		start(new BinHeapInsert(this, x));
	}

	@Override
	public void delete() {
		start(new BinHeapDelete(this));
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		start(new BinHeapDecrKey(this, (BinHeapNode) v, delta));
	}

	protected Pair chooseHeaps(int i, int j) {
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

	@Override
	public void meld(int i, int j) {
		final Pair p = chooseHeaps(i, j);
		i = p.first;
		j = p.second;
		((MeldablePQButtons) panel.buttons).activeHeap.setValue(i);
		start(new BinHeapMeld(this, i, j));
	}

	@Override
	public void clear() {
		for (int i = 0; i <= numHeaps; ++i) {
			root[i] = null;
		}
		setStats();
		reposition();
		panel.screen.V.resetView();
	}

	// number of nodes in the i-th heap
	public int size(int i) {
		int s = 0;
		BinHeapNode v, w;
		v = w = root[i];
		if (w == null) {
			return 0;
		}
		do {
			s += (1 << w.rank);
			w = w.right;
		} while (w != v);
		return s;
	}

	@Override
	public String stats() {
		return "";
	}

	@Override
	public void draw(View V) {
		for (int i = 0; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i].drawTree(V, root[i], null);
				if (i > 0) {
					V.setColor(Color.black);
					V.drawStringLeft(Languages.getString("heap") + " #" + i
							+ ":", root[i].x - Node.RADIUS - 5, root[i].y,
							Fonts.NORMAL);
				}
				if (min[i] != null) {
					if (minHeap) {
						V.drawStringTop(Languages.getString("min"), min[i].x,
								min[i].y - Node.RADIUS - 2, Fonts.NORMAL);
					} else {
						V.drawStringTop(Languages.getString("max"), min[i].x,
								min[i].y - Node.RADIUS - 2, Fonts.NORMAL);
					}
				}
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
					final Rectangle2D riBB = root[i].getBoundingBox();
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
		for (int i = 0; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i].endAnimation();
			}
		}
	}

	@Override
	protected boolean isAnimationDone() {
		for (int i = 0; i <= numHeaps; ++i) {
			if (root[i] != null && !root[i].isAnimationDone()) {
				return false;
			}
		}
		return true;
	}

	public void reposition() {
		int x = 0, maxx = 0, maxy = 0, maxheight = 0, x0 = 0;
		for (int i = 1; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i]._reposition(x, 0);
				if (active == i && root[0] != null) {
					x0 = x;
				}
				maxx = root[i].left.tox + Node.RADIUS;
				x = maxx + 3 * minsepx;
				if (maxheight < root[i].left.height) {
					maxheight = root[i].left.height;
				}
			}
		}
		// maxy = maxheight * (2 * RADIUS + yspan) - yspan;
		// // height*(2*RADIUS+yspan)-RADIUS-yspan je sur. najnizsieho
		// maxy += 4 * (RADIUS + yspan);
		maxy = (maxheight + 2) * minsepy;
		if (root[0] != null) {
			root[0]._reposition(x0, maxy);
			maxy += root[0].left.height * minsepy;
		}
		panel.screen.V.setBounds(0, 0, maxx, maxy);
	}

	@Override
	public void lowlight() {
		if (root[active] != null) {
			root[active].lowlightTree();
		}
	}

	@Override
	public void highlight(int i) {
		active = i;
		if (root[active] != null) {
			root[active].highlightTree();
		}
	}

	@Override
	public void mouseClicked(int x, int y) {
		int h = 0;
		BinHeapNode v = null;
		for (int i = 1; i <= numHeaps; ++i) {
			if (root[i] != null) {
				v = root[i].find(root[i], x, y);
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
				if (chosen != null) {
					chosen.unmark();
				}
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
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "root", root.clone());
		for (int i = 0; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i].storeTreeState(state);
			}
		}
		HashtableStoreSupport.store(state, hash + "min", min.clone());
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		final Object root = state.get(hash + "root");
		if (root != null) {
			this.root = (BinHeapNode[]) HashtableStoreSupport.restore(root);
		}
		for (int i = 0; i <= numHeaps; ++i) {
			if (this.root[i] != null) {
				this.root[i].restoreTreeState(state);
			}
		}

		final Object min = state.get(hash + "min");
		if (min != null) {
			this.min = (BinHeapNode[]) HashtableStoreSupport.restore(min);
		}
	}
}
