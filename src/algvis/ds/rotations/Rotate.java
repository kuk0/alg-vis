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
package algvis.ds.rotations;

import algvis.core.Algorithm;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.visual.Edge;
import algvis.core.visual.ShadePair;
import algvis.core.visual.ShadeSubtree;
import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.view.CornerEnum;

public class Rotate extends Algorithm {
	private final Rotations R;
	private final BST T;
	private final BSTNode v;

	public Rotate(Rotations R, BSTNode v) {
		super(R.panel);
		this.R = R;
		this.T = R.T;
		this.v = v;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		setHeader("rotate-header", v.getKey());
		if (v == T.getRoot()) {
			addStep(v, CornerEnum.BOTTOM, "rotate-root", v.getKey());
			return;
		}
		final BSTNode u = v.getParent();
		BSTNode a, b, c, p = u.getParent();
		final ShadePair shade = new ShadePair(v, u);
		addToScene(shade);
		final boolean rotR = v.isLeft();
		if (rotR) {
			a = v.getLeft();
			b = v.getRight();
			c = u.getRight();
		} else {
			a = u.getLeft();
			b = v.getLeft();
			c = v.getRight();
		}
		ShadeSubtree shadeA = null, shadeB = null, shadeC = null;
		if (R.subtrees) {
			if (a != null) {
				a.subtreeColor(NodeColor.RED);
				shadeA = new ShadeSubtree(a);
				addToScene(shadeA);
				addStep(shadeA.getRight(), shadeA.getBottom(), 100,
						CornerEnum.BOTTOMLEFT, rotR ? "rotate-rise"
								: "rotate-fall");
			}
			if (b != null) {
				b.subtreeColor(NodeColor.GREEN);
				shadeB = new ShadeSubtree(b);
				addToScene(shadeB);
			}
			if (c != null) {
				c.subtreeColor(NodeColor.BLUE);
				shadeC = new ShadeSubtree(c);
				addToScene(shadeC);
				addStep(shadeC.getLeft(), shadeC.getBottom(), 100,
						CornerEnum.BOTTOMRIGHT, rotR ? "rotate-fall"
								: "rotate-rise");
			}
		}
		pause();
		addToSceneUntilNext(new Edge(v, u));
		if (p != null) {
			if (u.isLeft() == v.isLeft()) {
				addToSceneUntilNext(new Edge(p, u, v));
			} else {
				addToSceneUntilNext(new Edge(p, v));
			}
			addStep(p, CornerEnum.TOP, "rotate-change-parent", u.getKey(),
					v.getKey(), p.getKey());
		} else {
			addToSceneUntilNext(new Edge(u.x, u.y - DataStructure.minsepy, v.x,
					v.y));
			addStep(v.x, (u.y + v.y) / 2, 200, v.isLeft() ? CornerEnum.LEFT
					: CornerEnum.RIGHT, "rotate-newroot", u.getKey(),
					v.getKey());
		}
		addStep(u.x, (u.y + v.y) / 2, 200, v.isLeft() ? CornerEnum.RIGHT
				: CornerEnum.LEFT, "rotate-change", u.getKey(), v.getKey());
		if (b != null) {
			addToSceneUntilNext(new Edge(u, b));
			addStep(b, CornerEnum.BOTTOM, "rotate-change-b", b.getKey(),
					u.getKey());
		} else {
			addToSceneUntilNext(new Edge(u.x, u.y, v.x + (rotR ? +1 : -1)
					* Node.RADIUS, v.y + DataStructure.minsepy));
			addStep(v, v.isLeft() ? CornerEnum.BOTTOMRIGHT
					: CornerEnum.BOTTOMLEFT, "rotate-change-nullb", v.getKey(),
					u.getKey());
		}

		if (u == T.getRoot()) {
			if (b != null) {

			} else {
				addStep("rotate-newroot-bnull", v.getKey(), u.getKey());
			}
		} else {
			if (b != null) {
				addStep("rotate-changes", v.getKey(), b.getKey(), u.getKey(), u
						.getParent().getKey());
			} else {
				addStep("rotate-changes-bnull", v.getKey(), u.getKey(), u
						.getParent().getKey());
			}
		}
		/*if (R.T.order) {
			addNote("rotate-preserves-order");
		}*/
		pause();

		T.rotate(v);
		R.reposition();

		pause();

		v.subtreeColor(NodeColor.NORMAL);
		removeFromScene(shade);
		if (shadeA != null) {
			removeFromScene(shadeA);
		}
		if (shadeB != null) {
			removeFromScene(shadeB);
		}
		if (shadeC != null) {
			removeFromScene(shadeC);
		}

		T.getRoot().calcTree();
	}
}
