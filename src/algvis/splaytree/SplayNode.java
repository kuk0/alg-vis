package algvis.splaytree;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;

public class SplayNode extends BSTNode {
	int pot = 0;

	public SplayNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public SplayNode(DataStructure D, int key) {
		this(D, key, 0, 0);
		getReady();
	}

	@Override
	public SplayNode getLeft() {
		return (SplayNode) super.getLeft();
	}

	@Override
	public SplayNode getRight() {
		return (SplayNode) super.getRight();
	}

	@Override
	public SplayNode getParent() {
		return (SplayNode) super.getParent();
	}

	@Override
	public void calc() {
		super.calc();
		int lp = 0, rp = 0;
		if (getLeft() != null) {
			lp = getLeft().pot;
		}
		if (getRight() != null) {
			rp = getRight().pot;
		}
		pot = (int) Math.floor(D.lg(size)) + lp + rp;
	}

	@Override
	public void calcTree() {
		if (getLeft() != null) {
			getLeft().calcTree();
		}
		if (getRight() != null) {
			getRight().calcTree();
		}
		calc();
	}
}
