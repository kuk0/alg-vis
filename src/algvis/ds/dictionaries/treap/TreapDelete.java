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
package algvis.ds.dictionaries.treap;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.ds.dictionaries.bst.BSTFind;

public class TreapDelete extends Algorithm {
	private final Treap T;
	private final int K;

	public TreapDelete(Treap T, int x) {
		super(T.panel);
		this.T = T;
		K = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("delete", K);
		addNote("bstdeletestart");
		BSTFind find = new BSTFind(T, K, this);
		find.runAlgorithm();
		TreapNode d = (TreapNode) find.getResult().get("node");

		if (d != null) {
			setHeader("delete", K);
			d.setColor(NodeColor.DELETE);

			addStep("treapbubbledown");
			pause();
			// prebubleme k listu
			while (!d.isLeaf()) {
				if (d.getLeft() == null) {
					T.rotate(d.getRight());
				} else if (d.getRight() == null) {
					T.rotate(d.getLeft());
				} else if (d.getRight().p > d.getLeft().p) {
					T.rotate(d.getRight());
				} else {
					T.rotate(d.getLeft());
				}
				pause();
			}
			addStep("treapdeletecase1");
			pause();
			addToScene(d);
			if (d.isRoot()) {
				T.setRoot(null);
			} else if (d.isLeft()) {
				d.getParent().setLeft(null);
			} else {
				d.getParent().setRight(null);
			}
			d.goDown();
			removeFromScene(d);

			T.reposition();
			addStep("done");
		}
	}
}
