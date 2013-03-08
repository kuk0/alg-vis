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
package algvis.ds.dictionaries.btree;

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.core.NodeColor;

public class BDelete extends Algorithm {
	private final BTree T;
	private final int K;

	public BDelete(BTree T, int x) {
		super(T.panel);
		this.T = T;
		K = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		BNode v = new BNode(T, K);
		v.setColor(NodeColor.DELETE);
		addToScene(v);
		setHeader("deletion");
		if (T.getRoot() == null) {
			v.goToRoot();
			addStep("empty");
			pause();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
		} else {
			BNode d = T.getRoot();
			v.goAbove(d);
			addStep("bstdeletestart");
			pause();

			while (true) {
				if (d.isIn(K)) {
					break;
				}
				final int p = d.search(K);
				if (p == 0) {
					addStep("bfind0", K, d.keys[0]);
				} else if (p == d.numKeys) {
					addStep("bfindn", d.keys[d.numKeys - 1], K, d.numKeys + 1);
				} else {
					addStep("bfind", d.keys[p - 1], K, d.keys[p], p + 1);
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
				removeFromScene(v);
				return;
			}

			d.setColor(NodeColor.FOUND);
			pause();
			d.setColor(NodeColor.NORMAL);
			removeFromScene(v);
			if (d.isLeaf()) {
				addStep("bdelete1");
				if (d.isRoot() && d.numKeys == 1) {
					v = d;
					T.setRoot(null);
					addToScene(v);
					v.goDown();
				} else {
					v = d.del(K);
					addToScene(v);
					T.reposition();
					v.goDown();
					pause();
				}
				removeFromScene(v);
			} else {
				addStep("bdelete2");
				BNode s = d.way(K + 1);
				v = new BNode(T, -Node.INF, d.tox, d.toy);
				v.setColor(NodeColor.FIND);
				addToScene(v);
				v.goAbove(s);
				pause();
				while (!s.isLeaf()) {
					s = s.c[0];
					v.goAbove(s);
					pause();
				}
				removeFromScene(v);
				v = s.delMin();
				addToScene(v);
				v.goTo(d);
				pause();
				d.replace(K, v.keys[0]);
				removeFromScene(v);
				pause();
				d.setColor(NodeColor.NORMAL);
				d = s;
			}

			while (!d.isRoot() && d.numKeys < (T.order - 1) / 2) {
				d.setColor(NodeColor.NOTFOUND);
				BNode s, s1 = null, s2 = null;
				final BNode p = d.parent;
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
					// treba zobrat prvok z s, nahradit nim p.keys[k]
					// a p.keys[k] pridat do d
					// tiez treba prehodit pointer z s ku d
					if (lefts) {
						addStep("bleft");
					} else {
						addStep("bright");
					}
					v = lefts ? s.delMax() : s.delMin();
					addToScene(v);
					v.goTo(p);
					pause();
					final int pkey = p.keys[k];
					p.keys[k] = v.keys[0];
					removeFromScene(v);
					v = new BNode(T, pkey, p.tox, p.toy);
					addToScene(v);
					v.goTo(d);
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
					break;
				} else {
					// treba spojit vrchol d + p.keys[k] + s
					// zmenit p.c[k] na novy vrchol a posunut to
					addStep("bmerge");
					if (p.isRoot() && p.numKeys == 1) {
						v = new BNode(T.getRoot());
						addToScene(v);
						T.getRoot().keys[0] = Node.NOKEY;
						v.goTo((d.tox + s.tox) / 2, d.y);
						pause();
						if (lefts) {
							T.setRoot(new BNode(s, v, d));
						} else {
							T.setRoot(new BNode(d, v, s));
						}
						break;
					} else {
						v = p.del(p.keys[k]);
						addToScene(v);
						v.goTo((d.tox + s.tox) / 2, d.y);
						pause();
						if (lefts) {
							p.c[k] = new BNode(s, v, d);
						} else {
							p.c[k] = new BNode(d, v, s);
						}
						p.c[k].parent = p;
						--p.numChildren;
						System.arraycopy(p.c, k + 1 + 1, p.c, k + 1,
								p.numChildren - (k + 1));
						d = p;
					}
				}
			}
			T.reposition();
		}
		removeFromScene(v);
		addNote("done");
	}
}
