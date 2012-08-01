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

import algvis.core.NodeColor;
import algvis.core.Node;

public class SkipDelete extends SkipAlg {
	public SkipDelete(SkipList L, int x) {
		super(L, x);
		v.setColor(NodeColor.DELETE);
		p = new SkipNode[L.height];
		setHeader("insertion");
	}

	@Override
	public void run() {
		addStep("bstdeletestart");
		SkipNode w = find();

		if (w.getRight().getKey() != K) {
			addStep("notfound");
			v.goDown();
			pause();
			return;
		}

		L.n--;
		L.e++;
		addStep("skipdelete");
		for (int i = 0; i < L.height; ++i) {
			if (p[i].getRight().getKey() != K) {
				break;
			}
			L.e--;
			L.setV(p[i].getRight());
			p[i].linkright(p[i].getRight().getRight());
			if (L.getV().getUp() != null) {
				L.getV().getUp().setDown(null);
			}
			L.getV().isolate();
			L.getV().goDown();
			pause();
			if (i > 0 && p[i].getKey() == -Node.INF
					&& p[i].getRight().getKey() == Node.INF) {
				L.setRoot(p[i].getDown());
				L.sent = p[i].getRight().getDown();
				L.getRoot().setUp(null);
				L.sent.setUp(null);
				L.height = i;
				break;
			}
		}

		addStep("done");
		L.reposition();
		pause();
		L.setV(null);
	}
}
