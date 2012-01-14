package algvis.splaytree;

import algvis.bst.BSTNode;
import algvis.core.Colors;

public class SplayFind extends SplayAlg {
	public SplayFind(Splay T, int x) {
		super(T, x);
		T.vv = v = new SplayNode(T, x);
		v.bgColor(Colors.FIND);
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			addStep("bstfindempty");
			mysuspend();
			v.goDown();
			v.bgColor(Colors.NOTFOUND);
			setHeader("search");
			addStep("bstfindnotfound");
		} else {
			v.goAboveRoot();
			BSTNode w = find(K);
			splay(w);

			addStep("splayinroot");
			mysuspend();

			setHeader("search");
			w.bgColor(Colors.NORMAL);
			v.goToRoot();
			if (w.key == v.key) {
				addStep("found");
				v.bgColor(Colors.FOUND);
			} else {
				addStep("notfound");
				v.bgColor(Colors.NOTFOUND);
				v.goDown();
			}
			mysuspend();
		}
	}
}
