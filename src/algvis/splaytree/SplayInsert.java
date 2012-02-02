package algvis.splaytree;

import algvis.core.NodeColor;

public class SplayInsert extends SplayAlg {
	public SplayInsert(SplayTree T, int x) {
		super(T, x);
		T.vv = v = new SplayNode(T, x);
		v.setColor(NodeColor.INSERT);
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
			SplayNode w = find(K);
			splay(w);

			setHeader("insertion");
			w.setColor(NodeColor.NORMAL);
			if (w.key == K) {
				addStep("alreadythere");
				v.goDown();
				v.setColor(NodeColor.NOTFOUND);
				return;
			} else if (w.key < K) {
				addStep("splayinsertleft");
				mysuspend();
				v.linkLeft(w);
				v.linkRight(w.getRight());
				w.setRight(null);
			} else {
				addStep("splayinsertright");
				mysuspend();
				v.linkRight(w);
				v.linkLeft(w.getLeft());
				w.setLeft(null);
			}
			T.root = v;
			T.reposition();
			mysuspend();
		}
		addStep("done");
		v.setColor(NodeColor.NORMAL);
		T.vv = null;
	}
}
