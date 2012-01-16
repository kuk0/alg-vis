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
	public void calc() {
		super.calc();
		int lp = 0, rp = 0;
		if (left != null) {
			lp = ((SplayNode) left).pot;
		}
		if (right != null) {
			rp = ((SplayNode) right).pot;
		}
		pot = (int) Math.floor(D.lg(size)) + lp + rp;
	}

	@Override
	public void calcTree() {
		if (left != null) {
			left.calcTree();
		}
		if (right != null) {
			right.calcTree();
		}
		calc();
	}
}
