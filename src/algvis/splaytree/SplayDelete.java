package algvis.splaytree;

import algvis.bst.BSTNode;
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
			setText("empty");
			mysuspend();
			s.goDown();
			s.bgColor(Colors.NOTFOUND);
			setText("notfound");
			return;
		}

		BSTNode w = find(K);
		splay(w);

		setHeader("deletion");
		w.bgColor(Colors.NORMAL);

		if (w.key != s.key) {
			setText("notfound");
			s.bgColor(Colors.NOTFOUND);
			s.goDown();
			return;
		}

		T.v = w;
		T.v.goDown();
		T.v.bgColor(Colors.DELETE);
		if (w.left == null) {
			setText("splaydeleteright");
			T.root = w.right;
			T.root.parent = null;
			T.reposition();
			mysuspend();
		} else if (w.right == null) {
			setText("splaydeleteleft");
			T.root = w.left;
			T.root.parent = null;
			T.reposition();
			mysuspend();
		} else {
			setText("splaydelete");
			T.root2 = w.left;
			T.root2.parent = null;
			T.root = w.right;
			T.root.parent = null;
			T.vv = s = new SplayNode(T, -Node.INF);
			s.bgColor(Colors.FIND);
			w = w.right;
			s.goTo(w);
			mysuspend();
			while (w.left != null) {
				w = w.left;
				s.goTo(w);
				mysuspend();
			}
			w.bgColor(Colors.FIND);
			T.vv = null;
			// splay
			while (!w.isRoot()) {
				if (w.parent.isRoot()) {
					T.rotate2(w);
					// setText ("splayroot");
				} else {
					if (w.isLeft() == w.parent.isLeft()) {
						/*
						 * if (w.isLeft()) setText ("splayzigzigleft"); else
						 * setText ("splayzigzigright");
						 */
						T.rotate2(w.parent);
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
			setText("splaydeletelink");
			T.root = w;
			w.bgColor(Colors.NORMAL);
			w.linkleft(T.root2);
			T.root2 = null;
			T.reposition();
			mysuspend();
		}

		setText("done");
		T.vv = null;
	}
}
