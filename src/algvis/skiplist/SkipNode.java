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

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.gui.view.View;

import java.awt.*;
import java.util.Hashtable;

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
		v.fillSqr(x, y, Node.RADIUS);
		v.setColor(Color.BLACK); // fgcolor);
		v.drawSqr(x, y, Node.RADIUS);
		if (marked) {
			v.drawSqr(x, y, Node.RADIUS + 2);
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
			V.drawArrow(x, y, getRight().x - Node.RADIUS, getRight().y);
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

	SkipNode getLeft() {
		return left;
	}

	void setLeft(SkipNode left) {
		this.left = left;
	}

	public SkipNode getRight() {
		return right;
	}

	SkipNode setRight(SkipNode right) {
		return this.right = right;
	}

	public SkipNode getUp() {
		return up;
	}

	public SkipNode setUp(SkipNode up) {
		return this.up = up;
	}

	public SkipNode getDown() {
		return down;
	}

	public SkipNode setDown(SkipNode down) {
		return this.down = down;
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		state.put(hash + "down", down);
		state.put(hash + "left", left);
		state.put(hash + "right", right);
		state.put(hash + "up", up);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		SkipNode down = (SkipNode) state.get(hash + "down");
		if (down != null) this.down = down;
		SkipNode left = (SkipNode) state.get(hash + "left");
		if (left != null) this.left = left;
		SkipNode right = (SkipNode) state.get(hash + "right");
		if (right != null) this.right = right;
		SkipNode up = (SkipNode) state.get(hash + "up");
		if (up != null) this.up = up;
	}
}
