package algvis.redblacktree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;

public class RBFind extends Algorithm {
	RB T;
	BSTNode v;
	int K;

	public RBFind(RB T, int x) {
		super(T.M);
		this.T = T;
		v = T.v = new BSTNode(T, K = x);
		v.bgColor(Colors.FIND);
		setHeader("search");
	}

	@Override
	public void run() {
		if (T.root == T.NULL) {
			v.goToRoot();
			setText("empty");
			mysuspend();
			v.goDown();
			v.bgColor(Colors.NOTFOUND);
			setText("notfound");
		} else {
			BSTNode w = T.root;
			v.goTo(w);
			setText("bstfindstart");
			mysuspend();
			while (true) {
				if (w.key == K) {
					setText("found");
					v.bgColor(Colors.FOUND);
					break;
				} else if (w.key < K) {
					setText("bstfindright", K, w.key);
					w = w.right;
					if (w != T.NULL) {
						v.goTo(w);
					} else { // notfound
						setText("notfound");
						v.bgColor(Colors.NOTFOUND);
						v.goRight();
						break;
					}
				} else {
					setText("bstfindleft", K, w.key);
					w = w.left;
					if (w != T.NULL) {
						v.goTo(w);
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
	}
}
