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
package algvis.ds.dictionaries.splaytree;

import java.util.Hashtable;

import algvis.core.history.HashtableStoreSupport;
import algvis.ds.dictionaries.bst.BST;
import algvis.ui.VisPanel;
import algvis.ui.view.Layout;
import algvis.ui.view.View;

public class SplayTree extends BST {
	public static String dsName = "splaytree";
	private SplayNode root2, w1, w2;

	@Override
	public String getName() {
		return "splaytree";
	}

	public SplayTree(VisPanel M) {
		super(M);
	}

	public SplayNode getRoot2() {
		return root2;
	}

	public void setRoot2(SplayNode root2) {
		this.root2 = root2;
	}

	SplayNode getW1() {
		return w1;
	}

	public void setW1(SplayNode w1) {
		this.w1 = w1;
	}

	SplayNode getW2() {
		return w2;
	}

	public void setW2(SplayNode w2) {
		this.w2 = w2;
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
		/*if (getW1() != null && getW1().getParent() != null) {
			V.drawWideLine(getW1().x, getW1().y, getW1().getParent().x, getW1()
					.getParent().y);
		}
		if (getW2() != null && getW2().getParent() != null) {
			V.drawWideLine(getW2().x, getW2().y, getW2().getParent().x, getW2().getParent().y);
		}*/
		super.draw(V);
		if (getRoot2() != null) {
			getRoot2().drawTree(V);
		}
	}

	@Override
	public void move() {
		super.move();
		if (getRoot2() != null) {
			getRoot2().moveTree();
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
	public Layout getLayout() {
		return Layout.COMPACT;
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "w1", w1);
		HashtableStoreSupport.store(state, hash + "w2", w2);
		HashtableStoreSupport.store(state, hash + "root2", root2);
		if (root2 != null) root2.storeState(state);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object w1 = state.get(hash + "w1");
		if (w1 != null) this.w1 = (SplayNode) HashtableStoreSupport.restore(w1);
		Object w2 = state.get(hash + "w2");
		if (w2 != null) this.w2 = (SplayNode) HashtableStoreSupport.restore(w2);
		Object root2 = state.get(hash + "root2");
		if (root2 != null) this.root2 = (SplayNode) HashtableStoreSupport.restore(root2);
		if (this.root2 != null) this.root2.restoreState(state);
	}
}
