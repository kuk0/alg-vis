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
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;

public class BSTInsert extends Algorithm {
	private final BST T;
	private final int K;

	public BSTInsert(BST T, int x) {
		super(T.panel);
		this.T = T;
		K = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		BSTNode v = new BSTNode(T, K, ZDepth.ACTIONNODE);
		addToScene(v);
		v.setState(Node.ALIVE);
		v.setColor(NodeColor.INSERT);
		setHeader("insert", K);
		if (T.getRoot() == null) {
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
		} else {
			BSTNode w = T.getRoot();
			v.goAboveRoot();
			addStep("bst-insert-start");
			pause();

			while (true) {
				if (w.getKey() == K) {
					addStep("alreadythere");
					v.setColor(NodeColor.NOTFOUND);
					v.goDown();
					removeFromScene(v);
					return;
				} else if (w.getKey() < K) {
					if (w.getRight() == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(w.getRight());
					}
					addStep("bst-insert-right", K, w.getKey());
					pause();
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
					addStep("bst-insert-left", K, w.getKey());
					pause();
					v.noArrow();
					if (w.getLeft() != null) {
						w = w.getLeft();
					} else {
						w.linkLeft(v);
						break;
					}
				}
				v.goAbove(w);
				pause();
			}
		}
		T.reposition();
		pause();
		addStep("done");
		v.setColor(NodeColor.NORMAL);
		removeFromScene(v);
//		v.setZDepth(ZDepth.DS);
	}

	@Override
	public Object getResult() {
		return null;
	}
}
