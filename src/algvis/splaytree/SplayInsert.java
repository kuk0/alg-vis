package algvis.splaytree;

import algvis.core.NodeColor;

public class SplayInsert extends SplayAlg {
	public SplayInsert(SplayTree T, int x) {
		super(T, x);
		T.setVV(v = new SplayNode(T, x));
		v.setColor(NodeColor.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		if (T.getRoot() == null) {
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
			mysuspend();
		} else {
			v.goAboveRoot();
			SplayNode w = find(K);
			splay(w);

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
			T.setRoot(v);
			T.reposition();
			mysuspend();
		}
		addStep("done");
		v.setColor(NodeColor.NORMAL);
		T.setVV(null);
	}
}
