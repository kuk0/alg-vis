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
package algvis.aatree;

import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class AAInsert extends Algorithm {
	private AA T;
	private AANode v;
	private int K;

	public AAInsert(AA T, int x) {
		super(T);
		this.T = T;
		T.setV(v = (AANode) T.setV(new AANode(T, K = x)));
		v.setColor(NodeColor.INSERT);
		setHeader("insert", K);
	}

	@Override
	public void run() {
		AANode w = (AANode) T.getRoot();
		if (T.getRoot() == null) {
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
			mysuspend();
			v.setColor(NodeColor.NORMAL);
			T.setV(null);
		} else {
			v.goAboveRoot();
			addStep("bst-insert-start");
			mysuspend();

			while (true) {
				if (w.getKey() == K) {
					addStep("alreadythere");
					v.goDown();
					v.setColor(NodeColor.NOTFOUND);
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
				mysuspend();
			}

			T.reposition();
			mysuspend();

			v.setColor(NodeColor.NORMAL);
			T.setV(null);
			// bubleme nahor
			while (w != null) {
				w.mark();
				addStep("aaok");
				// skew
				if (w.getLeft() != null
						&& w.getLeft().getLevel() == w.getLevel()) {
					addStep("aaskew");
					mysuspend();
					w.unmark();
					w = w.getLeft();
					w.mark();
					w.setArc();
					mysuspend();
					w.noArc();
					T.rotate(w);
					T.reposition();
				}
				// split
				AANode r = w.getRight();
				if (r != null && r.getRight() != null
						&& r.getRight().getLevel() == w.getLevel()) {
					addStep("aasplit");
					w.unmark();
					w = r;
					w.mark();
					w.setArc();
					mysuspend();
					w.noArc();
					T.rotate(w);
					w.setLevel(w.getLevel() + 1);
					T.reposition();
				}
				mysuspend();
				w.unmark();
				w = w.getParent();
			}
		}
		T.reposition();
		addStep("done");
	}
}
