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
			setHeader("insertion");
			T.root = v;
			v.goToRoot();
			addStep("newroot");
			mysuspend();
		} else {
			v.goAboveRoot();
			BSTNode w = find(K);
			splay(w);

			setHeader("insertion");
			w.bgColor(Colors.NORMAL);
			if (w.key == K) {
				addStep("alreadythere");
				v.goDown();
				v.bgColor(Colors.NOTFOUND);
				return;
			} else if (w.key < K) {
				addStep("splayinsertleft");
				mysuspend();
				v.linkLeft(w);
				v.linkRight(w.right);
				w.right = null;
			} else {
				addStep("splayinsertright");
				mysuspend();
				v.linkRight(w);
				v.linkLeft(w.left);
				w.left = null;
			}
			T.root = v;
			T.reposition();
			mysuspend();
		}
		addStep("done");
		v.bgColor(Colors.NORMAL);
		T.vv = null;
	}
}
