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
package algvis.unionfind;

import algvis.core.DataStructure;
import algvis.core.TreeNode;
import algvis.visual.View;

public class UnionFindNode extends TreeNode {
	public int rank = 0;
	public boolean greyPair = false;

	public UnionFindNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public UnionFindNode(DataStructure D, int key) {
		super(D, key);
	}

	@Override
	public UnionFindNode getChild() {
		return (UnionFindNode) super.getChild();
	}

	@Override
	public UnionFindNode getRight() {
		return (UnionFindNode) super.getRight();
	}

	@Override
	public UnionFindNode getParent() {
		return (UnionFindNode) super.getParent();
	}

	public void unsetGrey() {
		TreeNode w = getChild();
		while (w != null) {
			((UnionFindNode) w).unsetGrey();
			w = w.getRight();
		}
		greyPair = false;
	}

	public void drawGrey(View v) {
		TreeNode w = getChild();
		while (w != null) {
			((UnionFindNode) w).drawGrey(v);
			w = w.getRight();
		}
		if (greyPair && getParent() != null) {
			v.drawWideLine(x, y, getParent().x, getParent().y, 10.0f);
		}
	}

	@Override
	public void drawTree(View v) {
		drawGrey(v);
		super.drawTree(v);
	}
}
