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

package algvis.leftistheap;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.gui.MeldablePQButtonsNoDecr;
import algvis.gui.VisPanel;

public class LeftHeapPanel extends VisPanel {
	private static final long serialVersionUID = -6885107230514971633L;
	public static Class<? extends DataStructure> DS = LeftHeap.class;

	public LeftHeapPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		LeftHeap L = new LeftHeap(this);
		D = L;
		B = new MeldablePQButtonsNoDecr(this);
		L.active = 1;
		D.random(13);
		L.active = 2;
		D.random(10);
		L.lowlight();
		L.active = 3;
		D.random(7);
		L.lowlight();
		L.active = 1;
		D.M.screen.V.resetView();
	}

}
