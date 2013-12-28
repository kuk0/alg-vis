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
package algvis.ds.dictionaries.redblacktree;

import java.util.HashMap;

import algvis.core.Algorithm;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTInsert;

public class RBInsert extends Algorithm {
	private final RB T;
	private final int K;

	public RBInsert(RB T, int x) {
		super(T.panel);
		this.T = T;
		K = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("insert", K);
		final BSTInsert insert = new BSTInsert(T, new RBNode(T, K,
				ZDepth.ACTIONNODE), this);
		insert.runAlgorithm();
		final HashMap<String, Object> insertResult = insert.getResult();
		final boolean inserted = (Boolean) insertResult.get("inserted");

		if (inserted) {
			// TODO komentar "ideme bublat" (nieco ako pri BSTDelete:
			// "first we have to find a node")
			pause();

			// bubleme nahor
			RBNode w = (RBNode) insertResult.get("v");
			RBNode pw = w.getParent2();
			while (!w.isRoot() && pw.isRed()) {
				w.mark();
				final boolean isleft = pw.isLeft();
				final RBNode ppw = pw.getParent2();
				RBNode y = (isleft ? ppw.getRight() : ppw.getLeft());
				if (y == null) {
					y = T.NULL;
				}
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

			w.unmark();
			((RBNode) T.getRoot()).setRed(false);
			T.reposition();
			addNote("done");
		}
	}
}
