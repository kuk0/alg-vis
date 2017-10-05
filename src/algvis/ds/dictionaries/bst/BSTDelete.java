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

import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.visual.Edge;
import algvis.core.visual.ZDepth;
import algvis.ui.view.REL;

public class BSTDelete extends BSTAlg {

    public BSTDelete(BST T, int K) {
        super(T, K);
    }

    @Override
    public void runAlgorithm() {
        delete();
    }

    // returns parent of the deleted node; or empty if key was not in the tree
    public Optional<BSTNode> delete() {
        return delete(new BSTNode(T, K, ZDepth.ACTIONNODE));
    }

    public Optional<BSTNode> delete(BSTNode v) {
        this.K = v.getKey();
        setHeader("delete", K);
        addNote("bstdeletestart");
        final BSTNode toDelete = new BSTFind(T, K).find().orElse(null);
        BSTNode parent = null;

        if (toDelete != null) {
            parent = toDelete.getParent();
            addToScene(toDelete);
            toDelete.setColor(NodeColor.DELETE);

            if (toDelete.isLeaf()) { // case I - leaf
                addStep(toDelete, REL.TOP, "bst-delete-case1");
                addStep(toDelete, REL.BOTTOM, "bst-delete-unlink");
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
                BSTNode son;
                if (toDelete.getLeft() == null) {
                    son = toDelete.getRight();
                    addStep(toDelete, REL.LEFT, "bst-delete-case2");
                } else {
                    son = toDelete.getLeft();
                    addStep(toDelete, REL.RIGHT, "bst-delete-case2");
                }
                if (toDelete.isRoot()) {
                    addStep(son, REL.BOTTOM, "bst-delete-newroot", "" + K,
                        son.getKeyS());
                } else {
                    if (son.isLeft() == toDelete.isLeft()) {
                        addToSceneUntilNext(
                            new Edge(toDelete.getParent(), toDelete, son));
                    } else {
                        addToSceneUntilNext(
                            new Edge(toDelete.getParent(), son));
                    }
                    addStep(son, REL.BOTTOM, "bst-delete-linkpar", "" + K,
                        son.getKeyS(), toDelete.getParent().getKeyS());
                }
                pause();
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
                addStep(toDelete, REL.TOP, "bst-delete-case3", "" + K);
                BSTNode son = toDelete.getRight();
                toDelete.setColor(NodeColor.DELETE);
                BSTNode s = new BSTNode(T, -Node.INF, ZDepth.ACTIONNODE);
                s.setColor(NodeColor.FIND);
                addToScene(s);
                s.goAbove(son);
                addStep(s, REL.RIGHT, "bst-delete-succ-start");
                pause();
                while (son.getLeft() != null) {
                    addStep(s, REL.RIGHT, "bst-delete-go-left");
                    s.pointAbove(son.getLeft());
                    pause();
                    s.noArrow();
                    son = son.getLeft();
                    s.goAbove(son);
                }
                s.goTo(son);
                parent = son.getParent();
                if (parent == toDelete) {
                    parent = son;
                }

                final BSTNode p = son.getParent(), r = son.getRight();
                s.setColor(NodeColor.FOUND);
                addStep(s, REL.RIGHT, "bst-delete-succ", "" + K, son.getKeyS());
                pause();
                if (r == null) {
                    addStep(s, REL.BOTTOMLEFT, "bst-delete-succ-unlink");
                } else {
                    addStep(r, REL.BOTTOM, "bst-delete-succ-link", r.getKeyS(),
                        p.getKeyS());
                    if (son.isLeft()) {
                        addToSceneUntilNext(new Edge(p, son, r));
                    } else {
                        addToSceneUntilNext(new Edge(p, r));
                    }
                }
                pause();
                removeFromScene(s);
                s = son;
                addToScene(s);
                if (son.isLeft()) {
                    p.linkLeft(r);
                } else {
                    p.linkRight(r);
                }
                s.goNextTo(toDelete);
                pause();
                addStep(s, REL.RIGHT, "bst-delete-replace", "" + K,
                    son.getKeyS());
                pause();
                if (toDelete.getParent() == null) {
                    T.setRoot(s);
                } else {
                    if (toDelete.isLeft()) {
                        toDelete.getParent().linkLeft(s);
                    } else {
                        toDelete.getParent().linkRight(s);
                    }
                }
                s.setColor(NodeColor.NORMAL);
                s.linkLeft(toDelete.getLeft());
                s.linkRight(toDelete.getRight());
                s.goTo(toDelete);
                removeFromScene(s);
            } // end case III

            toDelete.goDown();
            removeFromScene(toDelete);
            T.reposition();
            addNote("done");
        }
        assert (T.getRoot() == null
            || (T.getRoot().testStructure() && T.getRoot().testStructure()));
        return Optional.ofNullable(parent);
    }
}
