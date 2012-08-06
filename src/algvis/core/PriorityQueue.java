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
package algvis.core;

import algvis.core.history.HashtableStoreSupport;
import algvis.gui.VisPanel;

import java.util.Hashtable;

abstract public class PriorityQueue extends DataStructure {
	public static String adtName = "pq";
	public boolean minHeap = false;

	protected PriorityQueue(VisPanel M) {
		super(M);
	}

	@Override
	abstract public void insert(int x);

	abstract public void delete();

	abstract public void decreaseKey(Node v, int delta);

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "minHeap", minHeap);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object minHeap = state.get(hash + "minHeap");
		if (minHeap != null) this.minHeap = (Boolean) HashtableStoreSupport.restore(minHeap);
	}
}
