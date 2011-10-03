package algvis.scapegoattree;

import algvis.bst.BSTNode;
import algvis.core.Node;

public class GBInsert extends GBAlg {
	public GBInsert(GBTree T, int x) {
		super(T, x);
		v.bgColor(Node.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			T.root = v;
			v.goToRoot();
			setText("newroot");
			mysuspend();
			v.bgColor(Node.NORMAL);
		} else {
			BSTNode w = T.root;
			v.goAboveRoot();
			setText("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					if (((GBNode) w).deleted) {
						setText("gbinsertunmark");
						((GBNode) w).deleted = false;
						w.bgColor(Node.NORMAL);
						--T.del;
						T.v = null;
					} else {
						setText("alreadythere");
						v.goDown();
						v.bgColor(Node.NOTFOUND);
					}
					return;
				} else if (w.key < K) {
					setText("bstinsertright", K, w.key);
					if (w.right != null) {
						w = w.right;
					} else {
						w.linkright(v);
						break;
					}
				} else {
					setText("bstinsertleft", K, w.key);
					if (w.left != null) {
						w = w.left;
					} else {
						w.linkleft(v);
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}
			v.bgColor(Node.NORMAL);
			T.reposition();
			mysuspend();

			BSTNode b = null;
			while (w != null) {
				w.calc();
				if (w.height > Math.ceil(T.alpha * T.lg(w.size)) && b == null) {
					b = w;
				}
				w = w.parent;
			}

			// rebuilding
			if (b != null) {
				BSTNode r = b;
				int s = 0;
				setText("gbtoohigh");
				r.mark();
				mysuspend();
				// to vine
				setText("gbrebuild1");
				while (r != null) {
					if (r.left == null) {
						r.unmark();
						if (((GBNode) r).deleted) {
							--T.del;
							if (b == r) {
								b = r.right;
							}
							T.v = r;
							if (r.parent == null) {
								T.root = r = r.right;
								if (r != null) {
									r.parent = null;
								}
							} else {
								r.parent.linkright(r = r.right);
							}
							T.v.goDown();
						} else {
							r = r.right;
							++s;
						}
						if (r != null) {
							r.mark();
						}
					} else {
						if (b == r) {
							b = r.left;
						}
						r.unmark();
						r = r.left;
						r.mark();
						T.rotate(r);
					}
					T.reposition();
					mysuspend();
				}

				// to tree
				setText("gbrebuild2");
				int c = 1;
				for (int i = 0, l = (int) Math.floor(T.lg(s + 1)); i < l; ++i) {
					c *= 2;
				}
				c = s + 1 - c;

				b = compr(b, c);
				s -= c;
				while (s > 1) {
					b = compr(b, s /= 2);
				}
			}
		}
		T.reposition();
		setText("done");
		T.v = null;
	}
}
