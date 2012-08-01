/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.btree;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.Node;

public class BDelete extends Algorithm {
	private final BTree T;
	private BNode v;
	private final int K;

	public BDelete(BTree T, int x) {
		super(panel, d);
		this.T = T;
		K = x;
		v = T.v = new BNode(T, x);
		v.setColor(NodeColor.DELETE);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			addStep("empty");
			pause();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
		} else {
			BNode d = T.root;
			v.goAbove(d);
			addStep("bstdeletestart");
			pause();

			while (true) {
				if (d.isIn(K)) {
					break;
				}
				int p = d.search(K);
				if (p == 0) {
					addStep("bfind0", K, d.key[0]);
				} else if (p == d.numKeys) {
					addStep("bfindn", d.key[d.numKeys - 1], K, d.numKeys + 1);
				} else {
					addStep("bfind", d.key[p - 1], K, d.key[p], p + 1);
				}
				d = d.c[p];
				if (d == null) {
					break;
				}
				v.goAbove(d);
				pause();
			}

			if (d == null) { // notfound
				addStep("notfound");
				v.goDown();
				return;
			}

			d.setColor(NodeColor.FOUND);
			pause();
			d.setColor(NodeColor.NORMAL);
			if (d.isLeaf()) {
				addStep("bdelete1");
				if (d.isRoot() && d.numKeys == 1) {
					T.v = d;
					T.root = null;
					T.v.goDown();
				} else {
					T.v = d.del(K);
					T.reposition();
					T.v.goDown();
					pause();
				}
			} else {
				addStep("bdelete2");
				BNode s = d.way(K + 1);
				v = T.v = new BNode(T, -Node.INF, d.x, d.y);
				v.goAbove(s);
				pause();
				while (!s.isLeaf()) {
					s = s.c[0];
					v.goAbove(s);
					pause();
				}
				v = T.v = s.delMin();
				v.goTo(d);
				pause();
				d.replace(K, v.key[0]);
				T.v = null;
				pause();
				d.setColor(NodeColor.NORMAL);
				d = s;
			}

			while (!d.isRoot() && d.numKeys < (T.order - 1) / 2) {
				d.setColor(NodeColor.NOTFOUND);
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
						addStep("bleft");
					} else {
						addStep("bright");
					}
					T.v = lefts ? s.delMax() : s.delMin();
					T.v.goTo(p);
					pause();
					int pkey = p.key[k];
					p.key[k] = T.v.key[0];
					T.v = new BNode(T, pkey, p.x, p.y);
					T.v.goTo(d);
					pause();
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
					d.setColor(NodeColor.NORMAL);
					T.v = null;
					break;
				} else {
					// treba spojit vrchol d + p.key[k] + s
					// zmenit p.c[k] na novy vrchol a posunut to
					addStep("bmerge");
					if (p.isRoot() && p.numKeys == 1) {
						T.v = new BNode(T.root);
						T.root.key[0] = Node.NOKEY;
						T.v.goTo((d.tox + s.tox) / 2, d.y);
						pause();
						if (lefts) {
							T.root = new BNode(s, T.v, d);
						} else {
							T.root = new BNode(d, T.v, s);
						}
						break;
					} else {
						T.v = p.del(p.key[k]);
						T.v.goTo((d.tox + s.tox) / 2, d.y);
						pause();
						if (lefts) {
							p.c[k] = new BNode(s, T.v, d);
						} else {
							p.c[k] = new BNode(d, T.v, s);
						}
						p.c[k].parent = p;
						--p.numChildren;
                        System.arraycopy(p.c, k + 1 + 1, p.c, k + 1, p.numChildren - (k + 1));
						d = p;
					}
				}
			}
			T.v = null;
			T.reposition();
		}
	}
}
