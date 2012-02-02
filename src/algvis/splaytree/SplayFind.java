package algvis.splaytree;

import algvis.core.NodeColor;

public class SplayFind extends SplayAlg {
	public SplayFind(SplayTree T, int x) {
		super(T, x);
		T.vv = v = new SplayNode(T, x);
		v.setColor(NodeColor.FIND);
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			addStep("bstfindempty");
			mysuspend();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			setHeader("search");
			addStep("bstfindnotfound");
		} else {
			v.goAboveRoot();
			SplayNode w = find(K);
			splay(w);

			addStep("splayinroot");
			mysuspend();

			setHeader("search");
			w.setColor(NodeColor.NORMAL);
			v.goToRoot();
			if (w.key == v.key) {
				addStep("found");
				v.setColor(NodeColor.FOUND);
			} else {
				addStep("notfound");
				v.setColor(NodeColor.NOTFOUND);
				v.goDown();
			}
			mysuspend();
		}
	}
}
