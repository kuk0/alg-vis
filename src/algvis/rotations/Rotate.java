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
		BSTNode u = v.parent;
		boolean rotR = v.isLeft();
		if (rotR) {
			if (v.left != null) v.left.subtreeBgColor(Color.red);
			if (v.right != null) v.right.subtreeBgColor(Color.green);
			if (u.right != null) u.right.subtreeBgColor(Color.blue);
		} else {
			if (u.left != null) u.left.subtreeBgColor(Color.red);
			if (v.left != null) v.left.subtreeBgColor(Color.green);
			if (v.right != null) v.right.subtreeBgColor(Color.blue);
		}
		mysuspend();
		
		T.rotate(v);
		R.v = u;
		T.reposition();
		mysuspend();

		R.v = null;
		if (v.left != null) v.left.subtreeBgColor(Colors.NORMAL);
		if (v.right != null) v.right.subtreeBgColor(Colors.NORMAL);
		if (u.left != null) u.left.subtreeBgColor(Colors.NORMAL);
		if (u.right != null) u.right.subtreeBgColor(Colors.NORMAL);
	}
}
