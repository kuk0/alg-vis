package algvis.redblacktree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;

public class RBFind extends Algorithm {
	RB T;
	BSTNode v;
	int K;

	public RBFind(RB T, int x) {
		super(T);
		this.T = T;
		v = T.v = new BSTNode(T, K = x);
		v.bgColor(Colors.FIND);
		setHeader("search");
	}

	@Override
	public void run() {
		if (T.root == T.NULL) {
			v.goToRoot();
			addStep("empty");
			mysuspend();
			v.goDown();
			v.bgColor(Colors.NOTFOUND);
			addStep("notfound");
		} else {
			BSTNode w = T.root;
			v.goTo(w);
			addStep("bstfindstart");
			mysuspend();
			while (true) {
				if (w.key == K) {
					addStep("found");
					v.bgColor(Colors.FOUND);
					break;
				} else if (w.key < K) {
					addStep("bstfindright", K, w.key);
					w = w.getRight();
					if (w != T.NULL) {
						v.goTo(w);
					} else { // notfound
						addStep("notfound");
						v.bgColor(Colors.NOTFOUND);
						v.goRight();
						break;
					}
				} else {
					addStep("bstfindleft", K, w.key);
					w = w.getLeft();
					if (w != T.NULL) {
						v.goTo(w);
					} else { // notfound
						addStep("notfound");
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
