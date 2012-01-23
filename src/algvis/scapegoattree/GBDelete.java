package algvis.scapegoattree;

import algvis.core.Colors;

public class GBDelete extends GBAlg {
	public GBDelete(GBTree T, int x) {
		super(T, x);
		v.bgColor(Colors.DELETE);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			addStep("empty");
			mysuspend();
			v.goDown();
			v.bgColor(Colors.NOTFOUND);
			addStep("notfound");
		} else {
			GBNode w = (GBNode) T.root;
			v.goTo(w);
			addStep("bstfindstart");
			mysuspend();
			while (true) {
				if (w.key == K) {
					if (w.deleted) {
						addStep("gbdeletedeleted");
						v.bgColor(Colors.NOTFOUND);
						v.goDown();
					} else {
						addStep("gbdeletemark");
						w.deleted = true;
						w.bgColor(GBNode.DELETED);
						++T.del;
						T.v = null;
					}
					break;
				} else if (w.key < K) {
					addStep("bstfindright", K, w.key);
					w = w.getRight();
					if (w != null) {
						v.goTo(w);
					} else { // notfound
						addStep("notfound");
						v.bgColor(Colors.NOTFOUND);
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
						v.bgColor(Colors.NOTFOUND);
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}

			// rebuilding
			GBNode b = (GBNode) T.root;
			if (b.size < 2 * T.del) {
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
						if (r.deleted) {
							--T.del;
							if (b == r) {
								b = r.getRight();
							}
							T.v = r;
							if (r.getParent() == null) {
								T.root = r = r.getRight();
								if (r != null) {
									r.setParent(null);
								}
							} else {
								r.getParent().linkRight(r = r.getRight());
							}
							T.v.goDown();
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
