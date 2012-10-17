/*******************************************************************************
 * Copyright (c) 2012 Jakub Kov��, Katar�na Kotrlov�, Pavol Luk�a, Viktor Tomkovi�, Tatiana T�thov�
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
package algvis.daryheap;

import algvis.core.Node;


public class DaryHeapDelete extends DaryHeapAlg {
	public DaryHeapDelete(DaryHeap H) {
		super(H);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (H.root == null) {
			addStep("heapempty");
			H.last = null;
			return;
		}
		
		if (H.root.numChildren == 0) {
			H.v = H.root;
			if (H.minHeap){
				addStep("minimum", H.root.getKey());
			} else {
				addStep("maximum", H.root.getKey());
			}
			H.root = null;
			H.v.mark();
			//--H.n;
			mysuspend();
			H.v.unmark();
			H.v.goDown();
			return;
		}
		if (H.minHeap){
			addStep("minimum", H.root.getKey());
		} else {
			addStep("maximum", H.root.getKey());
		}
		H.root.mark();
		mysuspend();
		//H.root.unmark();
		addStep("heapchange");
		mysuspend();
		H.root.unmark();
		
		H.v = new DaryHeapNode(H.last);
		H.v2 = new DaryHeapNode(H.root);
		H.last.setKey(Node.NOKEY);
		H.root.setKey(Node.NOKEY);
		H.v.goToRoot();
		H.v2.goTo(H.last);
		H.v2.mark();
		mysuspend();
		H.last.setKey(H.v2.getKey());
		H.root.setKey(H.v.getKey());
		H.last.setColor(H.v2.getColor());
		H.root.setColor(H.v.getColor());
		H.v = null;
		H.v2 = null;
		
		H.v = H.last;
		H.last = H.last.prevneighbour();
		H.v.goDown();
		H.v.getParent().c[H.v.nson - 1] = null;
		H.v.getParent().numChildren--;
		H.root.mark();
		H.reposition();
		
		if (H.minHeap) {
			addStep("mindheapbubbledown");
		} else {
			addStep("maxdheapbubbledown");
		}
		mysuspend();
		H.v = null;
		
		v = H.root;
		H.root.unmark();
		bubbledown(v);
	}
}

