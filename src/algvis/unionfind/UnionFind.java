package algvis.unionfind;

import java.util.ArrayList;

import algvis.core.DataStructure;
import algvis.core.TreeNode;
import algvis.core.View;
import algvis.core.VisPanel;

public class UnionFind extends DataStructure {
	public static String adtName = "ufa";
	public static String dsName = "ufi";

	public int count = 0;
	public ArrayList<TreeNode> sets = new ArrayList<TreeNode>();
	public ArrayList<TreeNode> vertices = new ArrayList<TreeNode>();
	public TreeNode v = null;

	public UnionFind(VisPanel M) {
		super(M);
		count = 0;
		sets = new ArrayList<TreeNode>();
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
		TreeNode T = new TreeNode(this, count);
		sets.add(T);
		vertices.add(T);
		for (TreeNode V : sets) {
			V.reposition();
		}
	}

	public void union(int element1, int element2) {

	}

	@Override
	public void clear() {
		count = 0;
		sets = new ArrayList<TreeNode>();
		for (int i = 0; i < 10; i++) {
			makeSet();
		}
		setStats();
	}

	@Override
	public void draw(View V) {
		if (sets != null) {
			for (TreeNode T : sets) {
				T.moveTree();
				T.drawTree(V);
			}
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
	}
}
