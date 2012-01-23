package algvis.scapegoattree;

import algvis.bst.BSTNode;
import algvis.core.Colors;

public class GBFind extends GBAlg {
	public GBFind(GBTree T, int x) {
		super(T, x);
		v.bgColor(Colors.FIND);
		setHeader("search");
	}

	@Override
	public void run() {
		if (T.root == null) {
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
					if (((GBNode) w).deleted) {
						addStep("gbfinddeleted");
						v.bgColor(Colors.NOTFOUND);
						v.goDown();
					} else {
						addStep("found");
						v.bgColor(Colors.FOUND);
					}
					break;
				} else if (w.key < K) {
					addStep("bstfindright", K, w.key);
					w = w.getRight();
					if (w != null) {
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
					if (w != null) {
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
