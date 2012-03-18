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

import java.awt.Color;

import org.jdom.Element;

import algvis.core.DataStructure;
import algvis.scenario.Command;
import algvis.visual.Node;
import algvis.visual.View;

public class SkipNode extends Node {
	private SkipNode left = null;
	private SkipNode right = null;
	private SkipNode up = null;
	private SkipNode down = null;

	public SkipNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public SkipNode(DataStructure D, int key) {
		super(D, key);
	}

	public void linkleft(SkipNode v) {
		setLeft(v);
		v.setRight(this);
	}

	public void linkright(SkipNode v) {
		setRight(v);
		v.setLeft(this);
	}

	public void linkup(SkipNode v) {
		setUp(v);
		v.setDown(this);
	}

	public void linkdown(SkipNode v) {
		setDown(v);
		v.setUp(this);
	}

	public void isolate() {
		setLeft(setRight(setUp(setDown(null))));
	}

	@Override
	public void drawBg(View v) {
		v.setColor(getBgColor());
		v.fillSqr(x, y, Node.radius);
		v.setColor(Color.BLACK); // fgcolor);
		v.drawSqr(x, y, Node.radius);
		if (marked) {
			v.drawSqr(x, y, Node.radius + 2);
		}
	}

	public void drawSkipList(View V) {
		if (getLeft() == null && getDown() != null) {
			V.setColor(Color.black);
			V.drawLine(x, y, getDown().x, getDown().y);
			getDown().drawSkipList(V);
		}
		if (getRight() != null) {
			V.setColor(Color.black);
			V.drawArrow(x, y, getRight().x - Node.radius, getRight().y);
			getRight().drawSkipList(V);
		}
		draw(V);
	}

	public void moveSkipList() {
		if (getLeft() == null && getDown() != null) {
			getDown().moveSkipList();
		}
		if (getRight() != null) {
			getRight().moveSkipList();
		}
		move();
	}

	public void _reposition() {
		if (D.x2 < this.tox) {
			D.x2 = this.tox;
		}
		if (D.y2 < this.toy) {
			D.y2 = this.toy;
		}
		if (getLeft() == null) {
			if (getUp() == null) {
				goToRoot();
			} else {
				goTo(getUp().tox, getUp().toy + DataStructure.minsepy);
			}
			if (getDown() != null) {
				getDown()._reposition();
			}
		} else {
			if (getDown() == null) {
				goNextTo(getLeft());
			} else {
				goTo(getDown().tox, getLeft().toy);
			}
		}
		if (getRight() != null) {
			getRight()._reposition();
		}
	}

	public SkipNode find(int x, int y) {
		if (inside(x, y))
			return this;
		if (getLeft() == null && getDown() != null) {
			SkipNode tmp = getDown().find(x, y);
			if (tmp != null)
				return tmp;
		}
		if (getRight() != null) {
			return getRight().find(x, y);
		}
		return null;
	}

	public SkipNode getLeft() {
		return left;
	}

	public void setLeft(SkipNode left) {
		if (this.left != left) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetLeftCommand(left));
			}
			this.left = left;
		}
	}

	public SkipNode getRight() {
		return right;
	}

	public SkipNode setRight(SkipNode right) {
		if (this.right != right) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetRightCommand(right));
			}
			this.right = right;
		}
		return right;
	}

	public SkipNode getUp() {
		return up;
	}

	public SkipNode setUp(SkipNode up) {
		if (this.up != up) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetUpCommand(up));
			}
			this.up = up;
		}
		return up;
	}

	public SkipNode getDown() {
		return down;
	}

	public SkipNode setDown(SkipNode down) {
		if (this.down != down) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetDownCommand(down));
			}
			this.down = down;
		}
		return down;
	}
	
	private class SetDownCommand implements Command {
		private final SkipNode oldDown, newDown;

		public SetDownCommand(SkipNode newDown) {
			oldDown = getDown();
			this.newDown = newDown;
		}

		@Override
		public Element getXML() {
			Element e = new Element("setDown");
			e.setAttribute("key", Integer.toString(key));
			if (newDown != null) {
				e.setAttribute("newDown", Integer.toString(newDown.key));
			} else {
				e.setAttribute("newDown", "null");
			}
			if (oldDown != null) {
				e.setAttribute("oldDown", Integer.toString(oldDown.key));
			} else {
				e.setAttribute("oldDown", "null");
			}
			return e;
		}

		@Override
		public void execute() {
			setDown(newDown);
		}

		@Override
		public void unexecute() {
			setDown(oldDown);
		}
	}
	
	private class SetLeftCommand implements Command {
		private final SkipNode oldLeft, newLeft;

		public SetLeftCommand(SkipNode newLeft) {
			oldLeft = getLeft();
			this.newLeft = newLeft;
		}

		@Override
		public Element getXML() {
			Element e = new Element("setLeft");
			e.setAttribute("key", Integer.toString(key));
			if (newLeft != null) {
				e.setAttribute("newLeft", Integer.toString(newLeft.key));
			} else {
				e.setAttribute("newLeft", "null");
			}
			if (oldLeft != null) {
				e.setAttribute("oldLeft", Integer.toString(oldLeft.key));
			} else {
				e.setAttribute("oldLeft", "null");
			}
			return e;
		}

		@Override
		public void execute() {
			setLeft(newLeft);
		}

		@Override
		public void unexecute() {
			setLeft(oldLeft);
		}
	}

	private class SetRightCommand implements Command {
		private final SkipNode oldRight, newRight;

		public SetRightCommand(SkipNode newRight) {
			oldRight = getRight();
			this.newRight = newRight;
		}

		@Override
		public Element getXML() {
			Element e = new Element("setRight");
			e.setAttribute("key", Integer.toString(key));
			if (newRight != null) {
				e.setAttribute("newRight", Integer.toString(newRight.key));
			} else {
				e.setAttribute("newRight", "null");
			}
			if (oldRight != null) {
				e.setAttribute("oldRight", Integer.toString(oldRight.key));
			} else {
				e.setAttribute("oldRight", "null");
			}
			return e;
		}

		@Override
		public void execute() {
			setRight(newRight);
		}

		@Override
		public void unexecute() {
			setRight(oldRight);
		}
	}

	private class SetUpCommand implements Command {
		private final SkipNode oldUp, newUp;

		public SetUpCommand(SkipNode newUp) {
			oldUp = getUp();
			this.newUp = newUp;
		}

		@Override
		public Element getXML() {
			Element e = new Element("setUp");
			e.setAttribute("key", Integer.toString(key));
			if (newUp != null) {
				e.setAttribute("newUp", Integer.toString(newUp.key));
			} else {
				e.setAttribute("newUp", "null");
			}
			if (oldUp != null) {
				e.setAttribute("oldUp", Integer.toString(oldUp.key));
			} else {
				e.setAttribute("oldUp", "null");
			}
			return e;
		}

		@Override
		public void execute() {
			setUp(newUp);
		}

		@Override
		public void unexecute() {
			setUp(oldUp);
		}
	}
}
