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
package algvis.unionfind;

import java.util.ArrayList;
import java.util.Random;

import algvis.core.Alignment;
import algvis.core.ClickListener;
import algvis.core.DataStructure;
import algvis.core.View;
import algvis.core.VisPanel;
import algvis.scenario.Scenario;
import algvis.unionfind.UnionFindFind.FindHeuristic;
import algvis.unionfind.UnionFindUnion.UnionHeuristic;

public class UnionFind extends DataStructure implements ClickListener {
	public static String adtName = "ufa";
	public static String dsName = "ufi";

	public int count = 0;
	public ArrayList<UnionFindNode> sets = new ArrayList<UnionFindNode>();
	public ArrayList<UnionFindNode> vertices = new ArrayList<UnionFindNode>();
	public UnionFindNode v = null;

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
		sets = new ArrayList<UnionFindNode>();
		makeSet(15);
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
			count++;
			UnionFindNode T = new UnionFindNode(this, count);
			sets.add(T);
			vertices.add(T);
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
		boolean p = M.pause;
		M.pause = false;
		{
			int i = 0;
			scenario.enableAdding(false);
			M.C.enableUpdating(false);
			for (; i < n - Scenario.maxAlgorithms; ++i) {
				union(at(g.nextInt(count)), at(g.nextInt(count)));
			}
			scenario.enableAdding(true);
			for (; i < n; ++i) {
				union(at(g.nextInt(count)), at(g.nextInt(count)));
			}
			M.C.enableUpdating(true);
			M.C.update();
		}
		M.pause = p;
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
			for (int i = 0; i < sets.size(); i++) {
				sets.get(i).moveTree();
				sets.get(i).drawTree(V);
			}
		}
		if (v != null) {
			if (isSelected(v) && (v.marked == false)) {
				v.mark();
			}
			if (!isSelected(v) && (v.marked != false)) {
				v.unmark();
			}
			v.move();
			v.draw(V);
		}
	}

	public void reposition() {
		if (sets != null) {
			int ey2 = -9999999;
			int ey1 = 9999999;
			for (int i = 0; i < sets.size(); i++) {
				y1 = y2 = 0;
				sets.get(i).reposition();
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
			for (int i = 0; i < sets.size(); i++) {
				shift += sets.get(i).leftw;
				sets.get(i).shift(shift, 0);
				shift += sets.get(i).rightw;
			}
			x2 = shift;
			M.screen.V.setBounds(x1, y1, x2, y2);
		}
	}

	public UnionFindNode at(int elementAt) {
		return vertices.get(elementAt);
	}

	public boolean isSelected(UnionFindNode u) {
		if ((u == firstSelected) || (u == secondSelected))
			return true;
		else
			return false;
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
}
