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
package algvis.heap;

import org.jdom2.Element;

import algvis.bst.BSTNode;
import algvis.core.Node;
import algvis.core.PriorityQueue;
import algvis.gui.VisPanel;
import algvis.gui.view.ClickListener;
import algvis.gui.view.View;
import algvis.internationalization.Languages;

public class Heap extends PriorityQueue implements ClickListener {
	public static final String dsName = "heap";
	private int n = 0;

	@Override
	public String getName() {
		return "heap";
	}

	public Heap(VisPanel M) {
		super(M);
		addNodes(3); // root (0), v (1), v2 (2)
		M.screen.V.setDS(this);
	}

	@Override
	public void insert(int x) {
		start(new HeapInsert(this, x));
	}

	@Override
	public void delete() {
		start(new HeapDelete(this));
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		if (v == null) {
			// TODO: vypindat
		} else
			start(new HeapDecrKey(this, (HeapNode) v, delta));
	}

	@Override
	public void clear() {
		setRoot(setV(setV2(null)));
		setN(0);
		setStats();
		// TODO asi nie
		panel.scenario.clear();
	}

	@Override
	public String stats() {
		if (getN() == 0) {
			return Languages.getString("size") + ": 0 ("
					+ Languages.getString("emptyheap") + ")";
		} else if (getN() == 1000) {
			return Languages.getString("size") + ": 1000 ("
					+ Languages.getString("fullheap") + ")";
		} else {
			return Languages.getString("size") + ": " + getN();
		}
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
		if (getV2() != null) {
			getV2().move();
			getV2().draw(V);
		}
	}

	public void reposition() {
		if (getRoot() != null) {
			getRoot().reposition();
			panel.screen.V.setBounds(x1, y1, x2, y2);
		}
	}

	@Override
	public void mouseClicked(int x, int y) {
		if (getRoot() == null)
			return;
		BSTNode v = getRoot().find(x, y);
		if (v != null) {
			if (v.marked) {
				v.unmark();
				chosen = null;
			} else {
				if (chosen != null)
					chosen.unmark();
				v.mark();
				chosen = v;
			}
		}
	}

	public HeapNode getRoot() {
		return (HeapNode) getNode(0);
	}

	public void setRoot(HeapNode root) {
		setNode(0, root, false);
	}

	public HeapNode getV() {
		return (HeapNode) getNode(1);
	}

	public HeapNode setV(HeapNode v) {
		setNode(1, v, true);
		return v;
	}

	public HeapNode getV2() {
		return (HeapNode) getNode(2);
	}

	public HeapNode setV2(HeapNode v2) {
		setNode(2, v2, true);
		return v2;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		if (this.n != n) {
			if (panel.scenario.isAddingEnabled()) {
				panel.scenario.add(new SetNCommand(n));
			}
			this.n = n;
		}
	}

	private class SetNCommand implements Command {
		private final int fromN, toN;

		public SetNCommand(int toN) {
			this.fromN = getN();
			this.toN = toN;
		}

		@Override
		public Element getXML() {
			Element e = new Element("setN");
			e.setAttribute("fromN", Integer.toString(fromN));
			e.setAttribute("toN", Integer.toString(toN));
			return e;
		}

		@Override
		public void execute() {
			setN(toN);
		}

		@Override
		public void unexecute() {
			setN(fromN);
		}
	}
}
