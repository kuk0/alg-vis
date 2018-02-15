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
package algvis.ds.priorityqueues.skewheap;

import java.awt.Color;
import java.util.Hashtable;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ds.priorityqueues.MeldablePQ;
import algvis.ui.view.View;

public class SkewHeapNode extends BSTNode {
    private boolean doubleArrow = false;
    boolean dashedRightLine = false; // if true the line leading to the right
                                    // son is dashed
    private boolean dashedLeftLine = false; // if true the line leading to the
                                           // left son is dashed

    private SkewHeapNode(DataStructure D, int key, int x, int y) {
        super(D, key, x, y);
        bgKeyColor();
    }

    public SkewHeapNode(DataStructure D, int key, int zDepth) {
        super(D, key, zDepth);
        bgKeyColor();
    }

    public SkewHeapNode(SkewHeapNode v) {
        this(v.D, v.getKey(), v.x, v.y);
    }

    public boolean prec(Node v) {
        if (((MeldablePQ) D).minHeap) {
            return this.getKey() < v.getKey();
        } else {
            return this.getKey() > v.getKey();
        }
    }

    public boolean preceq(Node v) {
        if (((MeldablePQ) D).minHeap) {
            return this.getKey() <= v.getKey();
        } else {
            return this.getKey() >= v.getKey();
        }
    }

    public void linkup(SkewHeapNode v) {
        if ((this.getParent() != null) && (v != null)) {
            final SkewHeapNode tmp = this.getParent();
            v.setRight(this);
            this.setParent(v);
            v.setParent(tmp);
            v.getParent().setRight(v);
        }
    }

    public void swapChildren() {
        final SkewHeapNode tmp = this.getLeft();
        this.setLeft(this.getRight());
        this.setRight(tmp);
    }

    public void setDoubleArrow(Node w) {
        dir = w;
        doubleArrow = true;
    }

    public void noDoubleArrow() {
        doubleArrow = false;
    }

    private void drawDoubleArrow(View v) {
        if (!doubleArrow || dir == null) {
            return;
        }
        int x1, y1, x2, y2;
        if (x < dir.x) {
            x1 = x;
            y1 = y;
            x2 = dir.x;
            y2 = dir.y;
        } else {
            x2 = x;
            y2 = y;
            x1 = dir.x;
            y1 = dir.y;
        }
        v.drawDoubleArrow(x1 + 2 * Node.RADIUS, y1, x2 - 2 * Node.RADIUS, y2);
    }

    @Override
    public void draw(View v) {
        super.draw(v);
        drawDoubleArrow(v);
    }

    @Override
    public void repos(int px, int py) {
        this.goTo(px, py);

        if (this.getRight() != null) {
            this.getRight().repos(px + getRight().leftw,
                py + (DataStructure.minsepy));// + 2 * SkewHeapNode.RADIUS));
        }
        if (this.getLeft() != null) {
            this.getLeft().repos(px - getLeft().rightw,
                py + (DataStructure.minsepy));// +
            // 2
            // *
            // SkewHeapNode.RADIUS));
        }
    }

    private void lowlight() {
        bgColor(new Color(200, 200 - getKey() / 10, 0));
    }

    private void highlight() {
        bgKeyColor();
    }

    public void lowlightTree() {
        lowlight();
        if (getLeft() != null) {
            getLeft().lowlightTree();
        }
        if (getRight() != null) {
            getRight().lowlightTree();
        }
    }

    public void highlightTree() {
        highlight();
        if (getLeft() != null) {
            getLeft().highlightTree();
        }
        if (getRight() != null) {
            getRight().highlightTree();
        }
    }

    @Override
    public void drawTree(View v) {

        if (this.state != INVISIBLE) {

            // if (thread) { v.setColor(Color.red); } else {
            v.setColor(Color.black);
            // }

            if ((getLeft() != null) && (getLeft().state != INVISIBLE)) {
                if (dashedLeftLine) {
                    v.drawDashedLine(x, y, getLeft().x, getLeft().y);
                } else {
                    v.drawLine(x, y, getLeft().x, getLeft().y);
                }
            }
            if ((getRight() != null) && (getRight().state != INVISIBLE)) {
                if (dashedRightLine) {
                    v.drawDashedLine(x, y, getRight().x, getRight().y);
                } else {
                    v.drawLine(x, y, getRight().x, getRight().y);
                }
            }
        }
        if (getLeft() != null) {
            getLeft().drawTree(v);
        }
        if (getRight() != null) {
            getRight().drawTree(v);
        }
        draw(v);
    }

    @Override
    public SkewHeapNode getRight() {
        return (SkewHeapNode) super.getRight();
    }

    public void setRight(SkewHeapNode v) {
        super.setRight(v);
    }

    @Override
    public SkewHeapNode getLeft() {
        return (SkewHeapNode) super.getLeft();
    }

    void setLeft(SkewHeapNode v) {
        super.setLeft(v);
    }

    @Override
    public SkewHeapNode getParent() {
        return (SkewHeapNode) super.getParent();
    }

    public void setParent(SkewHeapNode v) {
        super.setParent(v);
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "doubleArrow", doubleArrow);
        HashtableStoreSupport.store(state, hash + "dashedRightLine",
            dashedRightLine);
        HashtableStoreSupport.store(state, hash + "dashedLeftLine",
            dashedLeftLine);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object doubleArrow = state.get(hash + "doubleArrow");
        if (doubleArrow != null) {
            this.doubleArrow = (Boolean) HashtableStoreSupport
                .restore(doubleArrow);
        }
        final Object dashedRightLine = state.get(hash + "dashedRightLine");
        if (dashedRightLine != null) {
            this.dashedRightLine = (Boolean) HashtableStoreSupport
                .restore(dashedRightLine);
        }
        final Object dashedLeftLine = state.get(hash + "dashedLeftLine");
        if (dashedLeftLine != null) {
            this.dashedLeftLine = (Boolean) HashtableStoreSupport
                .restore(dashedLeftLine);
        }
    }
}
