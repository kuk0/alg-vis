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

import algvis.ui.InputField;

public class LeftHeapDecrKey extends LeftHeapAlg {
	private final int delta;

	public LeftHeapDecrKey(LeftHeap H, LeftHeapNode v, int delta) {
		super(H);
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

		bubbleup(v);
	}
}
