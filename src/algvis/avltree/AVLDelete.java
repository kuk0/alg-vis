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
package algvis.avltree;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.Node;

public class AVLDelete extends Algorithm {
	private final AVL T;
	private AVLNode v;
	private final int K;

	public AVLDelete(AVL T, int x) {
		super(T);
		this.T = T;
		v = (AVLNode) T.setV(new AVLNode(T, K = x));
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
			AVLNode d = (AVLNode) T.getRoot();
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
				addStep("notfound");
				return;
			}

			AVLNode w = d.getParent();
			d.setColor(NodeColor.FOUND);
			if (d.isLeaf()) { // case I - list
				addStep("bst-delete-case1");
				mysuspend();
				if (d.isRoot()) {
					T.setRoot(null);
				} else if (d.isLeft()) {
					d.getParent().unlinkLeft();
				} else {
					d.getParent().unlinkRight();
				}
				v.goDown();

			} else if (d.getLeft() == null || d.getRight() == null) { // case
																		// IIa -
																		// 1 syn
				addStep("bst-delete-case2");
				mysuspend();
				AVLNode s = (d.getLeft() == null) ? d.getRight() : d.getLeft();
				if (d.isRoot()) {
					T.setRoot(s);
				} else {
					if (d.isLeft()) {
						d.getParent().linkLeft(s);
					} else {
						d.getParent().linkRight(s);
					}
				}
				v.goDown();

			} else { // case III - 2 synovia
				addStep("bst-delete-case3");
				AVLNode s = d.getRight();
				v = (AVLNode) T.setV(new AVLNode(T, -Node.INF));
				v.setColor(NodeColor.FIND);
				v.goTo(s);
				mysuspend();
				while (s.getLeft() != null) {
					s = s.getLeft();
					v.goTo(s);
					mysuspend();
				}
				w = s.getParent();
				if (w == d) {
					w = s;
				}
				v = (AVLNode) T.setV(s);
				if (s.isLeft()) {
					s.getParent().linkLeft(s.getRight());
				} else {
					s.getParent().linkRight(s.getRight());
				}
				v.goNextTo(d);
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
				v.linkLeft(d.getLeft());
				v.linkRight(d.getRight());
				v.goTo(d);
				v.calc();
				T.setV(d);
				d.goDown();
			} // end case III

			addStep("avldeletebal");
			mysuspend();
			// bubleme nahor
			while (w != null) {
				w.mark();
				w.calc();
				addStep("avlupdatebal");
				mysuspend();
				if (w.balance() == -2) {
					if (w.getLeft().balance() != +1) { // R-rot
						addStep("avlr");
						w.unmark();
						w = w.getLeft();
						w.mark();
						w.setArc(w.getParent());
						mysuspend();
						w.noArc();
						T.rotate(w);
					} else { // LR-rot
						addStep("avllr");
						w.unmark();
						w = w.getLeft().getRight();
						w.mark();
						w.setArc(w.getParent());
						w.getParent().setArc(w.getParent().getParent());
						mysuspend();
						w.noArc();
						w.getParent().noArc();
						T.rotate(w);
						mysuspend();
						T.rotate(w);
					}
					mysuspend();
				} else if (w.balance() == +2) {
					if (w.getRight().balance() != -1) { // L-rot
						addStep("avll");
						w.unmark();
						w = w.getRight();
						w.mark();
						w.setArc(w.getParent());
						mysuspend();
						w.noArc();
						T.rotate(w);
					} else { // RL-rot
						addStep("avlrl");
						w.unmark();
						w = w.getRight().getLeft();
						w.mark();
						w.setArc(w.getParent());
						w.getParent().setArc(w.getParent().getParent());
						mysuspend();
						w.noArc();
						w.getParent().noArc();
						T.rotate(w);
						mysuspend();
						T.rotate(w);
					}
					mysuspend();
				}
				w.unmark();
				w = w.getParent();
			}

			T.reposition();
			addStep("done");
		}
	}
}
