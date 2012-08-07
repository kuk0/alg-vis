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
package algvis.ds.priorityqueues.daryheap;

import algvis.gui.InputField;

import java.util.HashMap;

public class DaryHeapDecrKey extends DaryHeapAlg{
	private final int delta;
	private final DaryHeapNode v;

	public DaryHeapDecrKey(DaryHeap H, DaryHeapNode v, int delta) {
		super(H);
		this.v = v;
		this.delta = delta;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		if (H.minHeap) {
			setHeader("decreasekey");
			addStep("decrkeymin");
		} else {
			setHeader("increasekey");
			addStep("incrkeymax");
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

		if (H.minHeap) {
			addStep("minheapbubbleup");
		} else {
			addStep("maxheapbubbleup");
		}
		bubbleup(v);
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}
}
