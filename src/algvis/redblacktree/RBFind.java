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
package algvis.redblacktree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class RBFind extends Algorithm {
	private final RB T;
	private final BSTNode v;
	private final int K;

	public RBFind(RB T, int x) {
		super(panel, d);
		this.T = T;
		v = T.setV(new BSTNode(T, K = x));
		v.setColor(NodeColor.FIND);
		setHeader("search");
	}

	@Override
	public void run() {
		if (T.getRoot() == T.NULL) {
			v.goToRoot();
			addStep("empty");
			pause();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
		} else {
			BSTNode w = T.getRoot();
			v.goTo(w);
			addStep("bstfindstart");
			pause();
			while (true) {
				if (w.getKey() == K) {
					addStep("found");
					v.setColor(NodeColor.FOUND);
					break;
				} else if (w.getKey() < K) {
					addStep("bstfindright", K, w.getKey());
					w = w.getRight();
					if (w != T.NULL) {
						v.goTo(w);
					} else { // notfound
						addStep("notfound");
						v.setColor(NodeColor.NOTFOUND);
						v.goRight();
						break;
					}
				} else {
					addStep("bstfindleft", K, w.getKey());
					w = w.getLeft();
					if (w != T.NULL) {
						v.goTo(w);
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
	}
}
