package algvis;

public class BDelete extends Algorithm {
	BTree T;
	BNode v;
	int K;

	public BDelete(BTree T, int x) {
		super(T.M);
		this.T = T;
		K = x;
		v = T.v = new BNode(T, x);
		v.bgColor(Node.DELETE);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			setText("empty");
			mysuspend();
			v.goDown();
			v.bgColor(Node.NOTFOUND);
			setText("notfound");
		} else {
			BNode d = T.root;
			v.goAbove(d);
			setText("bstdeletestart");
			mysuspend();

			while (true) {
				if (d.isIn(K)) {
					break;
				}
				int p = d.search(K);
				if (p == 0) {
					setText("bfind0", K, d.key[0]);
				} else if (p == d.numKeys) {
					setText("bfindn", d.key[d.numKeys - 1], K, d.numKeys + 1);
				} else {
					setText("bfind", d.key[p - 1], K, d.key[p], p + 1);
				}
				d = d.c[p];
				if (d == null) {
					break;
				}
				v.goAbove(d);
				mysuspend();
			}

			if (d == null) { // notfound
				setText("notfound");
				v.goDown();
				return;
			}

			d.bgColor(Node.FOUND);
			mysuspend();
			d.bgColor(Node.NORMAL);
			if (d.isLeaf()) {
				setText("bdelete1");
				if (d.isRoot() && d.numKeys == 1) {
					T.v = d;
					T.root = null;
					T.v.goDown();
				} else {
					T.v = d.del(K);
					T.reposition();
					T.v.goDown();
					mysuspend();
				}
			} else {
				setText("bdelete2");
				BNode s = d.way(K + 1);
				v = T.v = new BNode(T, -Node.INF, d.x, d.y);
				v.goAbove(s);
				mysuspend();
				while (!s.isLeaf()) {
					s = s.c[0];
					v.goAbove(s);
					mysuspend();
				}
				v = T.v = s.delMin();
				v.goTo(d);
				mysuspend();
				d.replace(K, v.key[0]);
				T.v = null;
				mysuspend();
				d.bgColor(Node.NORMAL);
				d = s;
			}

			while (!d.isRoot() && d.numKeys < (T.order - 1) / 2) {
				d.bgColor(Node.NOTFOUND);
				BNode s, s1 = null, s2 = null, p = d.parent;
				boolean lefts = true;
				int k = d.order(), n1 = 0, n2 = 0;
				if (k > 0) {
					s1 = p.c[k - 1];
					n1 = s1.numKeys;
				}
				if (k + 1 < p.numChildren) {
					s2 = p.c[k + 1];
					n2 = s2.numKeys;
				}
				if (n1 >= n2) {
					s = s1;
					--k;
				} else {
					s = s2;
					lefts = false;
				}

				if (s.numKeys > (T.order - 1) / 2) {
					// treba zobrat prvok z s, nahradit nim p.key[k]
					// a p.key[k] pridat do d
					// tiez treba prehodit pointer z s ku d
					if (lefts) {
						setText("bleft");
					} else {
						setText("bright");
					}
					T.v = lefts ? s.delMax() : s.delMin();
					T.v.goTo(p);
					mysuspend();
					int pkey = p.key[k];
					p.key[k] = T.v.key[0];
					T.v = new BNode(T, pkey, p.x, p.y);
					T.v.goTo(d);
					mysuspend();
					if (lefts) {
						d.insMin(pkey);
						if (!d.isLeaf()) {
							d.insMinCh(s.delMaxCh());
							d.c[0].parent = d;
						}
					} else {
						d.insMax(pkey);
						if (!d.isLeaf()) {
							d.insMaxCh(s.delMinCh());
							d.c[d.numChildren - 1].parent = d;
						}
					}
					d.bgColor(Node.NORMAL);
					T.v = null;
					break;
				} else {
					// treba spojit vrchol d + p.key[k] + s
					// zmenit p.c[k] na novy vrchol a posunut to
					setText("bmerge");
					if (p.isRoot() && p.numKeys == 1) {
						T.v = new BNode(T.root);
						T.root.key[0] = Node.NOKEY;
						T.v.goTo((d.tox + s.tox) / 2, d.y);
						mysuspend();
						if (lefts) {
							T.root = new BNode(s, T.v, d);
						} else {
							T.root = new BNode(d, T.v, s);
						}
						break;
					} else {
						T.v = p.del(p.key[k]);
						T.v.goTo((d.tox + s.tox) / 2, d.y);
						mysuspend();
						if (lefts) {
							p.c[k] = new BNode(s, T.v, d);
						} else {
							p.c[k] = new BNode(d, T.v, s);
						}
						p.c[k].parent = p;
						--p.numChildren;
						for (int i = k + 1; i < p.numChildren; ++i) {
							p.c[i] = p.c[i + 1];
						}
						d = p;
					}
				}
			}
			T.v = null;
			T.reposition();
		}
	}
}
