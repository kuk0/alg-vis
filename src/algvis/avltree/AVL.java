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
package algvis.avltree;

import algvis.bst.BST;
import algvis.bst.BSTFind;
import algvis.gui.VisPanel;
import algvis.gui.view.Layout;

public class AVL extends BST {
	public static String dsName = "avltree";

	@Override
	public String getName() {
		return "avltree";
	}

	public AVL(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new AVLInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new BSTFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new AVLDelete(this, x));
	}
	
	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}
}
