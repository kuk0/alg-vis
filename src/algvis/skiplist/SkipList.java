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
package algvis.skiplist;

import algvis.core.Dictionary;
import algvis.core.Node;
import algvis.gui.VisPanel;
import algvis.gui.view.Alignment;
import algvis.gui.view.ClickListener;
import algvis.gui.view.View;

public class SkipList extends Dictionary implements ClickListener {
	public static String dsName = "skiplist";
	SkipNode sent;
	int height = 1, n = 0, e = 0;

	@Override
	public String getName() {
		return "skiplist";
	}

	public SkipList(VisPanel M) {
		super(M);
		M.screen.V.setDS(this);
		M.screen.V.align = Alignment.LEFT;
		setRoot(new SkipNode(this, -Node.INF));
		getRoot().linkright(sent = new SkipNode(this, Node.INF));
		reposition();
	}

	@Override
	public void insert(int x) {
		start(new SkipInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new SkipFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new SkipDelete(this, x));
	}

	@Override
	public void clear() {
		// TODO asi nezmazat historiu
		if (n != 0 || M.scenario.hasNext()) {
			M.scenario.newAlgorithm();
			M.scenario.newStep();
			height = 1;
			n = e = 0;
			setRoot(new SkipNode(this, -Node.INF));
			getRoot().linkright(sent = new SkipNode(this, Node.INF));
			setV(null);
			M.C.clear();
			setStats();
			reposition();
			// M.screen.V.resetView(); TODO toto bolo v BST.clear()
		}
	}

	@Override
	public String stats() {
		if (getRoot() == null) {
			return M.S.L.getString("size") + ": 0;   "
					+ M.S.L.getString("height") + ": 0;   #"
					+ M.S.L.getString("excess") + ": 0";
		} else {
			return M.S.L.getString("size") + ": " + n + ";   "
					+ M.S.L.getString("height") + ": " + height + ";   #"
					+ M.S.L.getString("excess") + ": " + e;
		}
	}

	@Override
	public void draw(View V) {
		if (getRoot() != null) {
			getRoot().moveSkipList();
			getRoot().drawSkipList(V);
		}
		if (getV() != null) {
			getV().move();
			getV().draw(V);
		}
	}

	public void reposition() {
		x1 = 0;
		y1 = 0;
		getRoot()._reposition();
		M.screen.V.setBounds(x1, y1, x2, y2);
	}

	public void mouseClicked(int x, int y) {
		if (getRoot() != null) {
			Node w = getRoot().find(x, y);
			if (w != null) {
				M.B.I.setText("" + w.getKey());
			}
		}
	}

	public SkipNode getRoot() {
		return (SkipNode) super.getRoot();
	}

	public SkipNode setRoot(SkipNode root) {
		super.setRoot(root);
		return root;
	}

	public SkipNode getV() {
		return (SkipNode) super.getV();
	}

	public void setV(SkipNode v) {
		super.setV(v);
	}
}
