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
package algvis.splaytree;

import algvis.bst.BST;
import algvis.gui.VisPanel;
import algvis.gui.view.Layout;
import algvis.gui.view.View;

public class SplayTree extends BST {
	public static String dsName = "splaytree";

	@Override
	public String getName() {
		return "splaytree";
	}

	public SplayTree(VisPanel M) {
		super(M);
		addNodes(4); // root2 (2), vv (3), w1 (4), w2 (5)
	}

	public SplayNode getRoot2() {
		return (SplayNode) getNode(2);
	}

	public void setRoot2(SplayNode root2) {
		setNode(2, root2, false);
	}

	public SplayNode getVV() {
		return (SplayNode) getNode(3);
	}

	public void setVV(SplayNode vv) {
		setNode(3, vv, true);
	}

	public SplayNode getW1() {
		return (SplayNode) getNode(4);
	}

	public void setW1(SplayNode w1) {
		setNode(4, w1, true);
	}

	public SplayNode getW2() {
		return (SplayNode) getNode(5);
	}

	public void setW2(SplayNode w2) {
		setNode(5, w2, false);
	}

	@Override
	public void insert(int x) {
		start(new SplayInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new SplayFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new SplayDelete(this, x));
	}

	@Override
	public void draw(View V) {
		if (getW1() != null && getW1().getParent() != null) {
			V.drawWideLine(getW1().x, getW1().y, getW1().getParent().x, getW1()
					.getParent().y);
		}
		if (getW2() != null && getW2().getParent() != null) {
			V.drawWideLine(getW2().x, getW2().y, getW2().getParent().x, getW2()
					.getParent().y);
		}

		if (getRoot() != null) {
			getRoot().moveTree();
			getRoot().drawTree(V);
		}
		if (getRoot2() != null) {
			getRoot2().moveTree();
			getRoot2().drawTree(V);
		}
		if (getV() != null) {
			getV().move();
			getV().draw(V);
		}
		if (getVV() != null) {
			getVV().move();
			getVV().draw(V);
		}
	}

	/*
	 * public String stats() { return super.stats()+";   Potential: "+ ((root ==
	 * null) ? "0" : ((SplayNode)root).pot); }
	 */

	public void rotate2(SplayNode v) {
		if (v.isLeft()) {
			rightrot(v);
		} else {
			leftrot(v);
		}
		v.reposition();
		if (v.getLeft() != null) {
			v.getLeft().calc();
		}
		if (v.getRight() != null) {
			v.getRight().calc();
		}
		v.calc();
	}

	@Override
	public void clear() {
		super.clear();
		setVV(null);
	}

	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}

}
