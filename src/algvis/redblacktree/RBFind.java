package algvis.redblacktree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class RBFind extends Algorithm {
	RB T;
	BSTNode v;
	int K;

	public RBFind(RB T, int x) {
		super(T);
		this.T = T;
		v = T.setNodeV(new BSTNode(T, K = x));
		v.setColor(NodeColor.FIND);
		setHeader("search");
	}

	@Override
	public void run() {
		if (T.root == T.NULL) {
			v.goToRoot();
			addStep("empty");
			mysuspend();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
		} else {
			BSTNode w = T.root;
			v.goTo(w);
			addStep("bstfindstart");
			mysuspend();
			while (true) {
				if (w.key == K) {
					addStep("found");
					v.setColor(NodeColor.FOUND);
					break;
				} else if (w.key < K) {
					addStep("bstfindright", K, w.key);
					w = w.getRight();
					if (w != T.NULL) {
						v.goTo(w);
					} else { // notfound
						addStep("notfound");
						v.setColor(NodeColor.NOTFOUND);
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
						v.setColor(NodeColor.NOTFOUND);
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}
		}
	}
}
