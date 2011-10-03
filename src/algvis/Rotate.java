package algvis;

public class Rotate extends Algorithm {
	BST T;
	BSTNode v;

	public Rotate(BST T, int x) {
		super(T.M);
		this.T = T;
		v = T.root;
		while (v != null && v.key != x) {
			if (v.key < x) {
				v = v.right;
			} else {
				v = v.left;
			}
		}
	}

	@Override
	public void run() {
		if (v == null) {
			// vypis ze taky vrchol neexistuje
			return;
		} else if (v == T.root) {
			// vypis ze to je root...
			return;
		}
		BSTNode u = v.parent;
		boolean rotR = v.isLeft();
		u.mark();
		v.mark();
		v.setArc(u);
		/*
		 * if (rotR) { u.pointTo(v.right); } else { u.pointTo(v.left); } if
		 * (!u.isRoot()) { u.parent.pointTo(v); }
		 */
		mysuspend();
		v.noArc();
		if (u.isRoot()) {
			T.root = v;
			v.parent = null;
		} else {
			if (u.isLeft()) {
				((RotNode) u.parent).linkLeft(v);
			} else {
				((RotNode) u.parent).linkRight(v);
			}
		}
		if (rotR) { // rotate right
			if (v.right == null) {
				u.left = null;
			} else {
				((RotNode) u).linkLeft(v.right);
			}
			((RotNode) v).linkRight(u);
		} else { // rotate left
			if (v.left == null) {
				u.right = null;
			} else {
				((RotNode) u).linkRight(v.left);
			}
			((RotNode) v).linkLeft(u);
		}
		T.reposition();
		mysuspend();
		u.unmark();
		v.unmark();
		((RotNode) u).removeOldEdges();
		((RotNode) v).removeOldEdges();
		u.noArrow();
		if (!v.isRoot()) {
			v.parent.noArrow();
			((RotNode) v.parent).removeOldEdges();
		}
		T.reposition();
		mysuspend();
		if (v.left != null) {
			v.left.calc();
		}
		if (v.right != null) {
			v.right.calc();
		}
		v.calc();
	}
}
