package algvis.unionfind;

import java.util.ArrayList;

import algvis.core.Alignment;
import algvis.core.ClickListener;
import algvis.core.DataStructure;
import algvis.core.View;
import algvis.core.VisPanel;
import algvis.unionfind.UnionFindFind.FindHeuristic;
import algvis.unionfind.UnionFindUnion.UnionHeuristic;

public class UnionFind extends DataStructure implements ClickListener {
	public static String adtName = "ufa";
	public static String dsName = "ufi";

	public int count = 0;
	public ArrayList<UnionFindNode> sets = new ArrayList<UnionFindNode>();
	public ArrayList<UnionFindNode> vertices = new ArrayList<UnionFindNode>();
	public UnionFindNode v = null;

	public FindHeuristic pathCompression = FindHeuristic.HALVING;
	public UnionHeuristic unionState = UnionHeuristic.NONE;

	public UnionFindNode firstSelected = null;
	public UnionFindNode secondSelected = null;

	public UnionFind(VisPanel M) {
		super(M, dsName);
		M.screen.V.align = Alignment.LEFT;
		M.screen.V.setDS(this);
		count = 0;
		sets = new ArrayList<UnionFindNode>();
		makeSet(10);
	}

	@Override
	public String stats() {
		return "No idea what this should contains.";
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
			// System.out.println(x1 + " " + x2 + " " + y1 + " " + y2);
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
