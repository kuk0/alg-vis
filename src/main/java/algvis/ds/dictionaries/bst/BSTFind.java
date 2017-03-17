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
import algvis.core.visual.ZDepth;
import algvis.ui.view.REL;

import java.util.HashMap;

public class BSTFind extends Algorithm {
    private final BST T;
    private final int K;
    private final HashMap<String, Object> result = new HashMap<String, Object>(); // node

    public BSTFind(BST T, int x) {
        this(T, x, null);
    }

    public BSTFind(BST T, int x, Algorithm a) {
        super(T.panel, a);
        this.T = T;
        K = x;
    }

    @Override
    public void runAlgorithm() {
        setHeader("find", K);
        result.put("node", null);
        final BSTNode v = new BSTNode(T, K, ZDepth.ACTIONNODE);
        v.setColor(NodeColor.FIND);
        addToScene(v);
        if (T.getRoot() == null) {
            v.goToRoot();
            addStep(v, REL.BOTTOM, "empty");
            pause();
            addStep(v, REL.BOTTOM, "notfound");
            v.goDown();
            v.setColor(NodeColor.NOTFOUND);
        } else {
            BSTNode w = T.getRoot();
            v.goAbove(w);
            addStep(w, REL.BOTTOM, "bstfindstart");
            pause();
            while (true) {
                if (w.getKey() == K) {
                    v.goTo(w);
                    addStep(w, REL.BOTTOM, "found");
                    v.setColor(NodeColor.FOUND);
                    result.put("node", w);
                    break;
                } else if (w.getKey() < K) {
                    if (w.getRight() == null) {
                        v.pointInDir(45);
                    } else {
                        v.pointAbove(w.getRight());
                    }
                    addStep(v, REL.LEFT, "bstfindright", K, w.getKey());
                    pause();
                    v.noArrow();
                    w.setColor(NodeColor.DARKER);
                    if (w.getLeft() != null) {
                        w.getLeft().subtreeColor(NodeColor.DARKER);
                    }
                    if (w.getRight() != null) {
                        w = w.getRight();
                        v.goAbove(w);
                    } else { // not found
                        addStep(w, REL.BOTTOMLEFT, "notfound");
                        v.setColor(NodeColor.NOTFOUND);
                        v.goRight();
                        break;
                    }
                } else {
                    if (w.getLeft() == null) {
                        v.pointInDir(135);
                    } else {
                        v.pointAbove(w.getLeft());
                    }
                    addStep(v, REL.RIGHT, "bstfindleft", K, w.getKey());
                    pause();
                    v.noArrow();
                    w.setColor(NodeColor.DARKER);
                    if (w.getRight() != null) {
                        w.getRight().subtreeColor(NodeColor.DARKER);
                    }
                    if (w.getLeft() != null) {
                        w = w.getLeft();
                        v.goAbove(w);
                    } else { // notfound
                        addStep(w, REL.BOTTOMLEFT, "notfound");
                        v.setColor(NodeColor.NOTFOUND);
                        v.goLeft();
                        break;
                    }
                }
                pause();
            }
        }
        if (result.get("node") == null) {
            removeFromScene(v);
        }
        pause();
        if (T.getRoot() != null) {
            T.getRoot().subtreeColor(NodeColor.NORMAL);
        }
        if (result.get("node") != null) {
            removeFromScene(v);
        }
    }

    @Override
    public HashMap<String, Object> getResult() {
        return result;
    }
}
