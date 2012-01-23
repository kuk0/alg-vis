package algvis.splaytree;

import algvis.core.Colors;
import algvis.core.Node;

public class SplayDelete extends SplayAlg {
	public SplayDelete(Splay T, int x) {
		super(T, x);
	}

	@Override
	public void run() {
		if (T.root == null) {
			s.goToRoot();
			addStep("empty");
			mysuspend();
			s.goDown();
			s.bgColor(Colors.NOTFOUND);
			addStep("notfound");
			return;
		}

		SplayNode w = find(K);
		splay(w);

		setHeader("deletion");
		w.bgColor(Colors.NORMAL);

		if (w.key != s.key) {
			addStep("notfound");
			s.bgColor(Colors.NOTFOUND);
			s.goDown();
			return;
		}

		T.v = w;
		T.v.goDown();
		T.v.bgColor(Colors.DELETE);
		if (w.getLeft() == null) {
			addStep("splaydeleteright");
			T.root = w.getRight();
			T.root.setParent(null);
			T.reposition();
			mysuspend();
		} else if (w.getRight() == null) {
			addStep("splaydeleteleft");
			T.root = w.getLeft();
			T.root.setParent(null);
			T.reposition();
			mysuspend();
		} else {
			addStep("splaydelete");
			T.root2 = w.getLeft();
			T.root2.setParent(null);
			T.root = w.getRight();
			T.root.setParent(null);
			T.vv = s = new SplayNode(T, -Node.INF);
			s.bgColor(Colors.FIND);
			w = w.getRight();
			s.goTo(w);
			mysuspend();
			while (w.getLeft() != null) {
				w = w.getLeft();
				s.goTo(w);
				mysuspend();
			}
			w.bgColor(Colors.FIND);
			T.vv = null;
			// splay
			while (!w.isRoot()) {
				if (w.getParent().isRoot()) {
					T.rotate2(w);
					// setText ("splayroot");
				} else {
					if (w.isLeft() == w.getParent().isLeft()) {
						/*
						 * if (w.isLeft()) setText ("splayzigzigleft"); else
						 * setText ("splayzigzigright");
						 */
						T.rotate2(w.getParent());
						mysuspend();
						T.rotate2(w);
					} else {
						/*
						 * if (!w.isLeft()) setText ("splayzigzagleft"); else
						 * setText ("splayzigzagright");
						 */
						T.rotate2(w);
						mysuspend();
						T.rotate2(w);
					}
				}
				mysuspend();
			}
			addStep("splaydeletelink");
			T.root = w;
			w.bgColor(Colors.NORMAL);
			w.linkLeft(T.root2);
			T.root2 = null;
			T.reposition();
			mysuspend();
		}

		addStep("done");
		T.vv = null;
	}
}
