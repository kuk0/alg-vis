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
package algvis.ds.dictionaries.redblacktree;

import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.DataStructure;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.view.View;

import java.util.Hashtable;

public class RBNode extends BSTNode {
	private boolean red = true;

	public RBNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public RBNode(DataStructure D, int key, int zDepth) {
		super(D, key, zDepth);
	}

	public boolean isRed() {
		return red;
	}

	public void setRed(boolean red) {
		this.red = red;
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
		// TODO check
		setColor(isRed() ? NodeColor.RED : NodeColor.BLACK);
		super.draw(v);
	}

	void drawBigNodes(View v) {
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

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "red", red);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object red = state.get(hash + "red");
		if (red != null) this.red = (Boolean) HashtableStoreSupport.restore(red);
	}
}
