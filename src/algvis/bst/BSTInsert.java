package algvis.bst;

import algvis.core.Algorithm;
import algvis.core.Colors;

public class BSTInsert extends Algorithm {
	BST T;
	BSTNode v;
	int K;

	public BSTInsert(BST T, int x) {
		super(T);
		this.T = T;
		v = T.setNodeV(new BSTNode(T, K = x));
		v.bgColor(Colors.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		v.getReady();
		if (T.root == null) {
			T.setRoot(v);
			v.goToRoot();
			setText("newroot");
		} else {
			BSTNode w = T.root;
			v.goAboveRoot();
			setText("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					setText("alreadythere");
					v.bgColor(Colors.NOTFOUND);
					v.goDown();
					finish();
					return;
				} else if (w.key < K) {
					if (w.right == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(w.right);
					}
					setText("bstinsertright", K, w.key);
					mysuspend();
					v.noArrow();
					if (w.right != null) {
						w = w.right;
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					if (w.left == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(w.left);
					}
					setText("bstinsertleft", K, w.key);
					mysuspend();
					v.noArrow();
					if (w.left != null) {
						w = w.left;
					} else {
						w.linkLeft(v);
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}
		}
		T.reposition();
		mysuspend();
		setText("done");
		v.bgColor(Colors.NORMAL);
		T.setNodeV(null);
		finish();
	}
}
