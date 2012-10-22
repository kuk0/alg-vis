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
package algvis.skiplist;

import algvis.core.MyRandom;
import algvis.core.Node;
import algvis.core.NodeColor;

public class SkipInsert extends SkipAlg {
	public SkipInsert(SkipList L, int x) { // Buttons B,
		super(L, x);
		v.setColor(NodeColor.INSERT);
		setHeader("insert", K);
	}

	@Override
	public void run() {
		addStep("skipinsertstart");
		SkipNode w = find();

		if (w.getKey() == v.getKey()) {
			addStep("alreadythere");
			v.goDown();
			mysuspend();
			addNote("done");
			return;
		}

		L.n++;
		addStep("skipinsertafter");
		mysuspend();
		SkipNode z, oldv = null;
		addNote("skiplist-tossing");
		int i = 0;
		do {
			if (i > 0) {
				addStep("skiplist-head", i);
				L.e++;
			}
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
				v.linkleft(L.setRoot(new SkipNode(L, -Node.INF)));
				v.linkright(L.sent = new SkipNode(L, Node.INF));
				L.getRoot().linkdown(oldr);
				L.sent.linkdown(olds);
				L.reposition();
				oldv = v;
				v = new SkipNode(L, v.getKey(), v.tox, -10);
				++i;
				++L.height;
			}
			mysuspend();
		} while (MyRandom.heads());

		addStep("skiplist-tail", i);
		mysuspend();
		addNote("done");

		L.getV().setColor(NodeColor.NORMAL);
		L.setV(null);
	}
}
