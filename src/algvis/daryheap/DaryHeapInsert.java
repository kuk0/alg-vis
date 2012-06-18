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


public class DaryHeapInsert extends DaryHeapAlg{
	DaryHeap T; //prepisat na H
	DaryHeapNode v;
	int K;

	public DaryHeapInsert(DaryHeap T, int x) {
		super(T);
		T.v = v = new DaryHeapNode(T, K = x);
		this.T = T;
		setHeader("insertion");
	}
	
	@Override
	public void run() {

		v.mark();
		if ( (H.root != null) && (H.root.nnodes == 1000) ) {
			addStep("heapfull");
			H.v = null;
			v.unmark();
			return;
		}		
		DaryHeapNode w;

		if (H.minHeap) {
			addStep("minheapbubbleup");
		} else {
			addStep("maxheapbubbleup");
		}
		
		//int n = H.root.nnodes - 1;
		if (H.root == null) {
			H.root = w = v;
			v.goToRoot();
			H.last = H.root;
			mysuspend();
		} else { //najdeme miesto pre v
			w = H.last.nextneighbour();
			w.linknewson(v);
			H.reposition();			
			mysuspend();
		}
		H.v = null;
		
		++H.root.nnodes;
		// mysuspend();
		v.unmark();
		bubbleup(v);
	}

}
