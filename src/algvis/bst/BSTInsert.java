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

public class BSTInsert extends Algorithm {
	BST T;
	BSTNode v;
	int K;

	public BSTInsert(BST T, int x) {
		super(T);
		this.T = T;
		v = T.setV(new BSTNode(T, K = x));
		v.setColor(NodeColor.INSERT);
		setHeader("insert", K);
	}

	@Override
	public void run() {
		if (T.getRoot() == null) {
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
		} else {
			BSTNode w = T.getRoot();
			v.goAboveRoot();
			addStep("bst-insert-start");
			mysuspend();

			while (true) {
				if (w.key == K) {
					addStep("alreadythere");
					v.setColor(NodeColor.NOTFOUND);
					v.goDown();
					return;
				} else if (w.key < K) {
					if (w.getRight() == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(w.getRight());
					}
					addStep("bst-insert-right", K, w.key);
					mysuspend();
					v.noArrow();
					if (w.getRight() != null) {
						w = w.getRight();
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					if (w.getLeft() == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(w.getLeft());
					}
					addStep("bst-insert-left", K, w.key);
					mysuspend();
					v.noArrow();
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
		}
		T.reposition();
		mysuspend();
		addNote("done");
		v.setColor(NodeColor.NORMAL);		
		T.setV(null);
	}
}
