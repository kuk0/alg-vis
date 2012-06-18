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
package algvis.aatree;

import algvis.bst.BST;
import algvis.bst.BSTFind;
import algvis.bst.BSTNode;
import algvis.gui.VisPanel;
import algvis.gui.view.Layout;
import algvis.gui.view.View;

public class AA extends BST {
	public static String dsName = "aatree";
	public boolean mode23 = false;

	@Override
	public String getName() {
		return "aatree";
	}

	public AA(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new AAInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new BSTFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new AADelete(this, x));
	}

	public void setMode23(boolean set) {
		mode23 = set;
		scenario.newStep();
		reposition();
	}

	public boolean getMode23() {
		return mode23;
	}

	public BSTNode skew(BSTNode w) {
		if (w.getLeft() != null && w.getLeft().getLevel() == w.getLevel()) {
			w = w.getLeft();
			rotate(w);
			reposition();
		}
		return w;
	}

	public BSTNode split(BSTNode w) {
		BSTNode r = w.getRight();
		if (r != null && r.getRight() != null
				&& r.getRight().getLevel() == w.getLevel()) {
			w = r;
			rotate(w);
			w.setLevel(w.getLevel() + 1);
			reposition();
		}
		return w;
	}

	@Override
	public void draw(View V) {
		if (getRoot() != null) {
			getRoot().moveTree();
			((AANode) getRoot()).drawTree2(V);
		}
		if (getV() != null) {
			getV().move();
			getV().draw(V);
		}
	}
	
	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}
}
