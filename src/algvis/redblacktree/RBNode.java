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
package algvis.redblacktree;

import org.jdom.Element;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.gui.view.View;
import algvis.scenario.Command;

public class RBNode extends BSTNode {
	private boolean red = true;

	public RBNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public RBNode(DataStructure D, int key) {
		super(D, key);
	}

	public boolean isRed() {
		return red;
	}

	public void setRed(boolean red) {
		if (this.red != red) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetRedCommand(red));
			}
			this.red = red;
		}
	}

	@Override
	public RBNode getLeft() {
		return (RBNode) super.getLeft();
	}

	public RBNode getLeft2() {
		RBNode r = getLeft();
		if (r == null)
			return ((RB) D).NULL;
		else
			return r;
	}

	@Override
	public RBNode getRight() {
		return (RBNode) super.getRight();
	}

	public RBNode getRight2() {
		RBNode r = getRight();
		if (r == null)
			return ((RB) D).NULL;
		else
			return r;
	}

	@Override
	public RBNode getParent() {
		return (RBNode) super.getParent();
	}

	public RBNode getParent2() {
		RBNode p = getParent();
		if (p == null)
			return ((RB) D).NULL;
		else
			return p;
	}

	@Override
	public void draw(View v) {
		if (state == Node.INVISIBLE || getKey() == NULL) {
			return;
		}
		boolean a = D.scenario.isAddingEnabled();
		D.scenario.enableAdding(false);
		setColor(isRed() ? NodeColor.RED : NodeColor.BLACK);
		D.scenario.enableAdding(a);
		super.draw(v);
	}

	public void drawBigNodes(View v) {
		if (getKey() == NULL) {
			return;
		}
		if (getLeft() != null) {
			getLeft().drawBigNodes(v);
		}
		if (getRight() != null) {
			getRight().drawBigNodes(v);
		}
		if (isRed() && getParent() != null) {
			v.drawWideLine(x, y, getParent().x, getParent().y);
		} else {
			v.drawWideLine(x - 1, y, x + 1, y);
		}
	}

	public void drawRBTree(View v) {
		if (((RB) D).mode24) {
			drawBigNodes(v);
		}
		drawTree(v);
	}

	private class SetRedCommand implements Command {
		private final boolean oldRed, newRed;

		public SetRedCommand(boolean newRed) {
			oldRed = isRed();
			this.newRed = newRed;
		}

		@Override
		public Element getXML() {
			Element e = new Element("setRed");
			e.setAttribute("key", Integer.toString(getKey()));
			e.setAttribute("wasRed", Boolean.toString(oldRed));
			e.setAttribute("isRed", Boolean.toString(newRed));
			return e;
		}

		@Override
		public void execute() {
			setRed(newRed);
		}

		@Override
		public void unexecute() {
			setRed(oldRed);
		}
	}
}
