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
package algvis.core;

import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.core.DataStructure;
import algvis.ui.VisPanel;
import algvis.ui.view.Layout;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

abstract public class Dictionary extends DataStructure {
	public static String adtName = "dictionary";
	protected Node root;

	protected Dictionary(VisPanel M) {
		super(M);
	}

	@Override
	abstract public void insert(int x);

	abstract public void find(int x);

	abstract public void delete(int x);

	protected Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	@Override
	public Rectangle2D getBoundingBox() {
		return root == null ? null : root.getBoundingBox();
	}

	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}

	@Override
	public void endAnimation() {
		if (root != null) root.endAnimation();
	}

	@Override
	public boolean isAnimationDone() {
		return root == null || root.isAnimationDone();
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "root", root);
		if (root != null) root.storeState(state);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object root = state.get(hash + "root");
		if (root != null) this.root = (Node) HashtableStoreSupport.restore(root);
		if (this.root != null) this.root.restoreState(state);
	}
}
