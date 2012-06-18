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

import algvis.gui.VisPanel;
import algvis.gui.view.Highlighting;

abstract public class MeldablePQ extends DataStructure implements Highlighting{
	public static String adtName = "meldable-pq";

	public static final int numHeaps = 10;
	public boolean minHeap = false;
	public int active = 1;

	public MeldablePQ(VisPanel M, String dsName) {
		super(M);
	}

	public static String adtName() {
		return "meldable-pq";
	}

	@Override
	abstract public void insert(int x);

	abstract public void delete();

	abstract public void meld(int i, int j);

	abstract public void decreaseKey(Node v, int delta);
}
