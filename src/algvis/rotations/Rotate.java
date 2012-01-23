package algvis.rotations;

import java.awt.Color;

import algvis.bst.BST;
import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;

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
				v.getLeft().subtreeBgColor(Color.red);
			if (v.getRight() != null)
				v.getRight().subtreeBgColor(Color.green);
			if (u.getRight() != null)
				u.getRight().subtreeBgColor(Color.blue);
		} else {
			if (u.getLeft() != null)
				u.getLeft().subtreeBgColor(Color.red);
			if (v.getLeft() != null)
				v.getLeft().subtreeBgColor(Color.green);
			if (v.getRight() != null)
				v.getRight().subtreeBgColor(Color.blue);
		}
		mysuspend();

		T.rotate(v);
		R.v = u;
		T.reposition();
		mysuspend();

		R.v = null;
		if (v.getLeft() != null)
			v.getLeft().subtreeBgColor(Colors.NORMAL);
		if (v.getRight() != null)
			v.getRight().subtreeBgColor(Colors.NORMAL);
		if (u.getLeft() != null)
			u.getLeft().subtreeBgColor(Colors.NORMAL);
		if (u.getRight() != null)
			u.getRight().subtreeBgColor(Colors.NORMAL);
	}
}
