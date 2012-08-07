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
package algvis.ds.dictionaries.scapegoattree;

import algvis.core.NodeColor;
import algvis.ds.dictionaries.bst.BSTNode;

import java.util.HashMap;

public class GBFind extends GBAlg {
	public GBFind(GBTree T, int x) {
		super(T, x);
		v.setColor(NodeColor.FIND);
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		addToScene(v);
		setHeader("search");
		if (T.getRoot() == null) {
			v.goToRoot();
			addStep("empty");
			pause();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("notfound");
			removeFromScene(v);
		} else {
			BSTNode w = T.getRoot();
			v.goTo(w);
			addStep("bstfindstart");
			pause();
			while (true) {
				if (w.getKey() == K) {
					if (((GBNode) w).isDeleted()) {
						addStep("gbfinddeleted");
						v.setColor(NodeColor.NOTFOUND);
						v.goDown();
					} else {
						addStep("found");
						v.setColor(NodeColor.FOUND);
					}
					break;
				} else if (w.getKey() < K) {
					addStep("bstfindright", K, w.getKey());
					w = w.getRight();
					if (w != null) {
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
					if (w != null) {
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
			removeFromScene(v);
		}
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}
}
