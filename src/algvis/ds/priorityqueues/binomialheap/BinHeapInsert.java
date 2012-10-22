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
package algvis.ds.priorityqueues.binomialheap;

import algvis.core.visual.ZDepth;

import java.util.HashMap;

public class BinHeapInsert extends BinHeapAlg {
	private final int x;

	public BinHeapInsert(BinomialHeap H, int x) {
		super(H);
		this.x = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		int i = H.active;
		H.root[0] = H.min[0] = new BinHeapNode(H, x, ZDepth.ACTIONNODE);
		addToScene(H.root[0]); // TODO hm, naco je aj v datovej strukture
		if (H.root[i] != null) {
//			H.root[0].x = H.root[i].x;
			H.root[0].goTo(H.root[i].tox, H.root[0].toy);
		}
		H.reposition();
		pause();
		// meld
		if (H.root[i] == null) {
			H.root[i] = H.min[i] = H.root[0];
			removeFromScene(H.root[0]);
			H.root[0] = null;
			H.reposition();
			pause();
			return;
		}

		meld(i);
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}
}
