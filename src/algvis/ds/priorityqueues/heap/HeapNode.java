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
package algvis.ds.priorityqueues.heap;

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ds.priorityqueues.PriorityQueue;
import algvis.ui.Fonts;
import algvis.ui.view.View;

public class HeapNode extends BSTNode {

    protected HeapNode(DataStructure D, int key, int x, int y, int zDepth) {
        super(D, key, x, y, zDepth);
        bgKeyColor();
    }

    public HeapNode(DataStructure D, int key, int zDepth) {
        super(D, key, zDepth);
        bgKeyColor();
    }

    public HeapNode(HeapNode v) {
        this(v.D, v.getKey(), v.tox, v.toy, ZDepth.ACTIONNODE);
    }

    @Override
    public HeapNode getLeft() {
        return (HeapNode) super.getLeft();
    }

    @Override
    public HeapNode getRight() {
        return (HeapNode) super.getRight();
    }

    @Override
    public HeapNode getParent() {
        return (HeapNode) super.getParent();
    }

    /**
     * v.prec(w) iff v precedes w in the heap order, i.e., should be higher in
     * the heap v precedes w if v.key < w.key when we have a min heap, but v
     * precedes w if v.key > w.key when we have a max heap
     */
    public boolean prec(Node v) {
        if (((PriorityQueue) D).minHeap) {
            return this.getKey() < v.getKey();
        } else {
            return this.getKey() > v.getKey();
        }
    }

    /**
     * Precedes or equals (see prec).
     */
    public boolean preceq(Node v) {
        if (((PriorityQueue) D).minHeap) {
            return this.getKey() <= v.getKey();
        } else {
            return this.getKey() >= v.getKey();
        }
    }

    public void drawTree(int n, int k, View v) {
        if (getLeft() != null) {
            getLeft().drawTree(n, 2 * k, v);
        }
        if (getRight() != null) {
            getRight().drawTree(n, 2 * k + 1, v);
        }
        if (!isRoot()) {
            v.setColor(Color.BLACK);
            v.drawLine(x, y, getParent().x, getParent().y);
        }
        draw(v);
        int ax = (2 * k - n) * Node.RADIUS, ay = -50;
        v.setColor(Color.YELLOW);
        v.fillSqr(ax, ay, Node.RADIUS);
        v.setColor(Color.BLACK);
        v.drawSqr(ax, ay, Node.RADIUS);
        v.drawString(getKeyS(), ax, ay, Fonts.NORMAL);
        v.drawString("" + k, ax, ay - Node.RADIUS - 5, Fonts.SMALL);
    }

}
