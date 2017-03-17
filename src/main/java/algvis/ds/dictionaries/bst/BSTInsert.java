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

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.ui.view.REL;

import java.util.HashMap;

import java.util.HashMap;

public class BSTInsert extends Algorithm {
    private final BST T;
    private final int K;
    private final BSTNode v;
    private final HashMap<String, Object> result = new HashMap<String, Object>(); // "inserted", "w", "v"

    public BSTInsert(BST T, BSTNode v) {
        this(T, v, null);
    }

    public BSTInsert(BST T, BSTNode v, Algorithm a) {
        super(T.panel, a);
        this.T = T;
        this.v = v;
        K = v.getKey();
        v.setColor(NodeColor.INSERT);
    }

    @Override
    public void runAlgorithm() {
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
                    result.put("inserted", false);
                    pause();
                    return;
                } else if (w.getKey() < K) {
                    if (w.getRight() == null) {
                        v.pointInDir(45);
                    } else {
                        v.pointAbove(w.getRight());
                    }
                    addStep(v, REL.LEFT, "bst-insert-right", K, w.getKey());
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
                    addStep(v, REL.RIGHT, "bst-insert-left", K, w.getKey());
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
            result.put("w", w);
        }
        result.put("inserted", true);
        result.put("v", v);
        T.reposition();
        pause();
        addNote("done");
        v.setColor(NodeColor.NORMAL);
        removeFromScene(v);
        // v.setZDepth(ZDepth.DS);

        assert (T.getRoot().testStructure() && T.getRoot().testStructure());
    }

    @Override
    public HashMap<String, Object> getResult() {
        return result;
    }
}
