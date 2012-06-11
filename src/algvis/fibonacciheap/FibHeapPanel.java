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
package algvis.fibonacciheap;

import algvis.core.DataStructure;
import algvis.core.MeldablePQButtons;
import algvis.core.Settings;
import algvis.core.VisPanel;

public class FibHeapPanel extends VisPanel {
	private static final long serialVersionUID = 2755087791754509441L;
	public static Class<? extends DataStructure> DS = FibonacciHeap.class;

	public FibHeapPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		FibonacciHeap H = new FibonacciHeap(this);
		D = H;
		B = new MeldablePQButtons(this);
		H.active = 1;
		D.random(7);
		H.active = 2;
		D.random(3);
		H.lowlight();
		H.active = 3;
		D.random(5);
		H.lowlight();
		H.active = 1;
		D.M.screen.V.resetView();
	}

}
