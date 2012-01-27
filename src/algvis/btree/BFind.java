package algvis.btree;

import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class BFind extends Algorithm {
	BTree T;
	BNode v;

	public BFind(BTree T, int x) {
		super(T);
		this.T = T;
		v = T.v = new BNode(T, x);
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
			BNode w = T.root;
			v.goTo(w);
			addStep("bstfindstart");
			mysuspend();

			while (true) {
				if (w.isIn(v.key[0])) {
					addStep("found");
					v.goDown();
					v.setColor(NodeColor.FOUND);
					break;
				}
				if (w.isLeaf()) {
					addStep("notfound");
					v.setColor(NodeColor.NOTFOUND);
					v.goDown();
					break;
				}
				w = w.way(v.key[0]);
				v.goTo(w);
				mysuspend();
			}
		}
	}
}
