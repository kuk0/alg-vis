package algvis.bst;

import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class BSTFind extends Algorithm {
	BST T;
	BSTNode v;
	int K;

	public BSTFind(BST T, int x) {
		super(T);
		this.T = T;
		v = T.setNodeV(new BSTNode(T, K = x));
		v.setColor(NodeColor.FIND);
		setHeader("search");
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			addStep("empty");
			mysuspend();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
		} else {
			BSTNode w = T.root;
			v.goAbove(w);
			addStep("bstfindstart");
			mysuspend();
			while (true) {
				if (w.key == K) {
					v.goTo(w);
					addStep("found");
					v.setColor(NodeColor.FOUND);
					break;
				} else if (w.key < K) {
					if (w.getRight() == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(w.getRight());
					}
					addStep("bstfindright", K, w.key);
					mysuspend();
					v.noArrow();
					w = w.getRight();
					if (w != null) {
						v.goAbove(w);
					} else { // not found
						addStep("notfound");
						v.setColor(NodeColor.NOTFOUND);
						v.goRight();
						break;
					}
				} else {
					if (w.getLeft() == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(w.getLeft());
					}
					addStep("bstfindleft", K, w.key);
					mysuspend();
					v.noArrow();
					w = w.getLeft();
					if (w != null) {
						v.goAbove(w);
					} else { // notfound
						addStep("notfound");
						v.setColor(NodeColor.NOTFOUND);
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}
		}
		finish();
	}
}
