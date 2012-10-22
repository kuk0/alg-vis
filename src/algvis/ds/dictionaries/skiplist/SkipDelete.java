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
package algvis.ds.dictionaries.skiplist;

import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;

import java.util.HashMap;

public class SkipDelete extends SkipAlg {
	public SkipDelete(SkipList L, int x) {
		super(L, x);
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		v = new SkipNode(L, K, ZDepth.ACTIONNODE);
		v.setColor(NodeColor.DELETE);
		addToScene(v);
		p = new SkipNode[L.height];
		setHeader("insertion");
		addStep("bstdeletestart");
		SkipNode w = find();

		if (w.getKey() != K) {
			addStep("notfound");
			v.setColor(NodeColor.NOTFOUND);
			v.goDown();
			removeFromScene(v);
			return;
		}
		removeFromScene(v);

		addNote("skiplist-delete-found");
		L.n--;
		L.e++;
		addStep("skipdelete");
		for (int i = 0; i < L.height; ++i) {
			if (w == null || w.getKey() != K) {
				break;
			}
			L.e--;
			SkipNode left = w.getLeft(), right = w.getRight(), up = w.getUp();
			left.linkright(right);
			if (up != null) {
				up.setDown(null);
			}
			w.setColor(NodeColor.DELETE);
			addToScene(w);
			w.isolate();
			w.goDown();
			pause();
			removeFromScene(w);
			w = up;
			if (i > 0 && left.getKey() == -Node.INF
					&& right.getKey() == Node.INF) {
				L.setRoot(left.getDown());
				L.sent = right.getDown();
				L.getRoot().setUp(null);
				L.sent.setUp(null);
				L.height = i;
				break;
			}
		}

		addStep("done");
		L.reposition();
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}
}
