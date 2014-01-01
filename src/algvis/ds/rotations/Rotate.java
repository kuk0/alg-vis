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
import algvis.core.NodeColor;
import algvis.core.visual.ShadePair;
import algvis.core.visual.ShadeSubtree;
import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTNode;

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
            addNote("rotate-root", v.getKey());
            return;
        }
        final BSTNode u = v.getParent();
        BSTNode a, b, c;
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
            }
        }

        if (u == T.getRoot()) {
            if (b != null) {
                addStep("rotate-newroot", v.getKey(), b.getKey(), u.getKey());
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
        if (R.T.order) {
            addNote("rotate-preserves-order");
        }
        if (R.subtrees) {
            addNote("rotate-heights");
        }
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
