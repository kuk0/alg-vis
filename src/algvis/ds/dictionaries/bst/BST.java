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

import algvis.core.Dictionary;
import algvis.core.StringUtils;
import algvis.core.visual.ZDepth;
import algvis.internationalization.Languages;
import algvis.ui.VisPanel;
import algvis.ui.view.ClickListener;
import algvis.ui.view.Layout;
import algvis.ui.view.LayoutListener;
import algvis.ui.view.View;

public class BST extends Dictionary implements LayoutListener, ClickListener {
    public static String dsName = "bst";
    public boolean order = false;

    @Override
    public String getName() {
        return "bst";
    }

    public BST(VisPanel M) {
        super(M);
        M.screen.V.setDS(this);
    }

    @Override
    public BSTNode getRoot() {
        return (BSTNode) super.getRoot();
    }

    public BSTNode setRoot(BSTNode root) {
        super.setRoot(root);
        return root;
    }

    @Override
    public void insert(int x) {
        start(new BSTInsert(this, new BSTNode(this, x, ZDepth.ACTIONNODE)));
    }

    @Override
    public void find(int x) {
        start(new BSTFind(this, x));
    }

    @Override
    public void delete(int x) {
        start(new BSTDelete(this, x));
    }

    @Override
    public void clear() {
        if (getRoot() != null) {
            setRoot(null);
            panel.scene.clear();
            panel.scene.add(this);
            setStats();
            reposition();
            panel.screen.V.resetView();
        }
    }

    @Override
    public String stats() {
        if (getRoot() == null) {
            return Languages.getString("size") + ": 0;   "
                + Languages.getString("height") + ": 0 =  1.00\u00b7"
                + Languages.getString("opt") + ";   "
                + Languages.getString("avedepth") + ": 0";
        } else {
            getRoot().calcTree();
            return Languages.getString("size")
                + ": "
                + getRoot().size
                + ";   "
                + Languages.getString("height")
                + ": "
                + getRoot().height
                + " = "
                + StringUtils.format(
                    getRoot().height / (Math.floor(lg(getRoot().size)) + 1), 2,
                    5)
                + "\u00b7"
                + Languages.getString("opt")
                + ";   "
                + Languages.getString("avedepth")
                + ": "
                + StringUtils.format(getRoot().sumh / (double) getRoot().size,
                    2, -5);
        }
    }

    /**
     * Move and draw all the objects on the scene (in this case the BST and one
     * auxiliary node v).
     */
    @Override
    public void draw(View V) {
        if (getRoot() != null) {
            getRoot().drawTree(V);
        }
    }

    @Override
    public void move() {
        if (getRoot() != null) {
            getRoot().moveTree();
        }
    }

    protected void leftrot(BSTNode v) {
        final BSTNode u = v.getParent();
        if (v.getLeft() == null) {
            u.unlinkRight();
        } else {
            u.linkRight(v.getLeft());
        }
        if (u.isRoot()) {
            setRoot(v);
        } else {
            if (u.isLeft()) {
                u.getParent().linkLeft(v);
            } else {
                u.getParent().linkRight(v);
            }
        }
        v.linkLeft(u);
    }

    protected void rightrot(BSTNode v) {
        final BSTNode u = v.getParent();
        if (v.getRight() == null) {
            u.unlinkLeft();
        } else {
            u.linkLeft(v.getRight());
        }
        if (u.isRoot()) {
            setRoot(v);
        } else {
            if (u.isLeft()) {
                u.getParent().linkLeft(v);
            } else {
                u.getParent().linkRight(v);
            }
        }
        v.linkRight(u);
    }

    /**
     * Rotation is specified by a single vertex v; if v is a left child of its
     * parent, rotate right, if it is a right child, rotate lef This method also
     * recalculates positions of all nodes and their statistics.
     */
    public void rotate(BSTNode v) {
        if (v.isLeft()) {
            rightrot(v);
        } else {
            leftrot(v);
        }
        reposition();
        if (v.getLeft() != null) {
            v.getLeft().calc();
        }
        if (v.getRight() != null) {
            v.getRight().calc();
        }
        v.calc();
    }

    /**
     * Recalculate positions of all nodes in the tree.
     */
    public void reposition() {
        x1 = x2 = y1 = y2 = 0;
        if (getRoot() != null) {
            getRoot().reposition();
        }
        panel.screen.V.setBounds(x1, y1, x2, y2);
    }

    @Override
    public void changeLayout() {
        reposition();
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (getRoot() != null) {
            final BSTNode w = getRoot().find(x, y);
            if (w != null) {
                // w.markSubtree = true;
                panel.buttons.I.setText("" + w.getKey());
            }
        }
    }

    @Override
    public Layout getLayout() {
        return Layout.SIMPLE;
    }
}
