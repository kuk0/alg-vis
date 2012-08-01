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
package algvis.redblacktree;

import algvis.core.Algorithm;

public class RBInsert extends Algorithm {
	private final RB T;
	private final RBNode v;
	private final int K;

	public RBInsert(RB T, int x) {
		super(panel, d);
		this.T = T;
		v = (RBNode) T.setV(new RBNode(T, K = x));
		setHeader("insert", K);
	}

	@Override
	public void run() {
		RBNode w = (RBNode) T.getRoot();
		if (T.getRoot() == null) {
			// v.setLeft(v.setRight(v.setParent(T.NULL)));
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
			pause();
		} else {
			v.goAboveRoot();
			addStep("bst-insert-start");
			pause();

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
				pause();
			}
			// v.setLeft(v.setRight(T.NULL));

			T.reposition();
			pause();

			// bubleme nahor
			w = v;
			RBNode pw = w.getParent2();
			while (!w.isRoot() && pw.isRed()) {
				w.mark();
				boolean isleft = pw.isLeft();
				RBNode ppw = pw.getParent2(), y = (isleft ? ppw.getRight()
						: ppw.getLeft());
				if (y == null)
					y = T.NULL;
				if (y.isRed()) {
					// case 1
					addStep("rbinsertcase1");
					pause();
					pw.setRed(false);
					y.setRed(false);
					ppw.setRed(true);
					w.unmark();
					w = ppw;
					w.mark();
					pw = w.getParent2();
					pause();
				} else {
					// case 2
					if (isleft != w.isLeft()) {
						addStep("rbinsertcase2");
						pause();
						T.rotate(w);
						pause();
					} else {
						w.unmark();
						w = w.getParent2();
						w.mark();
					}
					pw = w.getParent2();
					// case 3
					addStep("rbinsertcase3");
					pause();
					w.setRed(false);
					pw.setRed(true);
					T.rotate(w);
					pause();
					w.unmark();
					break;
				}
			}
		}
		if (w != null)
			w.unmark();
		((RBNode) T.getRoot()).setRed(false);
		T.setV(null);
		T.reposition();
		addStep("done");
	}
}
