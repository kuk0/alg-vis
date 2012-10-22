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
package algvis.ds.unionfind;

import algvis.core.NodeColor;

public class UnionFindUnion extends UnionFindFind {
	public enum UnionHeuristic {
		NONE, BYRANK
	}

	private final UnionHeuristic unionState;
	private final UnionFind UF;
	private final UnionFindNode u;
    private final UnionFindNode v;

	public UnionFindUnion(UnionFind UF, UnionFindNode u, UnionFindNode v) {
		super(UF);
		this.UF = UF;
		this.unionState = UF.unionState;
		this.u = u;
		this.v = v;
	}
	
	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("ufunion");
		u.mark();
		v.mark();
		switch (unionState) {
			case NONE:
				unionSimple(u, v);
				break;
			case BYRANK:
				unionByRank(u, v);
				break;
			default:
				break;
		}
	}

	private void unionSimple(UnionFindNode V, UnionFindNode W) throws InterruptedException {
		UnionFindNode r1 = find(V);
		UnionFindNode r2 = find(W);
		if (r1 == r2) {
			addStep("ufsameset");
			pause();
		} else {
			addStep("ufunionsimple");
			pause();
			UF.removeFromSets(r2);
			r1.addChild(r2);
		}

		r1.unmark();
		r1.setColor(NodeColor.NORMAL);
		r2.unmark();
		r2.setColor(NodeColor.NORMAL);

		UF.reposition();
		addNote("done");
		// UF.panel.screen.V.resetView(); // only for testing, but still there
		// should
		// be some correction.
	}

	private void unionByRank(UnionFindNode V, UnionFindNode W) throws InterruptedException {
		UnionFindNode r1 = find(V);
		UnionFindNode r2 = find(W);
		if (r1 == r2) {
			addStep("ufsameset");
			pause();
		} else {
			addStep("ufunionbyrank", r1.getRank(), r2.getRank());
			if (r1.getRank() > r2.getRank()) {
				addStep("ufunionfirstsecond");
				pause();
				UF.removeFromSets(r2);
				r1.addChild(r2);
			} else if (r1.getRank() < r2.getRank()) {
				addStep("ufunionsecondfirst");
				pause();
				UF.removeFromSets(r1);
				r2.addChild(r1);
			} else {
				addStep("ufunionsamerank");
				pause();
				UF.removeFromSets(r2);
				r1.addChild(r2);
				r1.setRank(r1.getRank() + 1);
			}
		}

		r1.unmark();
		r1.setColor(NodeColor.NORMAL);
		r2.unmark();
		r2.setColor(NodeColor.NORMAL);

		UF.reposition();
		addNote("done");
		// UF.panel.screen.V.resetView(); // only for testing, but still there
		// should
		// be some correction.
	}
}
