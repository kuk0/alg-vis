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
package algvis.aatree;

import algvis.bst.BSTInsert;
import algvis.core.Algorithm;
import algvis.core.visual.ZDepth;

import java.util.HashMap;

public class AAInsert extends Algorithm {
	private final AA T;
	private final int K;

	public AAInsert(AA T, int x) {
		super(T.panel);
		this.T = T;
		K = x;
	}

	// TODO problem pri prechadzani historie spat - vykresli sa hrana nad vrcholom
	@Override
	public void runAlgorithm() throws InterruptedException {
		BSTInsert insert = new BSTInsert(T, new AANode(T, K, ZDepth.ACTIONNODE), this);
		insert.runAlgorithm();
		HashMap<String, Object> insertResult = insert.getResult();
		boolean inserted = (Boolean) insertResult.get("inserted");
		AANode w = (AANode) insertResult.get("w");
		
		if (inserted && w != null) {
			pause();
				
			// bubleme nahor
			while (w != null) {
				w.mark();
				addStep("aaok");
				// skew
				if (w.getLeft() != null
						&& w.getLeft().getLevel() == w.getLevel()) {
					addStep("aaskew");
					pause();
					w.unmark();
					w = w.getLeft();
					w.mark();
					w.setArc();
					pause();
					w.noArc();
					T.rotate(w);
					T.reposition();
				}
				// split
				AANode r = w.getRight();
				if (r != null && r.getRight() != null
						&& r.getRight().getLevel() == w.getLevel()) {
					addStep("aasplit");
					w.unmark();
					w = r;
					w.mark();
					w.setArc();
					pause();
					w.noArc();
					T.rotate(w);
					w.setLevel(w.getLevel() + 1);
					T.reposition();
				}
				pause();
				w.unmark();
				w = w.getParent();
			}
			T.reposition();
			addStep("done");
		}
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}
}
