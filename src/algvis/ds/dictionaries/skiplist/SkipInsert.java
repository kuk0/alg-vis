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

import algvis.core.MyRandom;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;

public class SkipInsert extends SkipAlg {
	
	public SkipInsert(SkipList L, int x) {
		super(L, x);
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		p = new SkipNode[L.height];
		v = new SkipNode(L, K, ZDepth.ACTIONNODE);
		v.setColor(NodeColor.INSERT);
		addToScene(v);
		setHeader("insert", K);
		addStep("skipinsertstart");
		SkipNode w = find();

		if (w.getKey() == v.getKey()) {
			addStep("alreadythere");
			v.setColor(NodeColor.NOTFOUND);
			v.goDown();
			removeFromScene(v);
			pause();
			addNote("done");
			return;
		}
		SkipNode inserted = v;

		L.n++;
		addStep("skipinsertafter");
		pause();
		SkipNode z, oldv = null;
		addNote("skiplist-tossing");
		int i = 0;
		do {
			if (i > 0) {
				addStep("skiplist-head", i);
				L.e++;
			}
			addToScene(v);
			if (i < L.height) {
				w = p[i++];
				z = w.getRight();
				w.linkright(v);
				z.linkleft(v);
				if (oldv != null) {
					v.linkdown(oldv);
				}
				L.reposition();
				oldv = v;
				v = new SkipNode(L, v.getKey(), v.tox, -10);
			} else {
				v.linkdown(oldv);
				SkipNode oldr = L.getRoot(), olds = L.sent;
				v.linkleft(L.setRoot(new SkipNode(L, -Node.INF, L.getZDepth())));
				v.linkright(L.sent = new SkipNode(L, Node.INF, L.getZDepth()));
				L.getRoot().linkdown(oldr);
				L.sent.linkdown(olds);
				L.reposition();
				oldv = v;
				v = new SkipNode(L, v.getKey(), v.tox, -10);
				++i;
				++L.height;
			}
			removeFromScene(oldv);
			pause();
		} while (MyRandom.heads());

		addStep("skiplist-tail", i);
		pause();
		addNote("done");

		inserted.setColor(NodeColor.NORMAL);
	}
}
