package algvis.unionfind;

import java.util.ArrayList;

import algvis.core.Alignment;
import algvis.core.DataStructure;
import algvis.core.View;
import algvis.core.VisPanel;
import algvis.unionfind.UnionFindFind.findType;
import algvis.unionfind.UnionFindUnion.unionType;

public class UnionFind extends DataStructure {
	public static String adtName = "ufa";
	public static String dsName = "ufi";

	public int count = 0;
	public ArrayList<UnionFindNode> sets = new ArrayList<UnionFindNode>();
	public ArrayList<UnionFindNode> vertices = new ArrayList<UnionFindNode>();
	public UnionFindNode v = null;

	public findType findState = findType.COMPRESSION;
	public unionType unionState = unionType.SIMPLE;

	public UnionFind(VisPanel M) {
		super(M);
		M.screen.V.align = Alignment.LEFT;
		count = 0;
		sets = new ArrayList<UnionFindNode>();
		for (int i = 0; i < 10; i++) {
			makeSet();
		}
	}

	@Override
	public String stats() {
		return "No idea what this should contains.";
	}

	@Override
	public void insert(int x) {
		// This represent how many times MakeSet() will be called
		for (int i = 0; i < x; i++) {
			// MakeSet();
		}
	}

	public void makeSet() {
		count++;
		UnionFindNode T = new UnionFindNode(this, count);
		sets.add(T);
		vertices.add(T);
		reposition();
	}

	public void union(int element1, int element2) {
		start(new UnionFindUnion(this, element1, element2));
	}

	@Override
	public void clear() {
		count = 0;
		sets = new ArrayList<UnionFindNode>();
		vertices = new ArrayList<UnionFindNode>();
		for (int i = 0; i < 10; i++) {
			makeSet();
		}
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
				sets.get(i).shift(shift,0);
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
}
