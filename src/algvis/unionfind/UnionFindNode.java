package algvis.unionfind;

import algvis.core.DataStructure;
import algvis.core.TreeNode;
import algvis.core.View;

public class UnionFindNode extends TreeNode {
	public int rank = 0;
	public boolean greyPair = false;

	public UnionFindNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public UnionFindNode(DataStructure D, int key) {
		super(D, key);
	}

	@Override
	public UnionFindNode getChild() {
		return (UnionFindNode) super.getChild();
	}

	@Override
	public UnionFindNode getRight() {
		return (UnionFindNode) super.getRight();
	}

	@Override
	public UnionFindNode getParent() {
		return (UnionFindNode) super.getParent();
	}

	public void unsetGrey() {
		TreeNode w = getChild();
		while (w != null) {
			((UnionFindNode) w).unsetGrey();
			w = w.getRight();
		}
		greyPair = false;
	}

	public void drawGrey(View v) {
		TreeNode w = getChild();
		while (w != null) {
			((UnionFindNode) w).drawGrey(v);
			w = w.getRight();
		}
		if (greyPair && getParent() != null) {
			v.drawWideLine(x, y, getParent().x, getParent().y, 10.0f);
		}
	}

	@Override
	public void drawTree(View v) {
		drawGrey(v);
		super.drawTree(v);
	}
}
