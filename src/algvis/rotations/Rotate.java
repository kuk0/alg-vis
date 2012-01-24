package algvis.rotations;

import algvis.bst.BST;
import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class Rotate extends Algorithm {
	Rotations R;
	BST T;
	BSTNode v;

	public Rotate(Rotations R, BSTNode v) {
		super(R.T);
		this.R = R;
		this.T = R.T;
		this.v = v;
	}

	@Override
	public void run() {
		if (v == T.root) {
			// vypis ze to je root...
			return;
		}
		BSTNode u = v.getParent();
		boolean rotR = v.isLeft();
		if (rotR) {
			if (v.getLeft() != null)
				v.getLeft().subtreeColor(NodeColor.RED);
			if (v.getRight() != null)
				v.getRight().subtreeColor(NodeColor.GREEN);
			if (u.getRight() != null)
				u.getRight().subtreeColor(NodeColor.BLUE);
		} else {
			if (u.getLeft() != null)
				u.getLeft().subtreeColor(NodeColor.RED);
			if (v.getLeft() != null)
				v.getLeft().subtreeColor(NodeColor.GREEN);
			if (v.getRight() != null)
				v.getRight().subtreeColor(NodeColor.BLUE);
		}
		mysuspend();

		T.rotate(v);
		R.v = u;
		T.reposition();
		mysuspend();

		R.v = null;
		if (v.getLeft() != null)
			v.getLeft().subtreeColor(NodeColor.NORMAL);
		if (v.getRight() != null)
			v.getRight().subtreeColor(NodeColor.NORMAL);
		if (u.getLeft() != null)
			u.getLeft().subtreeColor(NodeColor.NORMAL);
		if (u.getRight() != null)
			u.getRight().subtreeColor(NodeColor.NORMAL);
	}
}
