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
package algvis.binomialheap;

import java.awt.Color;

import algvis.core.MeldablePQ;
import algvis.core.Node;
import algvis.core.Pair;
import algvis.gui.Fonts;
import algvis.gui.MeldablePQButtons;
import algvis.gui.VisPanel;
import algvis.gui.view.Alignment;
import algvis.gui.view.ClickListener;
import algvis.gui.view.View;
import algvis.internationalization.Languages;

public class BinomialHeap extends MeldablePQ implements ClickListener {
	public static final String dsName = "binheap";
	public final BinHeapNode[] root; // root[0] je pomocny, prave meldujuci
	public final BinHeapNode[] min;
	public BinHeapNode d, v, v2;

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
		start(new BinHeapInsert(this, active, x));
	}

	@Override
	public void delete() {
		start(new BinHeapDelete(this, active));
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
		Pair p = chooseHeaps(i, j);
		i = p.first;
		j = p.second;
		((MeldablePQButtons) M.B).activeHeap.setValue(i);
		start(new BinHeapMeld(this, i, j));
	}

	@Override
	public void clear() {
		for (int i = 0; i <= numHeaps; ++i) {
			root[i] = null;
		}
		v = v2 = null;
		setStats();
		reposition();
		M.screen.V.resetView();
	}

	// number of nodes in the i-th heap
	public int size(int i) {
		int s = 0;
		BinHeapNode v, w;
		v = w = root[i];
		if (w == null)
			return 0;
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
		if (d != null) {
			d.move();
			d.draw(V);
		}
		for (int i = 0; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i].moveTree();
				root[i].drawTree(V, root[i], null);
				if (i > 0) {
					V.setColor(Color.black);
					V.drawStringLeft(Languages.getString("heap") + " #" + i + ":",
							root[i].x - Node.radius - 5, root[i].y, Fonts.NORMAL);
				}
				if (min[i] != null) {
					if (minHeap) {
						V.drawStringTop(Languages.getString("min"), min[i].x,
								min[i].y - Node.radius - 2, Fonts.NORMAL);
					} else {
						V.drawStringTop(Languages.getString("max"), min[i].x,
								min[i].y - Node.radius - 2, Fonts.NORMAL);
					}
				}
			}
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
		if (v2 != null) {
			v2.move();
			v2.draw(V);
		}
	}

	public void reposition() {
		int x = 0, maxx = 0, maxy = 0, maxheight = 0, x0 = 0;
		for (int i = 1; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i]._reposition(x, 0);
				if (active == i && root[0] != null) {
					x0 = x;
				}
				maxx = root[i].left.tox + Node.radius;
				x = maxx + 3 * minsepx;
				if (maxheight < root[i].left.height) {
					maxheight = root[i].left.height;
				}
			}
		}
		//  maxy = maxheight * (2 * radius + yspan) - yspan;
		//  // height*(2*radius+yspan)-radius-yspan je sur. najnizsieho
		// maxy += 4 * (radius + yspan);
		maxy = (maxheight + 2) * minsepy;
		if (root[0] != null) {
			root[0]._reposition(x0, maxy);
			maxy += root[0].left.height * minsepy;
		}
		M.screen.V.setBounds(0, 0, maxx, maxy);
	}

	public void lowlight() {
		if (root[active] != null) {
			root[active].lowlightTree();
		}
	}

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
				if (chosen != null)
					chosen.unmark();
				if (h == active) {
					v.mark();
					chosen = v;
				} else {
					((MeldablePQButtons) M.B).activeHeap.setValue(h);
					// lowlight();
					// highlight(h);
				}
			}
		}
	}
}
