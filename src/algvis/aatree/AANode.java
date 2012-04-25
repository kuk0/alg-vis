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
package algvis.aatree;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Fonts;
import algvis.core.Node;
import algvis.core.View;

public class AANode extends BSTNode {
	public AANode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		setLevel(1);
	}

	public AANode(DataStructure D, int key) {
		this(D, key, 0, 0);
		getReady();
	}

	public AANode(BSTNode v) {
		this(v.D, v.getKey(), v.x, v.y);
	}

	@Override
	public AANode getLeft() {
		return (AANode) super.getLeft();
	}

	@Override
	public AANode getRight() {
		return (AANode) super.getRight();
	}

	@Override
	public AANode getParent() {
		return (AANode) super.getParent();
	}

	@Override
	public void draw(View v) {
		if (state == Node.INVISIBLE || getKey() == NULL) {
			return;
		}
		drawBg(v);
		drawKey(v);
		drawArrow(v);
		drawArc(v);
		String str = new String("" + getLevel());
		v.drawString(str, x + Node.radius, y - Node.radius, Fonts.SMALL);
	}

	public void drawBigNodes(View v) {
		if (getLeft() != null) {
			getLeft().drawBigNodes(v);
		}
		if (getRight() != null) {
			getRight().drawBigNodes(v);
		}
		if (getParent() != null && getParent().getLevel() == getLevel()) {
			v.drawWideLine(x, y, getParent().x, getParent().y);
		} else {
			v.drawWideLine(x - 1, y, x + 1, y);
		}
	}

	public void drawTree2(View v) {
		if (((AA) D).mode23)
			drawBigNodes(v);
		drawTree(v);
	}
}
