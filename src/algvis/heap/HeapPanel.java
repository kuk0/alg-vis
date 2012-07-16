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
package algvis.heap;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.gui.PQButtons;
import algvis.gui.VisPanel;

public class HeapPanel extends VisPanel {
	private static final long serialVersionUID = -2488725769022251713L;
	public static Class<? extends DataStructure> DS = Heap.class;

	public HeapPanel(Settings S, boolean isScenarioEnabled) {
		super(S, isScenarioEnabled);
	}

	@Override
	public void initDS() {
		D = new Heap(this);
		B = new PQButtons(this);
		D.random(20);
	}
}
