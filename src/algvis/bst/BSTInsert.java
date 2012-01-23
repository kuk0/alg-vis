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
		if (T.root == null) {
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
		} else {
			BSTNode w = T.root;
			v.goAboveRoot();
			addStep("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					addStep("alreadythere");
					v.bgColor(Colors.NOTFOUND);
					v.goDown();
					finish();
					return;
				} else if (w.key < K) {
					if (w.getRight() == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(w.getRight());
					}
					addStep("bstinsertright", K, w.key);
					mysuspend();
					v.noArrow();
					if (w.getRight() != null) {
						w = w.getRight();
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					if (w.getLeft() == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(w.getLeft());
					}
					addStep("bstinsertleft", K, w.key);
					mysuspend();
					v.noArrow();
					if (w.getLeft() != null) {
						w = w.getLeft();
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
		addNote("done");
		v.bgColor(Colors.NORMAL);
		T.setNodeV(null);
		finish();
	}
}
