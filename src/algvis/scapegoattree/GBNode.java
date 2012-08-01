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
package algvis.scapegoattree;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;

import java.util.Hashtable;

public class GBNode extends BSTNode {
	private boolean deleted = false;

	public GBNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public GBNode(DataStructure D, int key) {
		super(D, key);
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public GBNode getLeft() {
		return (GBNode) super.getLeft();
	}

	@Override
	public GBNode getRight() {
		return (GBNode) super.getRight();
	}

	@Override
	public GBNode getParent() {
		return (GBNode) super.getParent();
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		state.put(hash + "deleted", deleted);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Boolean deleted = (Boolean) state.get(hash + "deleted");
		if (deleted != null) this.deleted = deleted;
	}
}
