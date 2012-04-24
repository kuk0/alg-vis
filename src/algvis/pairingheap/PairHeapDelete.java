/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováè, Katarína Kotrlová, Pavol Lukèa, Viktor Tomkoviè, Tatiana Tóthová
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
package algvis.pairingheap;

import algvis.core.DataStructure;

public class PairHeapDelete extends PairHeapAlg{

	int i;
	public Pairing pairState;
	
	public PairHeapDelete(DataStructure H, int i) {
		super(H);
		this.i = i;
		setHeader("deletion");
		this.pairState = this.H.pairState;
	}
	
	public void setState(Pairing state) {
		this.pairState = state;
	}
	
	@Override
	public void run() {
		if (H.root[i] == null) {
			return;
		}

		if (!H.minHeap) {
			addStep("maximum", H.root[i].key);
		} else {
			addStep("minimum", H.root[i].key);
		}

		H.v = new PairHeapNode(H.root[i]);
		H.v.mark();
		
		mysuspend();
		
		//spravime neviditelneho roota (vymazane minimum) a posunieme to o minsepy hore.
		
		H.v.goDown();

		H.root[i].state = -1; //<<----- potom odkomentovat
		H.root[i].shift(0, - PairingHeap.minsepy);
		
		switch (pairState) {
		case NAIVE:
			pairNaive(i);
			break;
		case LRRL:
			pairLRRL(i);
			break;
		/*
		case FB:
			break;
		case BF:
			break;
		case MULTI:
			break;
		case LAZYM:
			break;
		*/
		default:
			break;
		}
	}
}
