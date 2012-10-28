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

import algvis.ui.InputField;

public class PairHeapDecrKey extends PairHeapAlg {
	private final int delta;

	public PairHeapDecrKey(PairingHeap D, PairHeapNode v, int delta) {
		super(D);
		this.v = v;
		this.delta = delta;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		if (H.minHeap) {
			setHeader("decreasekey");
		} else {
			setHeader("increasekey");
		}

		if (H.minHeap) {
			v.setKey(v.getKey() - delta);
			if (v.getKey() < 1)
				v.setKey(1);
		} else {
			v.setKey(v.getKey() + delta);
			if (v.getKey() > InputField.MAX)
				v.setKey(InputField.MAX);
		}
		if (!v.isRoot()) {
			if (H.minHeap) {
				addStep("pairdecr"); // zvysili sme hodnotu, dieta odtrhneme a
				// prilinkujeme
			} else {
				addStep("pairincr");
			}
			H.root[0] = v;
			H.root[0].mark();
			pause();
			v.getParent().deleteChild(v);

			H.root[H.active].mark();
			H.reposition();
			pause();
			H.root[0].unmark();
			H.root[H.active].unmark();
			link(H.active, 0);
			H.root[0] = null;
			H.reposition();
		}

		addNote("done");
	}
}
