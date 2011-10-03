package algvis.scapegoattree;

import algvis.bst.BSTNode;
import algvis.core.Node;

public class GBFind extends GBAlg {
	public GBFind(GBTree T, int x) {
		super(T, x);
		v.bgColor(Node.FIND);
		setHeader("search");
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			setText("empty");
			mysuspend();
			v.goDown();
			v.bgColor(Node.NOTFOUND);
			setText("notfound");
		} else {
			BSTNode w = T.root;
			v.goTo(w);
			setText("bstfindstart");
			mysuspend();
			while (true) {
				if (w.key == K) {
					if (((GBNode) w).deleted) {
						setText("gbfinddeleted");
						v.bgColor(Node.NOTFOUND);
						v.goDown();
					} else {
						setText("found");
						v.bgColor(Node.FOUND);
					}
					break;
				} else if (w.key < K) {
					setText("bstfindright", K, w.key);
					w = w.right;
					if (w != null) {
						v.goTo(w);
					} else { // notfound
						setText("notfound");
						v.bgColor(Node.NOTFOUND);
						v.goRight();
						break;
					}
				} else {
					setText("bstfindleft", K, w.key);
					w = w.left;
					if (w != null) {
						v.goTo(w);
					} else { // notfound
						setText("notfound");
						v.bgColor(Node.NOTFOUND);
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}
		}
	}
}
