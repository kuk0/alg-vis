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
package algvis.ds.dictionaries.splaytree;

import algvis.core.Settings;
import algvis.core.DataStructure;
import algvis.ui.DictButtons;
import algvis.ui.VisPanel;

public class SplayPanel extends VisPanel {
	private static final long serialVersionUID = 7896254510404637883L;
	public static Class<? extends DataStructure> DS = SplayTree.class;

	public SplayPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new SplayTree(this);
		buttons = new DictButtons(this);
		D.random(20);
	}
}
