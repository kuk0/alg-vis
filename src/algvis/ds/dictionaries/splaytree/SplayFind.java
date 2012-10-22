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
package algvis.ds.dictionaries.splaytree;

import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;

import java.util.HashMap;

public class SplayFind extends SplayAlg {
	
	public SplayFind(SplayTree T, int x) {
		super(T, x);
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		SplayNode v = new SplayNode(T, K, ZDepth.ACTIONNODE);
		v.setColor(NodeColor.FIND);
		addToScene(v);
		setHeader("find", K);
		if (T.getRoot() == null) {
			v.goToRoot();
			addStep("bstfindempty");
			pause();
			v.goDown();
			v.setColor(NodeColor.NOTFOUND);
			addStep("bstfindnotfound");
		} else {
			v.goAboveRoot();
			SplayNode w = find(K);
			splay(w);

			addStep("splayinroot");
			pause();

			w.setColor(NodeColor.NORMAL);
			v.goToRoot();
			if (w.getKey() == v.getKey()) {
				addStep("found");
				v.setColor(NodeColor.FOUND);
			} else {
				addStep("notfound");
				v.setColor(NodeColor.NOTFOUND);
				v.goDown();
			}
			pause();
		}
		removeFromScene(v);
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}
}
