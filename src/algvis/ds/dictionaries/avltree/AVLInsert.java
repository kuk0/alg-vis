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

import java.util.HashMap;

import algvis.core.Algorithm;
import algvis.core.DataStructure;
import algvis.core.visual.DoubleArrow;
import algvis.core.visual.ShadeSubtree;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTInsert;

public class AVLInsert extends Algorithm {
	private final AVL T;
	private final int K;

	public AVLInsert(AVL T, int x) {
		super(T.panel);
		this.T = T;
		K = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("insert", K);
		final BSTInsert insert = new BSTInsert(T, new AVLNode(T, K,
				ZDepth.ACTIONNODE), this);
		insert.runAlgorithm();
		final HashMap<String, Object> insertResult = insert.getResult();
		final boolean inserted = (Boolean) insertResult.get("inserted");

		if (inserted) {
			AVLNode w = (AVLNode) insertResult.get("w");
			addNote("avlinsertbal");
			pause();

			// bubleme nahor
			while (w != null) {
				w.mark();
				w.calc();
				addStep("avlupdatebal");
				ShadeSubtree ul = null, um = null, ur = null;
				if (w.getLeft() != null) {
					addToScene(ul = new ShadeSubtree(w.getLeft()));
				}
				if (w.getRight() != null) {
					addToScene(ur = new ShadeSubtree(w.getRight()));
				}
				int hl = (w.getLeft() == null) ? 0 : w.getLeft().height, hr = (w
						.getRight() == null) ? 0 : w.getRight().height;
				DoubleArrow balanceArrow = new DoubleArrow(w.x,
						(int) (w.y + (hl + 0.5) * DataStructure.minsepy), w.x,
						(int) (w.y + (hr + 0.5) * DataStructure.minsepy));
				addToScene(balanceArrow);
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
						removeFromScene(balanceArrow);
						T.rotate(w);
					} else { // LR-rot
						removeFromScene(ul);
						if (w.getLeft().getLeft() != null) {
							addToScene(ul = new ShadeSubtree(w.getLeft().getLeft()));
						}
						addToScene(um = new ShadeSubtree(w.getLeft().getRight()));						
						addStep("avllr");
						w.unmark();
						w = w.getLeft().getRight();
						w.mark();
						w.setArc(w.getParent());
						w.getParent().setArc(w.getParent().getParent());
						pause();
						w.noArc();
						w.getParent().noArc();
						removeFromScene(balanceArrow);
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
						removeFromScene(balanceArrow);
						T.rotate(w);
					} else { // RL-rot
						removeFromScene(ur);
						if (w.getRight().getRight() != null) {
							addToScene(ur = new ShadeSubtree(w.getRight().getRight()));
						}
						addToScene(um = new ShadeSubtree(w.getRight().getLeft()));
						addStep("avlrl");
						w.unmark();
						w = w.getRight().getLeft();
						w.mark();
						w.setArc(w.getParent());
						w.getParent().setArc(w.getParent().getParent());
						pause();
						w.noArc();
						w.getParent().noArc();
						removeFromScene(balanceArrow);
						T.rotate(w);
						pause();
						T.rotate(w);
					}
					pause();
				} else {
					removeFromScene(balanceArrow);
				}
				w.unmark();
				w = w.getParent();
				if (ul != null) {
					removeFromScene(ul);
				}
				if (um != null) {
					removeFromScene(um);
				}
				if (ur != null) {
					removeFromScene(ur);
				}
			}
		}
		T.reposition();
		addNote("done");
	}
}
