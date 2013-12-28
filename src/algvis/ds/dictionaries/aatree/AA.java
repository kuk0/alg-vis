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
package algvis.ds.dictionaries.aatree;

import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTFind;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.VisPanel;
import algvis.ui.view.Layout;
import algvis.ui.view.View;

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
		// TODO reposition pokazi historiu (je to vobec potrebne?)
		// reposition();
	}

	public void skew(BSTNode w) {
		if (w.getLeft() != null && w.getLeft().getLevel() == w.getLevel()) {
			w = w.getLeft();
			rotate(w);
			reposition();
		}
	}

	public BSTNode split(BSTNode w) {
		final BSTNode r = w.getRight();
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
			((AANode) getRoot()).drawTree2(V);
		}
	}

	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}
}
