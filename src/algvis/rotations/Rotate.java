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
package algvis.rotations;

import algvis.bst.BST;
import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class Rotate extends Algorithm {
	Rotations R;
	BST T;
	BSTNode v;

	public Rotate(Rotations R, BSTNode v) {
		super(R.T);
		this.R = R;
		this.T = R.T;
		this.v = v;
	}

	@Override
	public void run() {
		if (v == T.getRoot()) {
			// vypis ze to je root...
			return;
		}
		BSTNode u = v.getParent();
		boolean rotR = v.isLeft();
		if (R.subtrees) {
			if (rotR) {
				if (v.getLeft() != null) {
					v.getLeft().subtreeColor(NodeColor.RED);
					v.getLeft().markSubtree = true;
				}
				if (v.getRight() != null) {
					v.getRight().subtreeColor(NodeColor.GREEN);
					v.getRight().markSubtree = true;
				}
				if (u.getRight() != null) {
					u.getRight().subtreeColor(NodeColor.BLUE);
					u.getRight().markSubtree = true;
				}
			} else {
				if (u.getLeft() != null) {
					u.getLeft().subtreeColor(NodeColor.RED);
					u.getLeft().markSubtree = true;
				}
				if (v.getLeft() != null) {
					v.getLeft().subtreeColor(NodeColor.GREEN);
					v.getLeft().markSubtree = true;
				}
				if (v.getRight() != null) {
					v.getRight().subtreeColor(NodeColor.BLUE);
					v.getRight().markSubtree = true;
				}
			}
		}
		mysuspend();

		T.rotate(v);
		R.v = u;
		R.reposition();
		mysuspend();

		R.v = null;
		if (v.getLeft() != null) {
			v.getLeft().subtreeColor(NodeColor.NORMAL);
			v.getLeft().markSubtree = false;
		}
		if (v.getRight() != null) {
			v.getRight().subtreeColor(NodeColor.NORMAL);
			v.getRight().markSubtree = false;
		}
		if (u.getLeft() != null) {
			u.getLeft().subtreeColor(NodeColor.NORMAL);
			u.getLeft().markSubtree = false;
		}
		if (u.getRight() != null) {
			u.getRight().subtreeColor(NodeColor.NORMAL);
			u.getRight().markSubtree = false;
		}
	}
}
