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

public class BSTFind extends Algorithm {
	private final BST T;
	private final BSTNode v;
	private final int K;

	public BSTFind(BST T, int x) {
		super(T);
		this.T = T;
		v = T.setV(new BSTNode(T, K = x));
		v.setColor(NodeColor.FIND);
		setHeader("find", K);
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
			BSTNode w = T.getRoot();
			v.goAbove(w);
			addStep("bstfindstart");
			mysuspend();
			while (true) {
				if (w.getKey() == K) {
					v.goTo(w);
					addNote("found");
					v.setColor(NodeColor.FOUND);
					break;
				} else if (w.getKey() < K) {
					if (w.getRight() == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(w.getRight());
					}
					addStep("bstfindright", K, w.getKey());
					mysuspend();
					v.noArrow();
					w.setColor(NodeColor.DARKER);
					if (w.getLeft() != null) w.getLeft().subtreeColor(NodeColor.DARKER);
					w = w.getRight();
					if (w != null) {
						v.goAbove(w);
					} else { // not found
						addNote("notfound");
						v.setColor(NodeColor.NOTFOUND);
						v.goRight();
						break;
					}
				} else {
					if (w.getLeft() == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(w.getLeft());
					}
					addStep("bstfindleft", K, w.getKey());
					mysuspend();
					v.noArrow();
					w.setColor(NodeColor.DARKER);
					if (w.getRight() != null) w.getRight().subtreeColor(NodeColor.DARKER);
					w = w.getLeft();
					if (w != null) {
						v.goAbove(w);
					} else { // notfound
						addNote("notfound");
						v.setColor(NodeColor.NOTFOUND);
						v.goLeft();
						break;
					}
				}
				mysuspend();
			}
		}
		mysuspend();
		if (T.getRoot() != null) {
			T.getRoot().subtreeColor(NodeColor.NORMAL);
		}
		T.setV(null);
	}
}
