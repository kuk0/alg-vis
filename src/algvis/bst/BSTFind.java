package algvis.bst;

import algvis.core.Algorithm;
import algvis.core.Colors;
import algvis.core.Node;

public class BSTFind extends Algorithm {
	BST T;
	BSTNode v;
	int K;

	public BSTFind(BST T, int x) {
		super(T, "BSTFind");
		this.T = T;
		v = T.setNodeV(new BSTNode(T, K = x, T.up()));
		v.setState(Node.ALIVE);
		v.bgColor(Colors.FIND);
		setHeader("search");
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			setText("empty");
			mysuspend();
			v.goDown();
			v.bgColor(Colors.NOTFOUND);
			setText("notfound");
		} else {
			BSTNode w = T.root;
			v.goAbove(w);
			setText("bstfindstart");
			mysuspend();
			while (true) {
				if (w.key == K) {
					v.goTo(w);
					setText("found");
					v.bgColor(Colors.FOUND);
					break;
				} else if (w.key < K) {
					if (w.right == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(w.right);
					}
					setText("bstfindright", K, w.key);
					mysuspend();
					v.noArrow();
					w = w.right;
					if (w != null) {
						v.goAbove(w);
					} else { // not found
						setText("notfound");
						v.bgColor(Colors.NOTFOUND);
						v.goRight();
						break;
					}
				} else {
					if (w.left == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(w.left);
					}
					setText("bstfindleft", K, w.key);
					mysuspend();
					v.noArrow();
					w = w.left;
					if (w != null) {
						v.goAbove(w);
					} else { // notfound
						setText("notfound");
						v.bgColor(Colors.NOTFOUND);
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
