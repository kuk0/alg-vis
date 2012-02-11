package algvis.scapegoattree;

import algvis.core.NodeColor;

public class GBDelete extends GBAlg {
	public GBDelete(GBTree T, int x) {
		super(T, x);
		v.setColor(NodeColor.DELETE);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (T.getRoot() == null) {
			v.goToRoot();
			addStep("empty");
			mysuspend();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
		} else {
			GBNode w = (GBNode) T.getRoot();
			v.goTo(w);
			addStep("bstfindstart");
			mysuspend();
			while (true) {
				if (w.key == K) {
					if (w.isDeleted()) {
						addStep("gbdeletedeleted");
						v.setColor(NodeColor.NOTFOUND);
						v.goDown();
					} else {
						addStep("gbdeletemark");
						w.setDeleted(true);
						w.setColor(NodeColor.DELETED);
						T.setDel(T.getDel() + 1);
						T.setV(null);
					}
					break;
				} else if (w.key < K) {
					addStep("bstfindright", K, w.key);
					w = w.getRight();
					if (w != null) {
						v.goTo(w);
					} else { // notfound
						addStep("notfound");
						v.setColor(NodeColor.NOTFOUND);
						v.goRight();
						break;
					}
				} else {
					addStep("bstfindleft", K, w.key);
					w = w.getLeft();
					if (w != null) {
						v.goTo(w);
					} else { // notfound
						addStep("notfound");
						v.setColor(NodeColor.NOTFOUND);
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}

			// rebuilding
			GBNode b = (GBNode) T.getRoot();
			if (b.size < 2 * T.getDel()) {
				addStep("gbdeleterebuild");
				GBNode r = b;
				int s = 0;
				r.mark();
				mysuspend();
				// to vine
				addStep("gbrebuild1");
				while (r != null) {
					if (r.getLeft() == null) {
						r.unmark();
						if (r.isDeleted()) {
							T.setDel(T.getDel() - 1);
							if (b == r) {
								b = r.getRight();
							}
							T.setV(r);
							if (r.getParent() == null) {
								T.setRoot(r = r.getRight());
								if (r != null) {
									r.setParent(null);
								}
							} else {
								r.getParent().linkRight(r = r.getRight());
							}
							T.getV().goDown();
						} else {
							r = r.getRight();
							++s;
						}
						if (r != null) {
							r.mark();
						}
					} else {
						if (b == r) {
							b = r.getLeft();
						}
						r.unmark();
						r = r.getLeft();
						r.mark();
						T.rotate(r);
					}
					T.reposition();
					mysuspend();
				}

				// to tree
				addStep("gbrebuild2");
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
	}
}
