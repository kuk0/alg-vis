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
package algvis.ds.priorityqueues.pairingheap;

import algvis.core.DataStructure;

public class PairHeapDelete extends PairHeapAlg {

	public PairHeapDelete(DataStructure H) {
		super(H);
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		final Pairing pairState = this.H.pairState;
		final int i = H.active;
		setHeader("deletion");

		if (H.root[i] == null) {
			return;
		}

		if (!H.minHeap) {
			addStep("maximum", H.root[i].getKey());
		} else {
			addStep("minimum", H.root[i].getKey());
		}

		final PairHeapNode v = new PairHeapNode(H.root[i]);
		v.mark();
		addToScene(v);

		pause();

		// spravime neviditelneho roota (vymazane minimum) a posunieme to o
		// minsepy hore.

		v.goDown();
		removeFromScene(v);

		H.root[i].state = -1; // <<----- potom odkomentovat
		H.root[i].shift(0, -DataStructure.minsepy);

		switch (pairState) {
		case NAIVE:
			pairNaive(i);
			break;
		case LRRL:
			pairLRRL(i);
			break;
		/*
		 * case FB: break; case BF: break; case MULTI: break; case LAZYM: break;
		 */
		default:
			break;
		}
	}
}
