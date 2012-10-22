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
package algvis.ds.priorityqueues.leftistheap;

import java.util.HashMap;

public class LeftHeapDelete extends LeftHeapAlg {

	public LeftHeapDelete(LeftHeap H) {
		super(H);
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		int i = H.active;
		setHeader("deletion");
		
		if (H.root[i] == null) {
			return;
		}

		if (!H.minHeap) {
			addStep("maximum", H.root[i].getKey());
		} else {
			addStep("minimum", H.root[i].getKey());
		}

		pause();

		LeftHeapNode tmp = H.root[i];
		H.root[i] = tmp.getLeft();
		H.root[0] = tmp.getRight();
		tmp = null;

		if (H.root[i] == null) {
			H.root[i] = H.root[0];
			H.root[0] = null;
			if (H.root[i] != null) {
				H.root[i].highlightTree();
				H.root[i].repos(H.root[i].x, H.root[i].y
						- (LeftHeap.minsepy));// + 2 * LeftHeapNode.RADIUS));
			}
			// heap #1 is empty; done;
			return;
		}
		H.root[i].setParent(null);

		if (H.root[0] == null) {
			H.root[i].repos(H.root[i].x, H.root[i].y - (LeftHeap.minsepy));// + 2 * LeftHeapNode.RADIUS));
			// heap #2 is empty; done;
			return;
		}
		H.root[0].setParent(null);

		H.root[i].repos(H.root[i].x, H.root[i].y - (LeftHeap.minsepy));// + 2 * LeftHeapNode.RADIUS));
		H.root[0].repos(H.root[0].x, H.root[0].y - (LeftHeap.minsepy));// + 2 * H.RADIUS));

		pause();
		meld(i);
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}

}
