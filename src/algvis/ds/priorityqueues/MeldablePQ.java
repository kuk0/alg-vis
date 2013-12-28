/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ds.priorityqueues;

import java.util.Hashtable;

import algvis.core.history.HashtableStoreSupport;
import algvis.ui.VisPanel;
import algvis.ui.view.Highlighting;

abstract public class MeldablePQ extends PriorityQueue implements Highlighting {
	public static String adtName = "meldable-pq";

	public static final int numHeaps = 10;
	public int active = 1;

	protected MeldablePQ(VisPanel M) {
		super(M);
	}

	abstract public void meld(int i, int j);

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "active", active);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		final Object active = state.get(hash + "active");
		if (active != null) {
			this.active = (Integer) HashtableStoreSupport.restore(active);
			final MeldablePQButtons buttons = ((MeldablePQButtons) panel.buttons);
			buttons.activeHeap.removeChangeListener(buttons);
			buttons.activeHeap.setValue(HashtableStoreSupport.restore(active));
			buttons.activeHeap.addChangeListener(buttons);
		}
	}
}
