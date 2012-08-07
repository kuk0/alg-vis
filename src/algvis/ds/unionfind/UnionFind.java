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
package algvis.ds.unionfind;

import algvis.core.history.HashtableStoreSupport;
import algvis.core.visual.ZDepth;
import algvis.ds.DataStructure;
import algvis.ds.unionfind.UnionFindFind.FindHeuristic;
import algvis.ds.unionfind.UnionFindUnion.UnionHeuristic;
import algvis.gui.VisPanel;
import algvis.gui.view.Alignment;
import algvis.gui.view.ClickListener;
import algvis.gui.view.View;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class UnionFind extends DataStructure implements ClickListener {
	public static String adtName = "ufa";
	public static String dsName = "ufi";

	public int count = 0;
	private ArrayList<UnionFindNode> sets = new ArrayList<UnionFindNode>();
	private ArrayList<UnionFindNode> vertices = new ArrayList<UnionFindNode>();
	private UnionFindNode v = null;

	public FindHeuristic pathCompression = FindHeuristic.NONE;
	public UnionHeuristic unionState = UnionHeuristic.NONE;

	public UnionFindNode firstSelected = null;
	public UnionFindNode secondSelected = null;

	@Override
	public String getName() {
		return "ufi";
	}

	public UnionFind(VisPanel M) {
		super(M);
		M.screen.V.align = Alignment.LEFT;
		M.screen.V.setDS(this);
		count = 0;
		makeSet(15);
	}

	void setV(UnionFindNode v) {
		this.v = v;
	}

	/** adds to sets and vertices */
    void add(UnionFindNode n) {
		count++;
		sets.add(n);
		vertices.add(n);
	}

	public void removeFromSets(UnionFindNode n) {
		sets.remove(n);
	}

	@Override
	public String stats() {
		return "";
	}

	@Override
	public void insert(int x) {
	}

	public void makeSet(int N) {
		for (int i = 0; i < N; i++) {
			add(new UnionFindNode(this, count, ZDepth.ACTIONNODE));
		}
		reposition();
	}

	public void find(UnionFindNode u) {
		start(new UnionFindFind(this, u));
	}

	public void union(UnionFindNode u, UnionFindNode v) {
		start(new UnionFindUnion(this, u, v));
	}

	@Override
	public void random(int n) {
		Random g = new Random(System.currentTimeMillis());
		boolean p = panel.pauses;
		panel.pauses = false;
			for (int i = 0; i < n; ++i) {
				union(at(g.nextInt(count)), at(g.nextInt(count)));
			}
		panel.pauses = p;
	}

	@Override
	public void clear() {
		count = 0;
		sets = new ArrayList<UnionFindNode>();
		vertices = new ArrayList<UnionFindNode>();
		makeSet(10);
		setStats();
	}

	@Override
	public void draw(View V) {
		if (sets != null) {
            for (UnionFindNode set : sets) {
                set.moveTree();
                set.drawTree(V);
            }
		}
		if (v != null) {
			if (isSelected(v) && (!v.marked)) {
				// v.mark(); // TODO
			}
			if (!isSelected(v) && (!!v.marked)) {
				// v.unmark(); // TODO are these lines needed?
			}
			v.move();
			v.draw(V);
		}
	}

	@Override
	public void move() {
	}

	@Override
	public Rectangle2D getBoundingBox() {
		return null;
	}

	@Override
	protected void endAnimation() {
	}

	@Override
	protected boolean isAnimationDone() {
		return false;
	}

	public void reposition() {
		if (sets != null) {
			int ey2 = -9999999;
			int ey1 = 9999999;
            for (UnionFindNode set : sets) {
                y1 = y2 = 0;
                set.reposition();
                if (y1 < ey1) {
                    ey1 = y1;
                }
                if (y2 > ey2) {
                    ey2 = y2;
                }
            }
			y1 = ey1;
			y2 = ey2;

			x1 = x2 = 0;
			int shift = -sets.get(0).leftw;
			x1 = shift;
            for (UnionFindNode set : sets) {
                shift += set.leftw;
                set.shift(shift, 0);
                shift += set.rightw;
            }
			x2 = shift;
			panel.screen.V.setBounds(x1, y1, x2, y2);
		}
	}

	public UnionFindNode at(int elementAt) {
		return vertices.get(elementAt);
	}

	boolean isSelected(UnionFindNode u) {
        return (u == firstSelected) || (u == secondSelected);
	}

	@Override
	public void mouseClicked(int x, int y) {
		UnionFindNode u = null;
		int i = 0;
		int j = sets.size();
		do {
			u = (UnionFindNode) sets.get(i).find(x, y);
			i++;
		} while ((u == null) && (i < j));
		if (u != null) {
			if (isSelected(u)) {
				u.unmark();
				if (u == secondSelected) {
					secondSelected = null;
				} else if (u == firstSelected) {
					firstSelected = secondSelected;
					secondSelected = null;
				}
			} else {
				u.mark();
				if (firstSelected == null) {
					firstSelected = u;
				} else if (secondSelected == null) {
					secondSelected = u;
				} else {
					firstSelected.unmark();
					firstSelected = secondSelected;
					secondSelected = u;
				}
			}
		}
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		HashtableStoreSupport.store(state, "count", count);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		Integer count = (Integer) state.get(hash + "count");
		if (count != null) this.count = count;
	}
}
