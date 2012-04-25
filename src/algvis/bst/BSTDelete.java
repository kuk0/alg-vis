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
package algvis.bst;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.Node;

public class BSTDelete extends Algorithm {
	BST T;
	BSTNode v;
	int K;

	public BSTDelete(BST T, int x) { // Buttons B,
		super(T);
		this.T = T;
		v = T.setV(new BSTNode(T, K = x));
		v.setColor(NodeColor.DELETE);
		setHeader("delete", x);
	}

	@Override
	public void run() {
		if (T.getRoot() == null) {
			v.goAboveRoot();
			addStep("empty");
			mysuspend();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
			return;
		} else {
			BSTNode d = T.getRoot();
			v.goAbove(d);
			addNote("bstdeletestart");
			addStep("bstfindstart");
			mysuspend();

			while (true) {
				if (d.getKey() == v.getKey()) { // found
					// v.setColor(NodeColor.FOUND);
					break;
				} else if (d.getKey() < K) { // right
					if (d.getRight() == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(d.getRight());
					}
					addStep("bstfindright", K, d.getKey());
					mysuspend();
					v.noArrow();
					d = d.getRight();
					if (d != null) {
						v.goAbove(d);
					} else {
						addNote("notfound");
						v.goRight();
						break;
					}
				} else { // left
					if (d.getLeft() == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(d.getLeft());
					}
					addStep("bstfindleft", K, d.getKey());
					mysuspend();
					v.noArrow();
					d = d.getLeft();
					if (d != null) {
						v.goAbove(d);
					} else {
						addNote("notfound");
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}

			if (d == null) { // notfound
				addNote("done");
				return;
			}
			v.goTo(d);
			addNote("found");

			if (d.isLeaf()) { // case I - leaf
				addNote("bst-delete-case1");
				addStep("bst-delete-unlink");
				mysuspend();
				if (d.isRoot()) {
					T.setRoot(null);
				} else if (d.isLeft()) {
					d.getParent().unlinkLeft();
				} else {
					d.getParent().unlinkRight();
				}
			} else if (d.getLeft() == null || d.getRight() == null) {
				// case II - 1 child
				addNote("bst-delete-case2");
				BSTNode s;
				if (d.getLeft() == null) {
					s = d.getRight();
				} else {
					s = d.getLeft();
				}
				if (s.isLeft() == d.isLeft()) {
					s.setArc(d.getParent());
				} else {
					s.pointTo(d.getParent());
				}
				if (d.isRoot()) {
					addStep("bst-delete-newroot", K, s.getKey());
				} else {
					addStep("bst-delete-linkpar", K, s.getKey(), d.getParent().getKey());
				}
				mysuspend();
				s.noArc();
				s.noArrow();
				if (d.getLeft() == null) {
					d.unlinkRight();
				} else {
					d.unlinkLeft();
				}
				if (d.isRoot()) {
					T.setRoot(s);
				} else {
					if (d.isLeft()) {
						d.getParent().linkLeft(s);
					} else {
						d.getParent().linkRight(s);
					}
				}
			} else { // case III - 2 children
				addNote("bst-delete-case3", K);
				mysuspend();
				BSTNode s = d.getRight();
				d.setColor(NodeColor.DELETE);
				v = T.setV(new BSTNode(T, -Node.INF));
				v.setColor(NodeColor.FIND);
				v.goAbove(s);
				addStep("bst-delete-succ-start");
				mysuspend();
				while (s.getLeft() != null) {
					addStep("bst-delete-go-left");
					v.pointAbove(s.getLeft());
					mysuspend();
					v.noArrow();
					s = s.getLeft();
					v.goAbove(s);
				}
				v.goTo(s);
				BSTNode p = s.getParent(), r = s.getRight();
				v.setColor(NodeColor.FOUND);
				addNote("bst-delete-succ", K, s.getKey());
				if (r == null) {
					addStep("bst-delete-succ-unlink");
				} else {
					addStep("bst-delete-succ-link", r.getKey(), p.getKey());
					if (s.isLeft()) {
						r.pointTo(p);
					} else {
						r.setArc(p);
					}
				}
				mysuspend();
				if (r != null) {
					r.noArc();
					r.noArrow();
				}
				v = T.setV(s);
				if (s.isLeft()) {
					p.linkLeft(r);
				} else {
					p.linkRight(r);
				}
				v.goNextTo(d);
				mysuspend();
				addStep("bst-delete-replace", K, s.getKey());
				mysuspend();
				if (d.getParent() == null) {
					T.setRoot(v);
				} else {
					if (d.isLeft()) {
						d.getParent().linkLeft(v);
					} else {
						d.getParent().linkRight(v);
					}
				}
				v.setColor(NodeColor.NORMAL);
				v.linkLeft(d.getLeft());
				v.linkRight(d.getRight());
				v.goTo(d);
			} // end case III

			T.setV(d);
			d.goDown();
			T.reposition();
			addNote("done");
		}
	}
}
