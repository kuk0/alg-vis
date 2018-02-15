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
package algvis.ds.dictionaries.btree;

import java.awt.Color;
import java.util.Hashtable;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.ui.Fonts;
import algvis.ui.view.View;

public class BNode extends Node {
    private int width;
    private int leftw;
    private int rightw;
    BNode parent = null;
    int numKeys = 1, numChildren = 0;
    int[] keys;
    BNode[] c;
    // View V;

    // statistics
    int nkeys = 1, nnodes = 1, height = 1;

    public BNode(DataStructure D, int key, int x, int y) {
        super(D, key, x, y);
        this.keys = new int[((BTree) D).order + 5];
        c = new BNode[((BTree) D).order + 5];
        this.keys[0] = key;
        numKeys = 1;
        // this.V = D.panel.S.V;
        width = _width();
    }

    public BNode(DataStructure D, int key) {
        this(D, key, 0, UPY);
    }

    public BNode(BNode v) {
        this(v.D, v.keys[0], v.tox, v.toy);
    }

    public BNode(BNode u, BNode v, BNode w) {
        this(u.D, Node.NOKEY, v.tox, v.toy);
        int n1 = u.numKeys, n2 = w.numKeys;
        numKeys = n1 + 1 + n2;
        System.arraycopy(u.keys, 0, keys, 0, n1);
        keys[n1] = v.keys[0];
        for (int i = 0; i < n2; ++i) {
            keys[n1 + 1 + i] = w.keys[i];
        }
        n1 = u.numChildren;
        n2 = w.numChildren;
        numChildren = n1 + n2;
        System.arraycopy(u.c, 0, c, 0, n1);
        System.arraycopy(w.c, 0, c, n1, n2);
        for (int i = 0; i < numChildren; ++i) {
            c[i].parent = this;
        }
        width = _width();
    }

    public boolean isRoot() {
        return parent == null;
    }

    public boolean isLeaf() {
        return numChildren == 0;
    }

    /*
     * public boolean isLeft() { return parent.left==this; } public void
     * linkleft(BSTNode v) { left = v; v.parent = this; } public void
     * linkright(BSTNode v) { right = v; v.parent = this; } public void
     * isolate() { left = right = parent = null; }
     */

    public void calcTree() {
        nkeys = numKeys;
        nnodes = 1;
        for (int i = 0; i < numChildren; ++i) {
            c[i].calcTree();
            nkeys += c[i].nkeys;
            nnodes += c[i].nnodes;
        }
        height = 1 + (isLeaf() ? 0 : c[0].height);
    }

    public void addLeaf(int x) {
        keys[numKeys++] = x;
        for (int i = numKeys - 1; i > 0; --i) {
            if (keys[i] < keys[i - 1]) {
                final int tmp = keys[i];
                keys[i] = keys[i - 1];
                keys[i - 1] = tmp;
            }
        }
        width = _width();
    }

    public int order() {
        for (int i = 0; i < parent.numChildren; ++i) {
            if (parent.c[i] == this) {
                return i;
            }
        }
        return -5; // TODO: vypindat exception
    }

    public void add(int k, BNode v) {
        for (int i = numKeys; i > k; --i) {
            keys[i] = keys[i - 1];
            c[i + 1] = c[i];
        }
        ++numKeys;
        ++numChildren;
        keys[k] = v.keys[0];
        c[k] = v.c[0];
        c[k].parent = this;
        c[k + 1] = v.c[1];
        c[k + 1].parent = this;
        width = _width();
    }

    public boolean isIn(int x) {
        for (int i = 0; i < numKeys; ++i) {
            if (keys[i] == x) {
                return true;
            }
        }
        return false;
    }

    public BNode way(int x) {
        if (x < keys[0]) {
            return c[0];
        }
        for (int i = 1; i < numKeys; ++i) {
            if (x < keys[i]) {
                return c[i];
            }
        }
        return c[numKeys];
    }

    public int search(int x) {
        if (x < keys[0]) {
            return 0;
        }
        for (int i = 1; i < numKeys; ++i) {
            if (x < keys[i]) {
                return i;
            }
        }
        return numKeys;
    }

    public BNode split() {
        final int k = numKeys, ku = numKeys / 2; // , kw = numKeys - ku - 1;
        final BNode u = new BNode(D, keys[0], tox, toy),
            v = new BNode(D, keys[ku], tox, toy),
            w = new BNode(D, keys[k - 1], tox, toy);
        for (int i = 1; i < ku; ++i) {
            u.addLeaf(keys[i]);
        }
        for (int i = ku + 1; i < k - 1; ++i) {
            w.addLeaf(keys[i]);
        }
        if (isLeaf()) {
            u.numChildren = w.numChildren = 0;
        } else {
            u.numChildren = (numChildren + 1) / 2;
            w.numChildren = numChildren / 2;
            for (int i = 0; i < u.numChildren; ++i) {
                u.c[i] = c[i];
                u.c[i].parent = u;
            }
            for (int i = 0; i < w.numChildren; ++i) {
                w.c[i] = c[u.numChildren + i];
                w.c[i].parent = w;
            }
        }
        u.parent = w.parent = v;
        v.numChildren = 2;
        v.parent = parent;
        v.c[0] = u;
        v.c[1] = w;
        u.width = u._width();
        w.width = w._width();
        u.x = u.tox = tox - u.width / 2 - Node.RADIUS;
        w.x = w.tox = tox + w.width / 2 + Node.RADIUS;
        return v;
    }

    public BNode del(int k) {
        int i = -1;
        while (keys[++i] != k) {
        }
        final int p = i;
        for (--numKeys; i < numKeys; i++) {
            keys[i] = keys[i + 1];
        }
        width = _width();
        return new BNode(D, k, tox - (numKeys + 1 - 2 * p) * Node.RADIUS, toy);
    }

    public BNode delMin() {
        final int r = keys[0];
        --numKeys;
        System.arraycopy(keys, 1, keys, 0, numKeys);
        width = _width();
        return new BNode(D, r, tox - (numKeys - 1) * Node.RADIUS, toy);
    }

    public BNode delMinCh() {
        final BNode r = c[0];
        --numChildren;
        System.arraycopy(c, 1, c, 0, numChildren);
        width = _width();
        return r;
    }

    public BNode delMax() {
        final BNode r = new BNode(D, keys[--numKeys],
            tox + (numKeys - 1) * Node.RADIUS, toy);
        width = _width();
        return r;
    }

    public BNode delMaxCh() {
        final BNode r = c[--numChildren];
        width = _width();
        return r;
    }

    public void insMin(int k) {
        System.arraycopy(keys, 0, keys, 1, numKeys++);
        keys[0] = k;
        width = _width();
    }

    public void insMinCh(BNode v) {
        System.arraycopy(c, 0, c, 1, numChildren++);
        c[0] = v;
        width = _width();
    }

    public void insMax(int k) {
        keys[numKeys++] = k;
        width = _width();
    }

    public void insMaxCh(BNode v) {
        c[numChildren++] = v;
        width = _width();
    }

    public void replace(int x, int y) {
        int i = -1;
        while (keys[++i] != x) {
        }
        keys[i] = y;
        width = _width();
    }

    String toString(int max) {
        if (numKeys == 0 || max == 0) {
            return "";
        }
        String str = "";
        if (keys[0] == INF) {
            str = "\u221e";
        } else if (keys[0] == -INF) {
            str = "-\u221e";
        } else {
            str = "" + keys[0];
        }
        for (int i = 1; i < Math.min(numKeys, max); ++i) {
            str = str + "  " + keys[i];
        }
        return str;
    }

    @Override
    public String toString() {
        return toString(numKeys);
    }

    int _width() {
        if (keys[0] != Node.NOKEY && numKeys > 0) {
            return Math.max(Fonts.NORMAL.fm.stringWidth(toString()) + 4,
                2 * Node.RADIUS);
        } else {
            return 2 * Node.RADIUS;
        }
    }

    int pos(int i) {
        if (i < 0) {
            return tox
                - D.panel.screen.V.stringWidth(toString(), Fonts.NORMAL) / 2
                - Node.RADIUS;
        }
        if (i >= numKeys) {
            return tox
                + D.panel.screen.V.stringWidth(toString(), Fonts.NORMAL) / 2
                + Node.RADIUS;
        }
        if (numKeys <= 1) {
            return tox;
        }
        final String s = toString(i);
        String t;
        if (i == 0) {
            t = "" + keys[0];
        } else {
            t = "  " + keys[i];
        }
        return tox - D.panel.screen.V.stringWidth(toString(), Fonts.NORMAL) / 2
            + D.panel.screen.V.stringWidth(s, Fonts.NORMAL)
            + D.panel.screen.V.stringWidth(t, Fonts.NORMAL) / 2;
    }

    @Override
    public void drawBg(View V) {
        V.setColor(getBgColor());
        V.fillRoundRectangle(x, y, width / 2, Node.RADIUS, 2 * Node.RADIUS,
            2 * Node.RADIUS);
        V.setColor(getFgColor());
        V.drawRoundRectangle(x, y, width / 2, Node.RADIUS, 2 * Node.RADIUS,
            2 * Node.RADIUS);
        // g.drawLine (x-leftw, y+2, x+rightw, y-2);
    }

    @Override
    public void drawKey(View V) {
        if (keys[0] != Node.NOKEY && numKeys > 0) {
            V.drawString(toString(), x, y, Fonts.NORMAL);
        }
    }

    public void drawTree(View v) {
        for (int i = 0; i < numChildren; ++i) {
            v.setColor(Color.black);
            /*
             * int xx, yy; if (i==0 || i==numChildren-1) { xx = x; yy = y; }
             * else { xx = (pos(i-1)+pos(i))/2; yy = y+D.RADIUS; }
             */
            v.drawLine(x, y, c[i].x, c[i].y - Node.RADIUS);
            c[i].drawTree(v);
        }
        draw(v);
    }

    public void moveTree() {
        for (int i = 0; i < numChildren; ++i) {
            c[i].moveTree();
        }
        move();
    }

    void rebox() {
        if (numChildren == 0) {
            leftw = rightw = width / 2 + ((BTree) D).xspan; // numKeys *
            // D.RADIUS +
            // D.xspan;
        } else {
            if (numChildren % 2 == 0) {
                leftw = rightw = 0;
            } else {
                leftw = c[numChildren / 2].leftw;
                rightw = c[numChildren / 2].rightw;
            }
            for (int i = 0; i < numChildren / 2; ++i) {
                leftw += c[i].leftw + c[i].rightw;
            }
            for (int i = (numChildren + 1) / 2; i < numChildren; ++i) {
                rightw += c[i].leftw + c[i].rightw;
            }
        }
    }

    void reboxTree() {
        for (int i = 0; i < numChildren; ++i) {
            c[i].reboxTree();
        }
        rebox();
    }

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
        int x = this.tox, x2 = this.tox;
        final int y = this.toy + 2 * Node.RADIUS + ((BTree) D).yspan;
        if (numChildren == 0) {
            return;
        }
        if (numChildren % 2 == 0) {
            int k = numChildren / 2 - 1;
            c[k].goTo(x -= c[k].rightw, y);
            c[k].repos();
            for (int i = k - 1; i >= 0; --i) {
                c[i].goTo(x -= c[i + 1].leftw + c[i].rightw, y);
                c[i].repos();
            }
            c[++k].goTo(x2 += c[k].leftw, y);
            c[k].repos();
            for (int i = k + 1; i < numChildren; ++i) {
                c[i].goTo(x2 += c[i - 1].rightw + c[i].leftw, y);
                c[i].repos();
            }
        } else {
            final int k = numChildren / 2;
            c[k].goTo(x, y);
            c[k].repos();
            for (int i = 1; i <= k; ++i) {
                c[k - i].goTo(x -= c[k - i].rightw + c[k - i + 1].leftw, y);
                c[k - i].repos();
                c[k + i].goTo(x2 += c[k + i].leftw + c[k + i - 1].rightw, y);
                c[k + i].repos();
            }
        }
    }

    public void _reposition() {
        reboxTree();
        repos();
    }

    int _goToX(BNode v) {
        final int x = keys[0];
        int p = v.numKeys;
        for (int i = 0; i < p; ++i) {
            if (x <= v.keys[i]) {
                p = i;
            }
        }
        return (v.pos(p - 1) + v.pos(p)) / 2;
    }

    public void goTo(BNode v) {
        goTo(_goToX(v), v.toy);
    }

    public void goAbove(BNode v) {
        goTo(_goToX(v), v.toy - 2 * Node.RADIUS + 2);
    }

    public void goBelow(BNode v) {
        goTo(_goToX(v), v.toy + 2 * Node.RADIUS - 2);
    }

    /*
     * public void goToRoot() { if (((BTree)D).root == null) { goTo (D.rootx,
     * D.rooty); } else { goTo(_goToX(((BTree)D).root), D.rooty); } }
     * 
     * public void goAboveRoot() { if (((BTree)D).root == null) { goTo (D.rootx,
     * D.rooty - 2*D.RADIUS); } else { goTo(_goToX(((BTree)D).root),
     * D.rooty-2*D.RADIUS); } }
     */

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "parent", parent);
        HashtableStoreSupport.store(state, hash + "c", c.clone());
        for (final BNode node : c) {
            if (node != null) {
                node.storeState(state);
            }
        }
        HashtableStoreSupport.store(state, hash + "numKeys", numKeys);
        HashtableStoreSupport.store(state, hash + "numChildren", numChildren);
        HashtableStoreSupport.store(state, hash + "keys", keys.clone());
        HashtableStoreSupport.store(state, hash + "leftw", leftw);
        HashtableStoreSupport.store(state, hash + "rightw", rightw);
        HashtableStoreSupport.store(state, hash + "width", width);
        HashtableStoreSupport.store(state, hash + "nkeys", nkeys);
        HashtableStoreSupport.store(state, hash + "nnodes", nnodes);
        HashtableStoreSupport.store(state, hash + "height", height);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object parent = state.get(hash + "parent");
        if (parent != null) {
            this.parent = (BNode) HashtableStoreSupport.restore(parent);
        }

        final Object c = state.get(hash + "c");
        if (c != null) {
            this.c = (BNode[]) HashtableStoreSupport.restore(c);
        }
        for (final BNode node : this.c) {
            if (node != null) {
                node.restoreState(state);
            }
        }

        final Object numKeys = state.get(hash + "numKeys");
        if (numKeys != null) {
            this.numKeys = (Integer) HashtableStoreSupport.restore(numKeys);
        }
        final Object numChildren = state.get(hash + "numChildren");
        if (numChildren != null) {
            this.numChildren = (Integer) HashtableStoreSupport
                .restore(numChildren);
        }
        final Object keys = state.get(hash + "keys");
        if (keys != null) {
            this.keys = (int[]) HashtableStoreSupport.restore(keys);
        }
        final Object leftw = state.get(hash + "leftw");
        if (leftw != null) {
            this.leftw = (Integer) HashtableStoreSupport.restore(leftw);
        }
        final Object rightw = state.get(hash + "rightw");
        if (rightw != null) {
            this.rightw = (Integer) HashtableStoreSupport.restore(rightw);
        }
        final Object width = state.get(hash + "width");
        if (width != null) {
            this.width = (Integer) HashtableStoreSupport.restore(width);
        }
        final Object nkeys = state.get(hash + "nkeys");
        if (nkeys != null) {
            this.nkeys = (Integer) HashtableStoreSupport.restore(nkeys);
        }
        final Object height = state.get(hash + "height");
        if (height != null) {
            this.height = (Integer) HashtableStoreSupport.restore(height);
        }
        final Object nnodes = state.get(hash + "nnodes");
        if (nnodes != null) {
            this.nnodes = (Integer) HashtableStoreSupport.restore(nnodes);
        }
    }
}
