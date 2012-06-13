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
import algvis.core.NodeColor;

public class TreapDelete extends Algorithm {
	Treap T;
	TreapNode v;
	int K;

	public TreapDelete(Treap T, int x) {
		super(T);
		this.T = T;
		T.setV(v = new TreapNode(T, K = x));
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
			return;
		} else {
			TreapNode d = (TreapNode)T.getRoot();
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

			d.setColor(NodeColor.FOUND);
			T.setV(null);
			addStep("treapbubbledown");
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
				mysuspend();
			}
			T.setV(d);
			addStep("treapdeletecase1");
			mysuspend();
			if (d.isRoot()) {
				T.setRoot(null);
			} else if (d.isLeft()) {
				d.getParent().setLeft(null);
			} else {
				d.getParent().setRight(null);
			}
			d.goDown();

			T.reposition();
			addStep("done");
		}
	}
}
