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
package algvis.ds.dictionaries.avltree;

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTFind;

public class AVLDelete extends Algorithm {
	private final AVL T;
	private final int K;

	public AVLDelete(AVL T, int x) {
		super(T.panel);
		this.T = T;
		K = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("delete", K);
		addNote("bstdeletestart");
		final BSTFind find = new BSTFind(T, K, this);
		find.runAlgorithm();
		final AVLNode d = (AVLNode) find.getResult().get("node");

		if (d != null) {
			setHeader("delete", K);
			addToScene(d);
			d.setColor(NodeColor.DELETE);

			AVLNode w = d.getParent();
			if (d.isLeaf()) { // case I - list
				addStep("bst-delete-case1");
				pause();
				if (d.isRoot()) {
					T.setRoot(null);
				} else if (d.isLeft()) {
					d.getParent().unlinkLeft();
				} else {
					d.getParent().unlinkRight();
				}
			} else if (d.getLeft() == null || d.getRight() == null) { // case
				// IIa -
				// 1 syn
				addStep("bst-delete-case2");
				pause();
				final AVLNode s = (d.getLeft() == null) ? d.getRight() : d
						.getLeft();
				if (d.isRoot()) {
					T.setRoot(s);
				} else {
					if (d.isLeft()) {
						d.getParent().linkLeft(s);
					} else {
						d.getParent().linkRight(s);
					}
				}
			} else { // case III - 2 synovia
				addStep("bst-delete-case3");
				AVLNode s = d.getRight();
				AVLNode v = new AVLNode(T, -Node.INF, ZDepth.ACTIONNODE);
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
				if (w == d) {
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
				v.goNextTo(d);
				pause();
				if (d.getParent() == null) {
					T.setRoot(v);
				} else {
					if (d.isLeft()) {
						d.getParent().linkLeft(v);
					} else {
						d.getParent().linkRight(v);
					}
				}
				removeFromScene(v);
				v.linkLeft(d.getLeft());
				v.linkRight(d.getRight());
				v.goTo(d);
				v.calc();
			} // end case III
			d.goDown();
			removeFromScene(d);

			addStep("avldeletebal");
			pause();
			// bubleme nahor
			while (w != null) {
				w.mark();
				w.calc();
				addStep("avlupdatebal");
				pause();
				if (w.balance() == -2) {
					if (w.getLeft().balance() != +1) { // R-rot
						addStep("avlr");
						w.unmark();
						w = w.getLeft();
						w.mark();
						w.setArc(w.getParent());
						pause();
						w.noArc();
						T.rotate(w);
					} else { // LR-rot
						addStep("avllr");
						w.unmark();
						w = w.getLeft().getRight();
						w.mark();
						w.setArc(w.getParent());
						w.getParent().setArc(w.getParent().getParent());
						pause();
						w.noArc();
						w.getParent().noArc();
						T.rotate(w);
						pause();
						T.rotate(w);
					}
					pause();
				} else if (w.balance() == +2) {
					if (w.getRight().balance() != -1) { // L-rot
						addStep("avll");
						w.unmark();
						w = w.getRight();
						w.mark();
						w.setArc(w.getParent());
						pause();
						w.noArc();
						T.rotate(w);
					} else { // RL-rot
						addStep("avlrl");
						w.unmark();
						w = w.getRight().getLeft();
						w.mark();
						w.setArc(w.getParent());
						w.getParent().setArc(w.getParent().getParent());
						pause();
						w.noArc();
						w.getParent().noArc();
						T.rotate(w);
						pause();
						T.rotate(w);
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
