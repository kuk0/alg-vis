package algvis.splaytree;

import algvis.bst.BSTNode;
import algvis.core.Colors;

public class SplayInsert extends SplayAlg {
	public SplayInsert(Splay T, int x) {
		super(T, x);
		T.vv = v = new SplayNode(T, x);
		v.bgColor(Colors.INSERT);
	}

	@Override
	public void run() {
		if (T.root == null) {
			T.root = v;
			v.goToRoot();
			setText("newroot");
			mysuspend();
		} else {
			v.goAboveRoot();
			BSTNode w = find(K);
			splay(w);

			setHeader("insertion");
			w.bgColor(Colors.NORMAL);
			if (w.key == K) {
				setText("alreadythere");
				v.goDown();
				v.bgColor(Colors.NOTFOUND);
				return;
			} else if (w.key < K) {
				setText("splayinsertleft");
				mysuspend();
				v.linkLeft(w);
				v.linkRight(w.right);
				w.right = null;
			} else {
				setText("splayinsertright");
				mysuspend();
				v.linkRight(w);
				v.linkLeft(w.left);
				w.left = null;
			}
			T.root = v;
			T.reposition();
			mysuspend();
		}
		setText("done");
		v.bgColor(Colors.NORMAL);
		T.vv = null;
	}
}
