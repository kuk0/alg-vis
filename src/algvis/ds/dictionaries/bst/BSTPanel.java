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
package algvis.ds.dictionaries.bst;

import algvis.core.Settings;
import algvis.ds.DataStructure;
import algvis.ui.VisPanel;
import algvis.ui.view.LayoutListener;

public class BSTPanel extends VisPanel {
	private static final long serialVersionUID = 2619694930536571557L;
	public static Class<? extends DataStructure> DS = BST.class;

	public BSTPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new BST(this);
		buttons = new BSTButtons(this);
		S.addLayoutListener((LayoutListener) D);
		D.random(20);
	}
}
