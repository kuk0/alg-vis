/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ds.dictionaries.aatree;

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTFind;
import algvis.ds.dictionaries.bst.BSTNode;

public class AADelete extends Algorithm {
	private final AA T;
	private final int K;

	public AADelete(AA T, int x) {
		super(T.panel);
		this.T = T;
		K = x;
	}

	// TODO niektore kroky ("This node is OK) nemaju pauzy
	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("delete", K);
		addNote("bstdeletestart");
		final BSTFind find = new BSTFind(T, K, this);
		find.runAlgorithm();
		final AANode toDelete = (AANode) find.getResult().get("node");

		if (toDelete != null) {
			setHeader("delete", K);
			addToScene(toDelete);
			toDelete.setColor(NodeColor.DELETE);

			AANode w = toDelete.getParent();
			if (toDelete.isLeaf()) { // case I - list
				addStep("bst-delete-case1");
				pause();
				if (toDelete.isRoot()) {
					T.setRoot(null);
				} else if (toDelete.isLeft()) {
					toDelete.getParent().unlinkLeft();
				} else {
					toDelete.getParent().unlinkRight();
				}
			} else if (toDelete.getLeft() == null
					|| toDelete.getRight() == null) { // case
				// IIa -
				// 1 syn
				addStep("bst-delete-case2");
				pause();
				final AANode s = (toDelete.getLeft() == null) ? toDelete
						.getRight() : toDelete.getLeft();
				if (toDelete.isRoot()) {
					T.setRoot(s);
				} else {
					if (toDelete.isLeft()) {
						toDelete.getParent().linkLeft(s);
					} else {
						toDelete.getParent().linkRight(s);
					}
				}
			} else { // case III - 2 synovia
				addStep("bst-delete-case3");
				final int lev = toDelete.getLevel();
				AANode s = toDelete.getRight();
				BSTNode v = new BSTNode(T, -Node.INF, ZDepth.ACTIONNODE);
				v.setColor(NodeColor.FIND);
				addToScene(v);
				v.goTo(s);
				pause();
				while (s.getLeft() != null) {
					s = s.getLeft();
					v.goTo(s);
					pause();
				}
				w = s.getParent();
				if (w == toDelete) {
					w = s;
				}
				removeFromScene(v);
				v = s;
				addToScene(v);
				if (s.isLeft()) {
					s.getParent().linkLeft(s.getRight());
				} else {
					s.getParent().linkRight(s.getRight());
				}
				v.goNextTo(toDelete);
				pause();
				v.setLevel(lev);
				if (toDelete.getParent() == null) {
					T.setRoot(v);
				} else {
					if (toDelete.isLeft()) {
						toDelete.getParent().linkLeft(v);
					} else {
						toDelete.getParent().linkRight(v);
					}
				}
				removeFromScene(v);
				v.linkLeft(toDelete.getLeft());
				v.linkRight(toDelete.getRight());
				v.goTo(toDelete);
				v.calc();
			} // end case III
			toDelete.goDown();
			removeFromScene(toDelete);
			T.reposition();
			pause();

			// bubleme nahor
			while (w != null) {
				final int ll = (w.getLeft() == null) ? 0 : w.getLeft()
						.getLevel(), rl = (w.getRight() == null) ? 0 : w
						.getRight().getLevel();
				int wl = w.getLevel();
				addStep("aaok");
				w.mark();
				if (ll < wl - 1 || rl < wl - 1) {
					wl--;
					w.setLevel(w.getLevel() - 1);
					if (rl > wl) {
						w.getRight().setLevel(wl);
					}
					// skew
					if (w.getLeft() != null
							&& w.getLeft().getLevel() == w.getLevel()) {
						addStep("aaskew");
						pause();
						w.unmark();
						w = w.getLeft();
						w.mark();
						w.setArc();
						pause();
						w.noArc();
						T.rotate(w);
						T.reposition();
					}

					if (w.getRight() != null) {
						T.skew(w.getRight());
						AANode r = w.getRight();
						if (r.getLeft() != null
								&& r.getLeft().getLevel() == r.getLevel()) {
							addStep("aaskew2");
							r.getLeft().setArc(r);
							pause();
							r.getLeft().noArc();
							pause();
							T.rotate(r.getLeft());
							T.reposition();
						}
						if (w.getRight().getRight() != null) {
							r = w.getRight().getRight();
							if (r.getLeft() != null
									&& r.getLeft().getLevel() == r.getLevel()) {
								addStep("aaskew3");
								r.getLeft().setArc(r);
								pause();
								r.getLeft().noArc();
								T.rotate(r.getLeft());
								T.reposition();
							}
						}
					}

					AANode r = w.getRight();
					if (r != null && r.getRight() != null
							&& r.getRight().getLevel() == w.getLevel()) {
						addStep("aasplit");
						r.setArc();
						pause();
						r.noArc();
						w.unmark();
						w = r;
						w.mark();
						T.rotate(w);
						w.setLevel(w.getLevel() + 1);
						T.reposition();
					}

					pause();
					if (w.getRight() != null) {
						r = w.getRight().getRight();
						if (r != null
								&& r.getRight() != null
								&& r.getRight().getLevel() == w.getRight()
										.getLevel()) {
							addStep("aasplit2");
							r.setArc();
							pause();
							r.noArc();
							T.rotate(r);
							r.setLevel(r.getLevel() + 1);
							T.reposition();
						}
					}
					pause();
				}
				w.unmark();
				w = w.getParent();
			}

			T.reposition();
			addNote("done");
		}
	}
}
