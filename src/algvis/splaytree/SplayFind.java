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
			setText("bstfindempty");
			mysuspend();
			v.goDown();
			v.bgColor(Colors.NOTFOUND);
			setHeader("search");
			setText("bstfindnotfound");
		} else {
			v.goAboveRoot();
			BSTNode w = find(K);
			splay(w);

			setText("splayinroot");
			mysuspend();

			setHeader("search");
			w.bgColor(Colors.NORMAL);
			v.goToRoot();
			if (w.key == v.key) {
				setText("found");
				v.bgColor(Colors.FOUND);
			} else {
				setText("notfound");
				v.bgColor(Colors.NOTFOUND);
				v.goDown();
			}
			mysuspend();
		}
	}
}
