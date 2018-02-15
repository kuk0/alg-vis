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

package algvis.ds.intervaltree;

import java.awt.Color;
import java.util.Hashtable;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ds.intervaltree.IntervalTrees.mimasuType;
import algvis.ui.Fonts;
import algvis.ui.view.View;

public class IntervalNode extends BSTNode {
    // boolean leaf = true;
    int b = 1, e = 1; // zaciatok a koniec intervalu, ktory reprezentuje

    public enum focusType {
        FALSE, // ziadny obdlznik
        TIN, // zeleny obdlznik
        TOUT, // cerveny obdlznik
        TWAIT // iba obvod obdlznika
    }

    focusType focused;
    private boolean markedColor = false;

    public IntervalNode(DataStructure D, int key, int zDepth) {
        super(D, key, zDepth);
        focused = focusType.FALSE;
    }

    private IntervalNode(DataStructure D, int key, int x, int y) {
        super(D, key, x, y);
        bgKeyColor();
    }

    public IntervalNode(IntervalNode v) {
        this(v.D, v.getKey(), v.x, v.y);
    }

    @Override
    public void drawKey(View v) {
        v.setColor(getFgColor());
        if (getKey() != NOKEY) {
            v.drawString(toString(), x, y, Fonts.NORMAL);
        }

        if (!isLeaf()) {
            v.drawStringLeft(Integer.toString(b), x - Node.RADIUS,
                y - Node.RADIUS, Fonts.SMALL);
            v.drawStringRight(Integer.toString(e), x + Node.RADIUS,
                y - Node.RADIUS, Fonts.SMALL);
        } else {
            v.drawString(Integer.toString(e), x, y + Node.RADIUS + 5,
                Fonts.SMALL);
        }
    }

    private static final NodeColor TREE = new NodeColor(Color.BLACK,
        new Color(0xFDFF9A));// 0xFEFFC3));
    private static final NodeColor EMPTY = new NodeColor(Color.BLACK,
        new Color(0xF0F0F0));

    @Override
    protected void drawBg(View v) {
        if (getKey() != NOKEY) {
            if (!isLeaf()) {
                this.setColor(TREE);
            }
        } else {
            this.setColor(EMPTY);
        }

        if (!isLeaf()) {
            v.setColor(this.getBgColor());
            v.fillCircle(x, y, Node.RADIUS);
            v.setColor(this.getFgColor());
            v.drawCircle(x, y, Node.RADIUS);
            if (marked) {
                v.drawCircle(x, y, Node.RADIUS + 2);
            }
        } else {
            if (getKey() != NOKEY) {
                if (this.markedColor) {
                    this.setColor(IN);
                } else {
                    this.setColor(NodeColor.NORMAL);
                }
            } else {
                this.setColor(EMPTY);
            }
            v.setColor(getBgColor());
            v.fillSqr(x, y, Node.RADIUS + 1);// + IntervalTree.minsepx);
            v.setColor(Color.BLACK); // fgcolor);
            v.drawSqr(x, y, Node.RADIUS + 1);// + IntervalTree.minsepx);
            // DOROBIT!!!
            if (marked) {
                v.drawSqr(x, y, Node.RADIUS - 1);
            }
        }
    }

    private int i;

    @Override
    public void drawTree(View v) {
        i = 0;
        drawTree2(v);
    }

    private static final NodeColor IN = new NodeColor(Color.BLACK,
        new Color(0xAAFF95));
    private static final NodeColor OUT = new NodeColor(Color.BLACK,
        new Color(0xFC9A79));
    private static final NodeColor WAIT = new NodeColor(Color.BLACK,
        new Color(0xFFFFFF));

    private void drawTree2(View v) {
        switch (focused) {
        case TIN:
            this.setColor(IN);
            break;
        case TOUT:
            this.setColor(OUT);
            break;
        case TWAIT:
            this.setColor(WAIT);
            break;
        case FALSE:
            break;
        }
        switch (focused) {
        case TIN:
        case TOUT:
        case TWAIT:
            v.setColor(this.getFgColor());
            final int c = (e - b + 1);
            final int d = (int) (Math.log10(c) / Math.log10(2));
            // System.out.println(d + " =vyska-1, minsepy= "
            // + IntervalTree.minsepy);
            final int width = (c) * IntervalTree.minsepx;
            final int height = (d) * DataStructure.minsepy + 4
                + 2 * Node.RADIUS;
            v.drawRoundRectangle(x, y + height / 2 - Node.RADIUS - 3, width / 2,
                height / 2, 8, 8);
            v.setColor(this.getBgColor());
            v.fillRoundRectangle(x, y + height / 2 - Node.RADIUS - 3, width / 2,
                height / 2, 8, 8);
        default:
            break;
        }
        if (state != INVISIBLE && getParent() != null) {
            v.setColor(Color.black);
            v.drawLine(x, y, getParent().x, getParent().y);
        }
        if (getLeft() != null) {
            // System.out.println("kreslim lavy " + getLeft().key + " " +
            // this.key);
            getLeft().drawTree2(v);
        }
        if (D instanceof BST && ((BST) D).order) { // && D.panel.S.layout ==
                                                  // Layout.SIMPLE
            v.setColor(Color.LIGHT_GRAY);
            ++i;
            if (i % 10 == 0) {
                v.drawLine(x, y, x, -22);
            } else {
                v.drawLine(x, y, x, -20);
            }
            if (i % 10 == 0) {
                v.drawString("" + i, x, -29, Fonts.NORMAL);
            } else if (i % 10 == 5) {
                v.drawString("5", x, -27, Fonts.NORMAL);
            } else {
                v.drawString("" + i % 10, x, -27, Fonts.SMALL);
            }
        }
        if (getRight() != null) {
            getRight().drawTree2(v);
        }
        draw(v);
    }

    @Override
    public void rebox() {
        /*
         * if there is a left child, leftw = width of the box enclosing the
         * whole left subtree, i.e., leftw+rightw; otherwise the width is the
         * node RADIUS plus some additional space called xspan
         */
        leftw = (getLeft() == null) ? ((IntervalTree) D).getMinsepx() / 2
            : getLeft().leftw + getLeft().rightw;
        // rightw is computed analogically
        rightw = (getRight() == null) ? ((IntervalTree) D).getMinsepx() / 2
            : getRight().leftw + getRight().rightw;
    }

    public boolean prec(IntervalNode v) {
        if (((IntervalTree) D).minTree == mimasuType.MIN) {
            return getKey() < v.getKey();
        } else {
            return getKey() > v.getKey();
        }
    }

    /**
     * Precedes or equals (see prec).
     */
    public boolean preceq(IntervalNode v) {
        if (((IntervalTree) D).minTree == mimasuType.MIN) {
            return getKey() <= v.getKey();
        } else {
            return getKey() >= v.getKey();
        }
    }

    @Override
    public IntervalNode getRight() {
        return (IntervalNode) super.getRight();
    }

    public void setRight(IntervalNode v) {
        super.setRight(v);
    }

    @Override
    public IntervalNode getLeft() {
        return (IntervalNode) super.getLeft();
    }

    public void setLeft(IntervalNode v) {
        super.setLeft(v);
    }

    @Override
    public IntervalNode getParent() {
        return (IntervalNode) super.getParent();
    }

    public void setParent(IntervalNode v) {
        super.setParent(v);
    }

    public void setInterval(int i, int j) {
        b = i;
        e = j;
    }

    public void markColor() {
        markedColor = true;
    }

    public void unmarkColor() {
        markedColor = false;
    }

    @Override
    public IntervalNode find(int x, int y) {
        if (inside(x, y)) {
            return this;
        }
        if (getLeft() != null) {
            final IntervalNode tmp = getLeft().find(x, y);
            if (tmp != null) {
                return tmp;
            }
        }
        if (getRight() != null) {
            return getRight().find(x, y);
        }
        return null;
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "b", b);
        HashtableStoreSupport.store(state, hash + "e", e);
        HashtableStoreSupport.store(state, hash + "focused", focused);
        HashtableStoreSupport.store(state, hash + "markedColor", markedColor);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object b = state.get(hash + "b");
        if (b != null) {
            this.b = (Integer) HashtableStoreSupport.restore(b);
        }
        final Object e = state.get(hash + "e");
        if (e != null) {
            this.e = (Integer) HashtableStoreSupport.restore(e);
        }
        final Object focused = state.get(hash + "focused");
        if (focused != null) {
            this.focused = (focusType) HashtableStoreSupport.restore(focused);
        }
        final Object markedColor = state.get(hash + "markedColor");
        if (markedColor != null) {
            this.markedColor = (Boolean) HashtableStoreSupport
                .restore(markedColor);
        }
    }
}
