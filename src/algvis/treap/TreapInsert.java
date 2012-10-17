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
package algvis.treap;

import algvis.core.Algorithm;

public class TreapInsert extends Algorithm {
	private final Treap T;
	private final TreapNode v;
	private final int K;

	public TreapInsert(Treap T, int x) {
		super(T);
		this.T = T;
		T.setV(v = new TreapNode(T, K = x));
		setHeader("insert", K);
	}

	@Override
	public void run() {
		if (T.getRoot() == null) {
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
			mysuspend();
		} else {
			TreapNode w = (TreapNode)T.getRoot();
			v.goAboveRoot();
			addStep("bst-insert-start");
			mysuspend();

			while (true) {
				if (w.getKey() == K) {
					addStep("alreadythere");
					v.goDown();
					return;
				} else if (w.getKey() < K) {
					addStep("bst-insert-right", K, w.getKey());
					if (w.getRight() != null) {
						w = w.getRight();
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					addStep("bst-insert-left", K, w.getKey());
					if (w.getLeft() != null) {
						w = w.getLeft();
					} else {
						w.linkLeft(v);
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}
			T.reposition();
			mysuspend();
			// bubleme nahor
			addStep("treapbubbleup");
			while (!v.isRoot() && v.getParent().p < v.p) {
				T.rotate(v);
				mysuspend();
			}
		}
		addStep("done");
		T.setV(null);
	}
}
