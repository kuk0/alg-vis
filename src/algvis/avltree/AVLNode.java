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

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.history.HashtableStoreSupport;
import algvis.gui.Fonts;
import algvis.gui.view.View;

import java.awt.*;
import java.util.Hashtable;

public class AVLNode extends BSTNode {
	private int bal = 0;

	public AVLNode(DataStructure D, int key, int zDepth) {
		super(D, key, zDepth);
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
		this.bal = bal;
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

		int xx = x - Node.RADIUS, yy = y - Node.RADIUS, dx = 2 * Node.RADIUS, dy = 2 * Node.RADIUS;
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
			V.drawOval(x - Node.RADIUS, y - Node.RADIUS, 2 * Node.RADIUS,
					2 * Node.RADIUS);
		}

		drawKey(V);
		if (getParent() != null && getParent().getLeft() == this) {
			V.drawString(b, x - Node.RADIUS - 1, y - Node.RADIUS - 1,
					Fonts.NORMAL);
		} else {
			V.drawString(b, x + Node.RADIUS + 1, y - Node.RADIUS - 1,
					Fonts.NORMAL);
		}
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "bal", bal);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object bal = state.get(hash + "bal");
		if (bal != null) this.bal = (Integer) HashtableStoreSupport.restore(bal);
	}
}
