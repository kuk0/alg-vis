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
package algvis.scapegoattree;

import org.jdom2.Element;

import algvis.bst.BST;
import algvis.core.StringUtils;
import algvis.gui.VisPanel;
import algvis.gui.view.Layout;
import algvis.gui.view.View;
import algvis.scenario.Command;

public class GBTree extends BST {
	public static String dsName = "scapegoat";
	double alpha = 1.01;
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
		if (this.del != del) {
			if (M.scenario.isAddingEnabled()) {
				M.scenario.add(new SetDelCommand(del));
			}
			this.del = del;
		}
	}

	@Override
	public void insert(int x) {
		start(new GBInsert(this, x));
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
	public void clear() {
		super.clear();
		setDel(0);
	}

	@Override
	public void draw(View V) {
		if (getRoot() != null) {
			getRoot().moveTree();
			getRoot().drawTree(V);
		}
		if (getV() != null) {
			getV().move();
			getV().draw(V);
		}
	}

	@Override
	public String stats() {
		if (getRoot() == null) {
			return "#" + M.S.L.getString("nodes") + ": 0;   #"
					+ M.S.L.getString("deleted") + ": 0;   "
					+ M.S.L.getString("height") + ": 0 =  1.00\u00b7"
					+ M.S.L.getString("opt") + ";   "
					+ M.S.L.getString("avedepth") + ": 0";
		} else {
			getRoot().calcTree();
			return "#"
					+ M.S.L.getString("nodes")
					+ ": "
					+ getRoot().size
					+ ";   #"
					+ M.S.L.getString("deleted")
					+ ": "
					+ getDel()
					+ ";   "
					+ M.S.L.getString("height")
					+ ": "
					+ getRoot().height
					+ " = "
					+ StringUtils
							.format(getRoot().height
									/ (Math.floor(lg(getRoot().size - getDel())) + 1),
									2, 5)
					+ "\u00b7"
					+ M.S.L.getString("opt")
					+ ";   "
					+ M.S.L.getString("avedepth")
					+ ": "
					+ StringUtils.format(getRoot().sumh
							/ (double) getRoot().size, 2, -5);
		}
	}

	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}

	private class SetDelCommand implements Command {
		private final int oldDel, newDel;

		public SetDelCommand(int newDel) {
			oldDel = getDel();
			this.newDel = newDel;
		}

		@Override
		public Element getXML() {
			Element e = new Element("setDel");
			e.setAttribute("GBTree", Integer.toString(hashCode()));
			e.setAttribute("oldDel", Integer.toString(oldDel));
			e.setAttribute("newDel", Integer.toString(newDel));
			return e;
		}

		@Override
		public void execute() {
			setDel(newDel);
		}

		@Override
		public void unexecute() {
			setDel(oldDel);
		}
	}
}
