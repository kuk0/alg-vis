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
package algvis.btree;

import algvis.core.Dictionary;
import algvis.gui.VisPanel;
import algvis.gui.view.View;
import algvis.internationalization.Languages;

public class BTree extends Dictionary {
	public static String dsName = "btree";
	int order = 5;
	BNode root = null, v = null;
	final int xspan = 5;
    final int yspan = 15;

	@Override
	public String getName() {
		return "btree";
	}

	public BTree(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new BInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new BFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new BDelete(this, x));
	}

	@Override
	public void clear() {
		root = v = null;
		setStats();
	}

	@Override
	public String stats() {
		if (root == null) {
			return "#" + Languages.getString("nodes") + ": 0;   #"
					+ Languages.getString("keys") + ": 0 = 0% "
					+ Languages.getString("full") + ";   "
					+ Languages.getString("height") + ": 0";
		} else {
			root.calcTree();
			return "#" + Languages.getString("nodes") + ": " + root.nnodes + ";   "
					+ "#" + Languages.getString("keys") + ": " + root.nkeys + " = "
					+ (100 * root.nkeys) / (root.nnodes * (order - 1)) + "% "
					+ Languages.getString("full") + ";   "
					+ Languages.getString("height") + ": " + root.height;
		}
	}

	@Override
	public void draw(View V) {
		if (root != null) {
			root.moveTree();
			root.drawTree(V);
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
	}

	public void reposition() {
		if (root != null) {
			root._reposition();
			M.screen.V.setBounds(x1, y1, x2, y2);
		}
	}
}
