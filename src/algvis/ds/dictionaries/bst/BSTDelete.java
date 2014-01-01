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

import java.util.HashMap;

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;

public class BSTDelete extends Algorithm {
    private final BST T;
    private final int K;
    private final HashMap<String, Object> result = new HashMap<String, Object>(); // "deleted"

    public BSTDelete(BST T, int x) {
        this(T, x, null);
    }

    public BSTDelete(BST T, int x, Algorithm a) {
        super(T.panel, a);
        this.T = T;
        K = x;
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        setHeader("delete", K);
        addNote("bstdeletestart");
        final BSTFind find = new BSTFind(T, K, this);
        find.runAlgorithm();
        final BSTNode toDelete = (BSTNode) find.getResult().get("node");

        if (toDelete != null) {
            result.put("deleted", true);
            addToScene(toDelete);
            toDelete.setColor(NodeColor.DELETE);

            if (toDelete.isLeaf()) { // case I - leaf
                addNote("bst-delete-case1");
                addStep("bst-delete-unlink");
                if (toDelete.isRoot()) {
                    T.setRoot(null);
                } else if (toDelete.isLeft()) {
                    toDelete.getParent().unlinkLeft();
                } else {
                    toDelete.getParent().unlinkRight();
                }
                pause();
            } else if (toDelete.getLeft() == null
                || toDelete.getRight() == null) {
                // case II - 1 child
                addNote("bst-delete-case2");
                BSTNode son;
                if (toDelete.getLeft() == null) {
                    son = toDelete.getRight();
                } else {
                    son = toDelete.getLeft();
                }
                if (son.isLeft() == toDelete.isLeft()) {
                    son.setArc(toDelete.getParent());
                } else {
                    son.pointTo(toDelete.getParent());
                }
                if (toDelete.isRoot()) {
                    addStep("bst-delete-newroot", K, son.getKey());
                } else {
                    addStep("bst-delete-linkpar", K, son.getKey(), toDelete
                        .getParent().getKey());
                }
                pause();
                son.noArc();
                son.noArrow();
                if (toDelete.getLeft() == null) {
                    toDelete.unlinkRight();
                } else {
                    toDelete.unlinkLeft();
                }
                if (toDelete.isRoot()) {
                    T.setRoot(son);
                } else {
                    if (toDelete.isLeft()) {
                        toDelete.getParent().linkLeft(son);
                    } else {
                        toDelete.getParent().linkRight(son);
                    }
                }
            } else { // case III - 2 children
                addNote("bst-delete-case3", K);
                // pause();
                BSTNode son = toDelete.getRight();
                toDelete.setColor(NodeColor.DELETE);
                BSTNode v = new BSTNode(T, -Node.INF, ZDepth.ACTIONNODE);
                v.setColor(NodeColor.FIND);
                addToScene(v);
                v.goAbove(son);
                addStep("bst-delete-succ-start");
                pause();
                while (son.getLeft() != null) {
                    addStep("bst-delete-go-left");
                    v.pointAbove(son.getLeft());
                    pause();
                    v.noArrow();
                    son = son.getLeft();
                    v.goAbove(son);
                }
                v.goTo(son);
                final BSTNode p = son.getParent(), r = son.getRight();
                v.setColor(NodeColor.FOUND);
                addNote("bst-delete-succ", K, son.getKey());
                if (r == null) {
                    addStep("bst-delete-succ-unlink");
                } else {
                    addStep("bst-delete-succ-link", r.getKey(), p.getKey());
                    if (son.isLeft()) {
                        r.pointTo(p);
                    } else {
                        r.setArc(p);
                    }
                }
                pause();
                if (r != null) {
                    r.noArc();
                    r.noArrow();
                }
                removeFromScene(v);
                v = son;
                addToScene(v);
                if (son.isLeft()) {
                    p.linkLeft(r);
                } else {
                    p.linkRight(r);
                }
                v.goNextTo(toDelete);
                pause();
                addStep("bst-delete-replace", K, son.getKey());
                pause();
                if (toDelete.getParent() == null) {
                    T.setRoot(v);
                } else {
                    if (toDelete.isLeft()) {
                        toDelete.getParent().linkLeft(v);
                    } else {
                        toDelete.getParent().linkRight(v);
                    }
                }
                v.setColor(NodeColor.NORMAL);
                v.linkLeft(toDelete.getLeft());
                v.linkRight(toDelete.getRight());
                v.goTo(toDelete);
                removeFromScene(v);
            } // end case III

            toDelete.goDown();
            removeFromScene(toDelete);
            T.reposition();
            addNote("done");
        } else {
            result.put("deleted", false);
        }
    }

    @Override
    public HashMap<String, Object> getResult() {
        return result;
    }
}
