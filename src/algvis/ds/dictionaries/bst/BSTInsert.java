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
package algvis.ds.dictionaries.bst;

import java.util.Optional;

import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ui.view.REL;

public class BSTInsert extends BSTAlg {

    public BSTInsert(BST T, int K) {
        super(T, K);
    }

    @Override
    public void runAlgorithm() {
        insert(new BSTNode(T, K, ZDepth.ACTIONNODE));
    }

    // returns node v if it was inserted, or empty() if a duplicate already existed
    public Optional<BSTNode> insert(BSTNode v) {
        this.K = v.getKey();
        v.setColor(NodeColor.INSERT);
        setHeader("insert", K);
        addToScene(v);

        if (T.getRoot() == null) {
            T.setRoot(v);
            v.goToRoot();
            addStep(v, REL.BOTTOM, "newroot");
        } else {
            BSTNode w = T.getRoot();
            v.goAboveRoot();
            addStep(w, REL.BOTTOM, "bst-insert-start");
            pause();

            while (true) {
                if (w.getKey() == K) {
                    addStep(w, REL.BOTTOM, "alreadythere");
                    v.setColor(NodeColor.NOTFOUND);
                    v.goDown();
                    removeFromScene(v);
                    pause();
                    return Optional.empty();
                } else if (w.getKey() < K) {
                    if (w.getRight() == null) {
                        v.pointInDir(45);
                    } else {
                        v.pointAbove(w.getRight());
                    }
                    addStep(v, REL.LEFT, "bst-insert-right", "" + K,
                        w.getKeyS());
                    pause();
                    v.noArrow();
                    if (w.getRight() != null) {
                        w = w.getRight();
                    } else {
                        w.linkRight(v);
                        break;
                    }
                } else {
                    if (w.getLeft() == null) {
                        v.pointInDir(135);
                    } else {
                        v.pointAbove(w.getLeft());
                    }
                    addStep(v, REL.RIGHT, "bst-insert-left", "" + K,
                        w.getKeyS());
                    pause();
                    v.noArrow();
                    if (w.getLeft() != null) {
                        w = w.getLeft();
                    } else {
                        w.linkLeft(v);
                        break;
                    }
                }
                v.goAbove(w);
                pause();
            }
        }
        T.reposition();
        pause();
        addNote("done");
        v.setColor(NodeColor.NORMAL);
        removeFromScene(v);

        assert (T.getRoot().testStructure() && T.getRoot().testStructure());
        return Optional.of(v);
    }
}
