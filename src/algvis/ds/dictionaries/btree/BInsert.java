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
import algvis.core.NodeColor;

public class BInsert extends Algorithm {
	private final BTree T;
	private final int K;

	public BInsert(BTree T, int x) {
		super(T.panel);
		this.T = T;
		K = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		final BNode v = new BNode(T, K);
		v.setColor(NodeColor.INSERT);
		addToScene(v);
		setHeader("insert", K);
		if (T.getRoot() == null) {
			T.setRoot(v);
			v.goAboveRoot();
			addStep("newroot");
			pause();
			v.setColor(NodeColor.NORMAL);
			removeFromScene(v);
		} else {
			BNode w = T.getRoot();
			v.goAbove(w);
			addStep("bst-insert-start");
			pause();

			while (true) {
				if (w.isIn(K)) {
					addStep("alreadythere");
					v.goDown();
					removeFromScene(v);
					return;
				}
				if (w.isLeaf()) {
					break;
				}
				final int p = w.search(K);
				if (p == 0) {
					addStep("bfind0", K, w.keys[0]);
				} else if (p == w.numKeys) {
					addStep("bfindn", w.keys[w.numKeys - 1], K, w.numKeys + 1);
				} else {
					addStep("bfind", w.keys[p - 1], K, w.keys[p], p + 1);
				}
				w = w.c[p];
				v.goAbove(w);
				pause();
			}

			addStep("binsertleaf");
			w.addLeaf(K);
			if (w.numKeys >= T.order) {
				w.setColor(NodeColor.NOTFOUND);
			}
			removeFromScene(v);
			pause();

			while (w.numKeys >= T.order) {
				addStep("bsplit");
				final int o = (w.parent != null) ? w.order() : -1;
				w = w.split();
				if (w.parent == null) {
					break;
				}
				w.parent.c[o] = w;
				pause();
				w.goBelow(w.parent);
				pause();
				w.parent.add(o, w);
				w = w.parent;
				if (w.numKeys >= T.order) {
					w.setColor(NodeColor.NOTFOUND);
				}
				T.reposition();
				pause();
			}
			if (w.isRoot()) {
				T.setRoot(w);
			}
			T.reposition();
		}
		addNote("done");
	}
}
