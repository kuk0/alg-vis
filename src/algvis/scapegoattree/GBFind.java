package algvis.scapegoattree;

import algvis.bst.BSTNode;
import algvis.core.NodeColor;

public class GBFind extends GBAlg {
	public GBFind(GBTree T, int x) {
		super(T, x);
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
			v.goTo(w);
			addStep("bstfindstart");
			mysuspend();
			while (true) {
				if (w.key == K) {
					if (((GBNode) w).deleted) {
						addStep("gbfinddeleted");
						v.setColor(NodeColor.NOTFOUND);
						v.goDown();
					} else {
						addStep("found");
						v.setColor(NodeColor.FOUND);
					}
					break;
				} else if (w.key < K) {
					addStep("bstfindright", K, w.key);
					w = w.getRight();
					if (w != null) {
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
					if (w != null) {
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
