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
package algvis.ds.dictionaries.scapegoattree;

import algvis.core.StringUtils;
import algvis.ds.dictionaries.bst.BST;
import algvis.ui.VisPanel;
import algvis.ui.view.Layout;
import algvis.internationalization.Languages;

import java.util.Hashtable;

public class GBTree extends BST {
	public static String dsName = "scapegoat";
	double alpha = 1.01; // TODO mozno nebude fungovat prepinanie alphy pri prehliadani historie
	private int del = 0;

	@Override
	public String getName() {
		return "scapegoat";
	}

	public GBTree(VisPanel M) {
		super(M);
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	@Override
	public void insert(int x) {
		start(new GBInsert(this, x));
	}

	@Override
	public void clear() {
		super.clear();
		setDel(0);
	}

	@Override
	public void find(int x) {
		start(new GBFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new GBDelete(this, x));
	}

	@Override
	public String stats() {
		if (getRoot() == null) {
			return "#" + Languages.getString("nodes") + ": 0;   #"
					+ Languages.getString("deleted") + ": 0;   "
					+ Languages.getString("height") + ": 0 =  1.00\u00b7"
					+ Languages.getString("opt") + ";   "
					+ Languages.getString("avedepth") + ": 0";
		} else {
			getRoot().calcTree();
			return "#"
					+ Languages.getString("nodes")
					+ ": "
					+ getRoot().size
					+ ";   #"
					+ Languages.getString("deleted")
					+ ": "
					+ getDel()
					+ ";   "
					+ Languages.getString("height")
					+ ": "
					+ getRoot().height
					+ " = "
					+ StringUtils
							.format(getRoot().height
									/ (Math.floor(lg(getRoot().size - getDel())) + 1),
									2, 5)
					+ "\u00b7"
					+ Languages.getString("opt")
					+ ";   "
					+ Languages.getString("avedepth")
					+ ": "
					+ StringUtils.format(getRoot().sumh
							/ (double) getRoot().size, 2, -5);
		}
	}

	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		state.put(hash + "del", del);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Integer del = (Integer) state.get(hash + "del");
		if (del != null) this.del = del;
	}
}
