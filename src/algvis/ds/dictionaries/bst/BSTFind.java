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
package algvis.ds.dictionaries.bst;

import java.util.HashMap;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;

public class BSTFind extends Algorithm {
	private final BST T;
	private final int K;
	private final HashMap<String, Object> result = new HashMap<String, Object>(); // node

	public BSTFind(BST T, int x) {
		this(T, x, null);
	}

	public BSTFind(BST T, int x, Algorithm a) {
		super(T.panel, a);
		this.T = T;
		K = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		result.put("node", null);
		BSTNode v = new BSTNode(T, K, ZDepth.ACTIONNODE);
		v.setColor(NodeColor.FIND);
		addToScene(v);
		setHeader("find", K);
		if (T.getRoot() == null) {
			v.goToRoot();
			addStep("empty");
			pause();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
		} else {
			BSTNode w = T.getRoot();
			v.goAbove(w);
			addStep("bstfindstart");
			pause();
			while (true) {
				if (w.getKey() == K) {
					v.goTo(w);
					addStep("found");
					v.setColor(NodeColor.FOUND);
					result.put("node", w);
					break;
				} else if (w.getKey() < K) {
					if (w.getRight() == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(w.getRight());
					}
					addStep("bstfindright", K, w.getKey());
					pause();
					v.noArrow();
					w.setColor(NodeColor.DARKER);
					if (w.getLeft() != null) w.getLeft().subtreeColor(NodeColor.DARKER);
					w = w.getRight();
					if (w != null) {
						v.goAbove(w);
					} else { // not found
						addStep("notfound");
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
					pause();
					v.noArrow();
					w.setColor(NodeColor.DARKER);
					if (w.getRight() != null) w.getRight().subtreeColor(NodeColor.DARKER);
					w = w.getLeft();
					if (w != null) {
						v.goAbove(w);
					} else { // notfound
						addStep("notfound");
						v.setColor(NodeColor.NOTFOUND);
						v.goLeft();
						break;
					}
				}
				pause();
			}
		}
		if (result.get("node") == null) removeFromScene(v);
		pause();
		if (T.getRoot() != null) {
			T.getRoot().subtreeColor(NodeColor.NORMAL);
		}
		if (result.get("node") != null) removeFromScene(v);
	}

	public HashMap<String, Object> getResult() {
		return result;
	}
}
