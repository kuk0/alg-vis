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
package algvis.avltree;

import java.awt.Color;

import org.jdom.Element;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.gui.Fonts;
import algvis.gui.view.View;
import algvis.scenario.Command;

//import static java.lang.Math.random;
//import static java.lang.Math.round;

public class AVLNode extends BSTNode {
	private int bal = 0;

	private AVLNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public AVLNode(DataStructure D, int key) {
		this(D, key, 0, 0);
		getReady();
	}

	public AVLNode(BSTNode v) {
		this(v.D, v.getKey(), v.x, v.y);
	}

	@Override
	public AVLNode getLeft() {
		return (AVLNode) super.getLeft();
	}

	@Override
	public AVLNode getRight() {
		return (AVLNode) super.getRight();
	}

	@Override
	public AVLNode getParent() {
		return (AVLNode) super.getParent();
	}

	public int balance() {
		int l = (getLeft() == null) ? 0 : getLeft().height, r = (getRight() == null) ? 0
				: getRight().height;
		setBalance(r - l);
		return bal;
	}

	void setBalance(int bal) {
		if (this.bal != bal) {
			D.M.scenario.add(new SetBalanceCommand(bal));
			this.bal = bal;
		}
	}

	int getBalance() {
		return bal;
	}

	@Override
	public void calc() {
		super.calc();
		balance();
	}

	@Override
	public void draw(View V) {
		if (state == Node.INVISIBLE || getKey() == NULL) {
			return;
		}
		drawBg(V);
		drawArrow(V);
		drawArc(V);

		int xx = x - Node.radius, yy = y - Node.radius, dx = 2 * Node.radius, dy = 2 * Node.radius;
		String b = "";
		if (getBgColor() == NodeColor.NORMAL.bgColor) {
			V.setColor(Color.ORANGE);
			switch (bal) {
			case +2:
				b = "++";
				V.fillArc(xx, yy, dx, dy, 270, 180);
				break;
			case +1:
				b = "+";
				V.fillArc(xx, yy, dx, dy, 210, 180);
				break;
			case 0:
				b = "\u00b7";
				V.fillArc(xx, yy, dx, dy, 180, 180);
				break;
			case -1:
				b = "\u2013";
				V.fillArc(xx, yy, dx, dy, 150, 180);
				break;
			case -2:
				b = "\u2013\u2013";
				V.fillArc(xx, yy, dx, dy, 90, 180);
				break;
			}
			V.setColor(getFgColor());
			V.drawOval(x - Node.radius, y - Node.radius, 2 * Node.radius,
					2 * Node.radius);
		}

		drawKey(V);
		if (getParent() != null && getParent().getLeft() == this) {
			V.drawString(b, x - Node.radius - 1, y - Node.radius - 1,
					Fonts.NORMAL);
		} else {
			V.drawString(b, x + Node.radius + 1, y - Node.radius - 1,
					Fonts.NORMAL);
		}
	}

	private class SetBalanceCommand implements Command {
		private final int fromBal, toBal;

		public SetBalanceCommand(int bal) {
			this.fromBal = getBalance();
			this.toBal = bal;
		}

		@Override
		public Element getXML() {
			Element e = new Element("setBalance");
			e.setAttribute("key", Integer.toString(getKey()));
			e.setAttribute("fromBalance", Integer.toString(fromBal));
			e.setAttribute("toBalance", Integer.toString(toBal));
			return e;
		}

		@Override
		public void execute() {
			setBalance(toBal);
		}

		@Override
		public void unexecute() {
			setBalance(fromBal);
		}
	}
}
