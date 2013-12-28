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
package algvis.ds.dictionaries.scapegoattree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ui.VisPanel;

public class GBPanel extends VisPanel {
	private static final long serialVersionUID = 5223738995380219622L;
	public static Class<? extends DataStructure> DS = GBTree.class;

	public GBPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new GBTree(this);
		scene.add(D);
		buttons = new GBButtons(this);
	}

	@Override
	public void start() {
		super.start();
		D.random(25);
	}
}
