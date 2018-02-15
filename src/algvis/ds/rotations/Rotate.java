/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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
import algvis.ui.view.REL;

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
    public void runAlgorithm() {
        setHeader("rotate-header", v.getKey());
        if (v == T.getRoot()) {
            addStep(v, REL.BOTTOM, "rotate-root", v.getKeyS());
            pause();
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
                addStep(shadeA.getBoundingBox(), 100, REL.BOTTOMLEFT,
                    rotR ? "rotate-rise" : "rotate-fall");
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
                addStep(shadeC.getBoundingBox(), 100, REL.BOTTOMRIGHT,
                    rotR ? "rotate-fall" : "rotate-rise");
            }
        }
        pause();

        addStep(u.getNodeBoundingBox().createUnion(v.getNodeBoundingBox()), 200,
            v.isLeft() ? REL.RIGHT : REL.LEFT, "rotate-change", u.getKeyS(),
            v.getKeyS());
        addToSceneUntilNext(new Edge(v, u));
        pause();
        if (p != null) {
            if (u.isLeft() == v.isLeft()) {
                addToSceneUntilNext(new Edge(p, u, v));
            } else {
                addToSceneUntilNext(new Edge(p, v));
            }
            addStep(p, REL.TOP, "rotate-change-parent", p.getKeyS(),
                v.getKeyS());
        } else {
            addToSceneUntilNext(
                new Edge(u.x, u.y - DataStructure.minsepy, v.x, v.y));
            addStep(u.getNodeBoundingBox().createUnion(v.getNodeBoundingBox()),
                200, v.isLeft() ? REL.LEFT : REL.RIGHT, "rotate-newroot",
                v.getKeyS());
        }
        pause();
        if (b != null) {
            addToSceneUntilNext(new Edge(u, b));
            addStep(b, REL.BOTTOM, "rotate-change-b", b.getKeyS(), u.getKeyS());
        } else {
            addToSceneUntilNext(
                new Edge(u.x, u.y, v.x + (rotR ? +1 : -1) * Node.RADIUS,
                    v.y + DataStructure.minsepy));
            addStep(v, v.isLeft() ? REL.BOTTOMRIGHT : REL.BOTTOMLEFT,
                "rotate-change-nullb", v.getKeyS(), u.getKeyS());
        }
        pause();

        T.rotate(v);
        R.reposition();

        pause();

        v.subtreeColor(NodeColor.NORMAL);
        removeFromScene(shade);
        removeFromScene(shadeA);
        removeFromScene(shadeB);
        removeFromScene(shadeC);

        T.getRoot().calcTree();
    }
}
