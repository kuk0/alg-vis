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
package algvis.unionfind;

import org.jdom.Element;

import algvis.core.DataStructure;
import algvis.core.TreeNode;
import algvis.core.View;
import algvis.scenario.commands.Command;

public class UnionFindNode extends TreeNode {
	private int rank = 0;
	private boolean grey = false;

	public UnionFindNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public UnionFindNode(DataStructure D, int key) {
		super(D, key);
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		if (this.rank != rank) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetRankCommand(rank));
			}
			this.rank = rank;
		}
	}

	@Override
	public UnionFindNode getChild() {
		return (UnionFindNode) super.getChild();
	}

	@Override
	public UnionFindNode getRight() {
		return (UnionFindNode) super.getRight();
	}

	@Override
	public UnionFindNode getParent() {
		return (UnionFindNode) super.getParent();
	}

	public boolean isGrey() {
		return grey;
	}

	public void setGrey(boolean grey) {
		if (grey == false) {
			UnionFindNode w = getChild();
			while (w != null) {
				w.setGrey(false);
				w = w.getRight();
			}
		}
		if (this.grey != grey) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetGreyCommand(grey));
			}
			this.grey = grey;
		}
	}

	public void drawGrey(View v) {
		TreeNode w = getChild();
		while (w != null) {
			((UnionFindNode) w).drawGrey(v);
			w = w.getRight();
		}
		if (isGrey() && getParent() != null) {
			v.drawWideLine(x, y, getParent().x, getParent().y, 10.0f);
		}
	}

	@Override
	public void drawTree(View v) {
		drawGrey(v);
		super.drawTree(v);
	}

	private class SetRankCommand implements Command {
		private final int oldRank, newRank;

		public SetRankCommand(int newRank) {
			oldRank = getRank();
			this.newRank = newRank;
		}

		@Override
		public Element getXML() {
			Element e = new Element("setRank");
			e.setAttribute("key", Integer.toString(key));
			e.setAttribute("oldRank", Integer.toString(oldRank));
			e.setAttribute("newRank", Integer.toString(newRank));
			return e;
		}

		@Override
		public void execute() {
			setRank(newRank);
		}

		@Override
		public void unexecute() {
			setRank(oldRank);
		}
	}

	private class SetGreyCommand implements Command {
		private final boolean oldGrey, newGrey;

		public SetGreyCommand(boolean newGrey) {
			oldGrey = isGrey();
			this.newGrey = newGrey;
		}

		@Override
		public Element getXML() {
			Element e = new Element("setGrey");
			e.setAttribute("key", Integer.toString(key));
			e.setAttribute("wasGrey", Boolean.toString(oldGrey));
			e.setAttribute("isGrey", Boolean.toString(newGrey));
			return e;
		}

		@Override
		public void execute() {
			setGrey(newGrey);
		}

		@Override
		public void unexecute() {
			setGrey(oldGrey);
		}
	}
}
