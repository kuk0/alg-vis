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

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.core.NodeColor;

public class RBDelete extends Algorithm {
	private RB T;
	private BSTNode v;
	private int K;

	public RBDelete(RB T, int x) {
		super(T);
		this.T = T;
		v = T.setV(new BSTNode(T, K = x));
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
			RBNode d = (RBNode) T.getRoot();
			v.goTo(d);
			addStep("bstdeletestart");
			mysuspend();

			while (true) {
				if (d.getKey() == K) { // found
					v.setColor(NodeColor.FOUND);
					break;
				} else if (d.getKey() < K) { // right
					addStep("bstfindright", K, d.getKey());
					d = d.getRight();
					if (d != null) {
						v.goTo(d);
					} else {
						v.goRight();
						break;
					}
				} else { // left
					addStep("bstfindleft", K, d.getKey());
					d = d.getLeft();
					if (d != null) {
						v.goTo(d);
					} else {
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}

			if (d == null) { // notfound
				addNote("notfound");
				return;
			}

			RBNode u = d, w = (u.getLeft() != null) ? u.getLeft() : u
					.getRight2();
			T.NULL.setParent(u.getParent2());
			d.setColor(NodeColor.FOUND);
			if (d.isLeaf()) { // case I - list
				addStep("bst-delete-case1");
				mysuspend();
				if (d.isRoot()) {
					T.setRoot(null);
				} else if (d.isLeft()) {
					d.getParent().setLeft(null);
				} else {
					d.getParent().setRight(null);
				}
				v.goDown();

			} else if (d.getLeft() == null || d.getRight() == null) {
				// case IIa - 1 syn
				addStep("bst-delete-case2");
				mysuspend();
				BSTNode s = (d.getLeft() == null) ? d.getRight() : d.getLeft();
				if (d.isRoot()) {
					T.setRoot(s);
					s.setParent(null);
				} else {
					s.setParent(d.getParent());
					if (d.isLeft()) {
						d.getParent().setLeft(s);
					} else {
						d.getParent().setRight(s);
					}
				}
				v.goDown();

			} else { // case III - 2 synovia
				addStep("bst-delete-case3");
				RBNode s = d.getRight();
				v = T.setV(new BSTNode(T, -Node.INF));
				v.setColor(NodeColor.FIND);
				v.goTo(s);
				mysuspend();
				while (s.getLeft() != null) {
					s = s.getLeft();
					v.goTo(s);
					mysuspend();
				}
				u = s;
				w = u.getRight2();
				T.NULL.setParent(u.getParent2());
				v = T.setV(s);
				((RBNode) v).setRed(d.isRed());
				if (s.isLeft()) {
					s.getParent().linkLeft(u.getRight());
				} else {
					s.getParent().linkRight(u.getRight());
				}
				v.goNextTo(d);
				mysuspend();
				if (d.getParent() == null) {
					v.setParent(null);
					T.setRoot(v);
				} else {
					if (d.isLeft()) {
						d.getParent().linkLeft(v);
					} else {
						d.getParent().linkRight(v);
					}
				}
				v.linkLeft(d.getLeft());
				v.linkRight(d.getRight());
				v.goTo(d);
				v.calc();
				T.setV(d);
				d.goDown();
			} // end case III

			if (!u.isRed()) {
				// bubleme nahor
				while (w.getParent2() != T.NULL && !w.isRed()) {
					T.NULL.setRed(false);
					if (w.getParent2().getLeft2() == w) {
						RBNode s = w.getParent2().getRight2();
						if (s.isRed()) {
							addStep("rbdelete1");
							mysuspend();
							s.setRed(false);
							w.getParent2().setRed(true);
							T.rotate(s);
						} else if (!s.getLeft2().isRed() && !s.getRight2().isRed()) {
							addStep("rbdelete2");
							mysuspend();
							s.setRed(true);
							w = w.getParent2();
						} else if (!s.getRight2().isRed()) {
							addStep("rbdelete3");
							mysuspend();
							s.getLeft2().setRed(false);
							s.setRed(true);
							T.rotate(s.getLeft());
						} else {
							addStep("rbdelete4");
							mysuspend();
							s.setRed(s.getParent2().isRed());
							w.getParent2().setRed(false);
							s.getRight2().setRed(false);
							T.rotate(s);
							w = (RBNode) T.getRoot();
						}
					} else {
						RBNode s = w.getParent2().getLeft2();
						if (s.isRed()) {
							addStep("rbdelete1");
							mysuspend();
							s.setRed(false);
							w.getParent2().setRed(true);
							T.rotate(s);
						} else if (!s.getRight2().isRed() && !s.getLeft2().isRed()) {
							addStep("rbdelete2");
							mysuspend();
							s.setRed(true);
							w = w.getParent2();
						} else if (!s.getLeft2().isRed()) {
							s.getRight2().setRed(false);
							addStep("rbdelete3");
							mysuspend();
							s.setRed(true);
							T.rotate(s.getRight2());
						} else {
							addStep("rbdelete4");
							mysuspend();
							s.setRed(s.getParent2().isRed());
							w.getParent2().setRed(false);
							s.getLeft2().setRed(false);
							T.rotate(s);
							w = (RBNode) T.getRoot();
						}
					}
					mysuspend();
				}
				w.setRed(false);
			}

			T.reposition();
			addStep("done");
		}
	}
}
