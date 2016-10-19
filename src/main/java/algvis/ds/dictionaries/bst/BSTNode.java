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

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.NodePair;
import algvis.core.history.HashtableStoreSupport;
import algvis.ui.Fonts;
import algvis.ui.view.Layout;
import algvis.ui.view.View;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import java.util.Vector;

public class BSTNode extends Node {
    private BSTNode left = null, right = null, parent = null;
    public int leftw, rightw;

    // variables for the Reingold-Tilford layout
    private int offset = 0; // offset from parent node
    private int level; // distance to root
    private boolean thread = false; // is this node threaded?

    // statistics
    public int size = 1, height = 1, sumh = 1;

    protected BSTNode(DataStructure D, int key, int x, int y) {
        super(D, key, x, y);
    }

    public BSTNode(DataStructure D, int key, int zDepth) {
        super(D, key, zDepth);
    }

    public BSTNode(DataStructure d, int key, int x, int y, int zDepth) {
        super(d, key, x, y, zDepth);
    }

    public BSTNode getLeft() {
        if (thread) {
            return null;
        } else {
            return left;
        }
    }

    public void setLeft(BSTNode left) {
        if (thread) {
            thread = false;
            right = null;
        }
        this.left = left;
    }

    public BSTNode getRight() {
        if (thread) {
            return null;
        } else {
            return right;
        }
    }

    public BSTNode setRight(BSTNode right) {
        if (thread) {
            thread = false;
            left = null;
        }
        this.right = right;
        return right;
    }

    public BSTNode getParent() {
        return parent;
    }

    public BSTNode setParent(BSTNode parent) {
        return this.parent = parent;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public boolean isRoot() {
        return getParent() == null;
    }

    public boolean isLeaf() {
        return getLeft() == null && getRight() == null;
    }

    public boolean isLeft() {
        return getParent() != null && getParent().getLeft() == this;
    }

    /**
     * removes edge between this and left; removes edge between newLeft and its
     * parent; creates new edge between this and newLeft
     */
    public void linkLeft(BSTNode newLeft) {
        if (getLeft() != newLeft) {
            if (getLeft() != null) {
                // remove edge between this and left
                unlinkLeft();
            }
            if (newLeft != null) {
                if (newLeft.getParent() != null) {
                    // remove edge between newLeft and its parent
                    newLeft.unlinkParent();
                }
                // create new edge between this and newLeft
                newLeft.setParent(this);
            }
            setLeft(newLeft);
        }
    }

    /**
     * removes edge between this and left
     */
    public void unlinkLeft() {
        getLeft().setParent(null);
        setLeft(null);
    }

    /**
     * removes edge between this and right; removes edge between newRight and
     * its parent; creates new edge between this and newRight
     */
    public void linkRight(BSTNode newRight) {
        if (getRight() != newRight) {
            if (getRight() != null) {
                // remove edge between this and right
                unlinkRight();
            }
            if (newRight != null) {
                if (newRight.getParent() != null) {
                    // remove edge between newRight and its parent
                    newRight.unlinkParent();
                }
                // create new edge between this and newRight
                newRight.setParent(this);
            }
            setRight(newRight);
        }
    }

    /**
     * removes edge between this and right
     */
    public void unlinkRight() {
        getRight().setParent(null);
        setRight(null);
    }

    private void unlinkParent() {
        if (isLeft()) {
            getParent().unlinkLeft();
        } else {
            getParent().unlinkRight();
        }
    }

    public void isolate() {
        setLeft(setRight(setParent(null)));
    }

    private void _preorder(Vector<BSTNode> acc) {
        acc.add(this);
        if (getLeft() != null) {
            getLeft()._preorder(acc);
        }
        if (getRight() != null) {
            getRight()._preorder(acc);
        }
    }

    private void _inorder(Vector<BSTNode> acc) {
        if (getLeft() != null) {
            getLeft()._inorder(acc);
        }
        acc.add(this);
        if (getRight() != null) {
            getRight()._inorder(acc);
        }
    }

    private void _postorder(Vector<BSTNode> acc) {
        if (getLeft() != null) {
            getLeft()._postorder(acc);
        }
        if (getRight() != null) {
            getRight()._postorder(acc);
        }
        acc.add(this);
    }

    public Vector<BSTNode> preorder() {
        Vector<BSTNode> acc = new Vector<BSTNode>();
        this._preorder(acc);
        return acc;
    }

    public Vector<BSTNode> inorder() {
        Vector<BSTNode> acc = new Vector<BSTNode>();
        this._inorder(acc);
        return acc;
    }

    public Vector<BSTNode> postorder() {
        Vector<BSTNode> acc = new Vector<BSTNode>();
        this._postorder(acc);
        return acc;
    }

    /**
     * Calculate the height, size, and sum of heights of this node, assuming
     * that this was already calculated for its children.
     */
    public void calc() {
        int ls = 0, rs = 0, lh = 0, rh = 0, lsh = 0, rsh = 0;
        if (getLeft() != null) {
            ls = getLeft().size;
            lh = getLeft().height;
            lsh = getLeft().sumh;
        }
        if (getRight() != null) {
            rs = getRight().size;
            rh = getRight().height;
            rsh = getRight().sumh;
        }
        size = ls + rs + 1;
        height = Math.max(lh, rh) + 1;
        sumh = lsh + rsh + size;
    }

    /**
     * Calculate the height, size, and sum of heights for all the nodes in this
     * subtree (recursively bottom-up).
     */
    public void calcTree() {
        for (BSTNode v : postorder()) {
            v.calc();
        }
    }

    public void setArc() {
        setArc(getParent());
    }

    // private static int i;

    public void drawTree(View v) {
        if (D instanceof BST && ((BST) D).order) {
            int i = 0;
            v.setColor(Color.LIGHT_GRAY);
            for (BSTNode w : inorder()) {
                ++i;
                if (i % 10 == 0) {
                    v.drawLine(w.x, w.y, w.x, -22);
                    v.drawString("" + i, w.x, -29, Fonts.NORMAL);
                } else {
                    v.drawLine(w.x, w.y, w.x, -20);
                    v.drawString("" + i % 10, w.x, -27, (i % 10 == 5) ? Fonts.NORMAL : Fonts.SMALL);
                }
            }
        }
        for (BSTNode w : postorder()) {
            if (w.state != INVISIBLE && !w.isRoot()) {
                v.setColor(Color.black);
                v.drawLine(w.x, w.y, w.getParent().x, w.getParent().y);
            }
            w.draw(v);
        }
    }

    public void moveTree() {
        for (BSTNode v : postorder()) {
            v.move();
        }
    }

    public void shiftTree(int dx, int dy) {
        for (BSTNode v : postorder()) {
            v.goTo(v.tox + dx, v.toy + dy);
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        Rectangle2D retVal = super.getBoundingBox();
        if (left != null) {
            retVal = retVal.createUnion(left.getBoundingBox());
        }
        if (right != null) {
            retVal = retVal.createUnion(right.getBoundingBox());
        }
        return retVal;
    }

    @Override
    public void endAnimation() {
        super.endAnimation();
        if (left != null) {
            left.endAnimation();
        }
        if (right != null) {
            right.endAnimation();
        }
    }

    @Override
    public boolean isAnimationDone() {
        return super.isAnimationDone()
            && (left == null || left.isAnimationDone())
            && (right == null || right.isAnimationDone());
    }

    /**
     * Create an (imaginary) box around the subtree rooted at this node.
     * Calculate the width from the node to the left side (leftw) and the width
     * from the node to the right side (rightw). Assumption: this box has
     * already been created for both children.
     */
    protected void rebox() {
        /*
         * if there is a left child, leftw = width of the box enclosing the
         * whole left subtree, i.e., leftw+rightw; otherwise the width is the
         * node RADIUS plus some additional space called xspan
         */
        leftw = (getLeft() == null) ? DataStructure.minsepx / 2
            : getLeft().leftw + getLeft().rightw;
        // rightw is computed analogically
        rightw = (getRight() == null) ? DataStructure.minsepx / 2
            : getRight().leftw + getRight().rightw;
    }

    /**
     * Rebox the whole subtree calculating the widths in postorder.
     */
    public void reboxTree() {
        for (BSTNode v : postorder()) {
            v.rebox();
        }
    }

    /**
     * Calculate the coordinates of each node from the widths of boxes around
     * them and direct the nodes to their new positions.
     */
    private void repos() {
        if (isRoot()) {
            goToRoot();
            D.x1 = -leftw;
            D.x2 = rightw;
            D.y2 = this.toy;
        }
        if (this.toy > D.y2) {
            D.y2 = this.toy;
        }
        if (getLeft() != null) {
            getLeft().goTo(this.tox - getLeft().rightw,
                this.toy + DataStructure.minsepy);
            getLeft().repos();
        }
        if (getRight() != null) {
            getRight().goTo(this.tox + getRight().leftw,
                this.toy + DataStructure.minsepy);
            getRight().repos();
        }
    }

    public void repos(int x, int y) {
        goTo(x, y);
        if (getLeft() != null) {
            getLeft().repos(this.tox - getLeft().rightw,
                this.toy + DataStructure.minsepy);
        }
        if (getRight() != null) {
            getRight().repos(this.tox + getRight().leftw,
                this.toy + DataStructure.minsepy);
        }
        if (isRoot()) {
            D.x1 = x - leftw;
            D.x2 = x + rightw;
            D.y2 = this.toy;
        }
        if (this.toy > D.y2) {
            D.y2 = this.toy;
        }
    }

    public void reposition() {
        if (D.getLayout() == Layout.SIMPLE) { // simple layout
            reboxTree();
            repos();
        } else { // Reingold-Tilford layout
            RTThreads();
            RTPreposition();
            RTPetrification(0, 0);
            reboxTree();
        }
    }

    private void RTThreads() {
        if (thread) {
            thread = false;
            left = null;
            right = null;
        }
        if (getLeft() != null) {
            left.RTThreads();
        }
        if (getRight() != null) {
            right.RTThreads();
        }
    }

    /**
     * Find the node at coordinates (x,y). This is used to identify the node
     * that has been clicked by user.
     */
    public BSTNode find(int x, int y) {
        if (inside(x, y)) {
            return this;
        }
        if (getLeft() != null) {
            final BSTNode tmp = getLeft().find(x, y);
            if (tmp != null) {
                return tmp;
            }
        }
        if (getRight() != null) {
            return getRight().find(x, y);
        }
        return null;
    }

    /**
     * Set up the threads with the help of extreme nodes. A node is "extreme"
     * when it is the leftmost/rightmost in the lowest level.
     * <p/>
     * 1. work out left and right subtree 2. get extreme nodes from the left and
     * right subtree 3. calculate the offset from parent & set a new thread if
     * required
     *
     * @return the leftmost and the rightmost node on the deepest level of the
     * subtree rooted at this node
     */
    private NodePair<BSTNode> RTPreposition() {
        final NodePair<BSTNode> result = new NodePair<BSTNode>();
        NodePair<BSTNode> fromLeftSubtree = null, fromRightSubtree = null;
        offset = 0;

        // 1. & 2. work out left & right subtree
        if (getLeft() != null) {
            fromLeftSubtree = getLeft().RTPreposition();
        }
        if (getRight() != null) {
            fromRightSubtree = getRight().RTPreposition();
        }
        // 3. examine this node
        if (isLeaf()) {
            if (!isRoot()) {
                offset = isLeft() ? -DataStructure.minsepx / 2
                    : +DataStructure.minsepx / 2;
            }
            result.left = this;
            result.right = this;
        } else { // This is not a leaf; at least one subtree is non-empty.
            /*
             * If one subtree is empty, it is not necessary to make a new
             * thread. A proper offset must be set.
             */
            if (getLeft() == null) {
                getRight().offset = DataStructure.minsepx / 2;
                result.left = fromRightSubtree.left;
                result.right = fromRightSubtree.right;
                return result;
            }

            if (getRight() == null) {
                getLeft().offset = -DataStructure.minsepx / 2;
                result.left = fromLeftSubtree.left;
                result.right = fromLeftSubtree.right;
                return result;
            }

            // Calculate offsets for the left and the right son.
            int loffset = 0; // offset of this node from the right contour of
            // the left subtree.
            int roffset = 0; // offset of this node from the left contour of the
            // right subtree.
            BSTNode L = getLeft();
            BSTNode R = getRight();
            /*
             * First, left.offset is 0 and only right.offset accumulates. The
             * offsets are corrected afterwards (this way is easier to
             * generalize to m-ary trees). Note that offsets can be negative.
             */
            getLeft().offset = 0;
            getRight().offset = 0;

            // traverse the right contour of the left subtree and the left
            // counour of the right subtree
            while ((L != null) && (R != null)) {
                /*
                 * left.offset + loffset is the horizontal distance from L to
                 * this node. Similarly, right.offset + roffset is the
                 * horizontal distance from R to this node.
                 */
                final int distance = (loffset + DataStructure.minsepx - roffset);
                if (distance > 0) {
                    getRight().offset += distance;
                    roffset += distance;
                }
                /*
                 * When passes through thread there will be for sure incorrect
                 * offset! So Elevator calculate this new offset. In algorithm
                 * TR published by Reingold this value is already calculated.
                 */
                boolean LwasThread = L.thread;
                final boolean RwasThread = R.thread;
                L = (L.right != null) ? L.right : L.left;
                if (L != null) {
                    loffset += L.offset;
                }
                R = (R.left != null) ? R.left : R.right;
                if (R != null) {
                    roffset += R.offset;
                }

                BSTNode Elevator = null;
                if (LwasThread) {
                    LwasThread = false;
                    loffset = 0;
                    Elevator = L;
                    while (Elevator != this) {
                        loffset += Elevator.offset;
                        Elevator = Elevator.getParent();
                    }
                }
                if (RwasThread) {
                    roffset = 0;
                    Elevator = R;
                    while (Elevator != this) {
                        roffset += Elevator.offset;
                        Elevator = Elevator.getParent();
                    }
                }
            }

            /*
             * Now, distances should be 0 for left and some value for right.. So
             * lets change it
             */

            getRight().offset /= 2;
            getLeft().offset = -getRight().offset;

            /*
             * General switch of making a new thread: we want to make a thread
             * iff one pair of extremes is deeper than others. We assume that
             * threads from subtrees are set properly.
             */

            if ((R != null) && (L == null)) { // the right subtree is deeper
                // than the left subtree
                fromLeftSubtree.left.thread = true;
                fromLeftSubtree.left.right = R;
                result.left = fromRightSubtree.left;
                result.right = fromRightSubtree.right;
            } else if ((L != null) && (R == null)) { // the left subtree is
                // deeper than the right
                // subtree
                fromRightSubtree.right.thread = true;
                fromRightSubtree.right.left = L;
                result.left = fromLeftSubtree.left;
                result.right = fromLeftSubtree.right;
            } else if ((L == null) && (R == null)) { // both subtrees have the
                // same height
                result.left = fromLeftSubtree.left;
                result.right = fromRightSubtree.right;
            }

        }
        return result;
    }

    /**
     * Calculate the absolute coordinates from the relative ones and dispose the
     * threads.
     *
     * @param x real x coordinate of parent node
     */
    private void RTPetrification(int x, int y) {
        goTo(x + offset, y);
        if (tox < D.x1) {
            D.x1 = tox;
        }
        if (tox > D.x2) {
            D.x2 = tox;
        }
        if (toy < D.y1) {
            // this case should be always false
            D.y1 = toy;
        }
        if (toy > D.y2) {
            D.y2 = toy;
        }

        if (thread) {
            // thread = false;
            // setLeft(null);
            // setRight(null);
        }
        if (getLeft() != null) {
            getLeft().RTPetrification(tox, y + DataStructure.minsepy);
        }
        if (getRight() != null) {
            getRight().RTPetrification(tox, y + DataStructure.minsepy);
        }
    }

    /**
     * Set color to this subtree.
     *
     * @param color
     */
    public void subtreeColor(NodeColor color) {
        for (BSTNode v : postorder()) {
            v.setColor(color);
        }
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "left", left);
        HashtableStoreSupport.store(state, hash + "right", right);
        HashtableStoreSupport.store(state, hash + "parent", parent);
        HashtableStoreSupport.store(state, hash + "level", level);
        HashtableStoreSupport.store(state, hash + "thread", thread);
        HashtableStoreSupport.store(state, hash + "leftw", leftw);
        HashtableStoreSupport.store(state, hash + "rightw", rightw);
        if (left != null) {
            left.storeState(state);
        }
        if (right != null) {
            right.storeState(state);
        }
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object left = state.get(hash + "left");
        if (left != null) {
            this.left = (BSTNode) HashtableStoreSupport.restore(left);
        }
        final Object right = state.get(hash + "right");
        if (right != null) {
            this.right = (BSTNode) HashtableStoreSupport.restore(right);
        }
        final Object parent = state.get(hash + "parent");
        if (parent != null) {
            this.parent = (BSTNode) HashtableStoreSupport.restore(parent);
        }
        final Object level = state.get(hash + "level");
        if (level != null) {
            this.level = (Integer) HashtableStoreSupport.restore(level);
        }
        final Object thread = state.get(hash + "thread");
        if (thread != null) {
            this.thread = (Boolean) HashtableStoreSupport.restore(thread);
        }
        final Object leftw = state.get(hash + "leftw");
        if (leftw != null) {
            this.leftw = (Integer) HashtableStoreSupport.restore(leftw);
        }
        final Object rightw = state.get(hash + "rightw");
        if (rightw != null) {
            this.rightw = (Integer) HashtableStoreSupport.restore(rightw);
        }

        if (this.left != null) {
            this.left.restoreState(state);
        }
        if (this.right != null) {
            this.right.restoreState(state);
        }
    }

    public boolean testStructure() {
        for (BSTNode v : postorder()) {
            if (v.getLeft() != null && v.getLeft().getParent() != v) {
                return false;
            }
            if (v.getRight() != null && v.getRight().getParent() != v) {
                return false;
            }
        }
        return true;
    }

    public boolean testOrder() {
        Vector<BSTNode> order = inorder();
        for (int i = 0; i < order.size() - 1; ++i) {
            if (order.get(i).getKey() >= order.get(i + 1).getKey()) {
                return false;
            }
        }
        return true;
    }
}
