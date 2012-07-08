/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.btree;

import java.awt.Color;

import algvis.core.NodeColor;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.gui.Fonts;
import algvis.gui.view.View;

public class BNode extends Node {
	private int width;
    private int leftw;
    private int rightw;
	BNode parent = null;
	int numKeys = 1, numChildren = 0;
	final int[] key;
	final BNode[] c;
	// View V;

	// statistics
	int nkeys = 1, nnodes = 1, height = 1;

	public BNode(DataStructure D, int key, int x, int y) {
		this.key = new int[((BTree) D).order + 5];
		c = new BNode[((BTree) D).order + 5];
		this.key[0] = key;
		numKeys = 1;
		this.D = D;
		// this.V = D.M.S.V;
		this.x = tox = x;
		this.y = toy = y;
		steps = 0;
		setColor(NodeColor.NORMAL);
		width = _width();
	}

	public BNode(DataStructure D, int key) {
		this(D, key, 0, 0);
		getReady();
	}

	public BNode(BNode v) {
		this(v.D, v.key[0], v.x, v.y);
	}

	public BNode(BNode u, BNode v, BNode w) {
		this(u.D, Node.NOKEY, v.x, v.y);
		int n1 = u.numKeys, n2 = w.numKeys;
		numKeys = n1 + 1 + n2;
        System.arraycopy(u.key, 0, key, 0, n1);
		key[n1] = v.key[0];
		for (int i = 0; i < n2; ++i) {
			key[n1 + 1 + i] = w.key[i];
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
		key[numKeys++] = x;
		for (int i = numKeys - 1; i > 0; --i) {
			if (key[i] < key[i - 1]) {
				int tmp = key[i];
				key[i] = key[i - 1];
				key[i - 1] = tmp;
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
			key[i] = key[i - 1];
			c[i + 1] = c[i];
		}
		++numKeys;
		++numChildren;
		key[k] = v.key[0];
		c[k] = v.c[0];
		c[k].parent = this;
		c[k + 1] = v.c[1];
		c[k + 1].parent = this;
		width = _width();
	}

	public boolean isIn(int x) {
		for (int i = 0; i < numKeys; ++i) {
			if (key[i] == x) {
				return true;
			}
		}
		return false;
	}

	public BNode way(int x) {
		if (x < key[0]) {
			return c[0];
		}
		for (int i = 1; i < numKeys; ++i) {
			if (x < key[i]) {
				return c[i];
			}
		}
		return c[numKeys];
	}

	public int search(int x) {
		if (x < key[0]) {
			return 0;
		}
		for (int i = 1; i < numKeys; ++i) {
			if (x < key[i]) {
				return i;
			}
		}
		return numKeys;
	}

	public BNode split() {
		int k = numKeys, ku = numKeys / 2; // , kw = numKeys - ku - 1;
		BNode u = new BNode(D, key[0], x, y), v = new BNode(D, key[ku], x, y), w = new BNode(
				D, key[k - 1], x, y);
		for (int i = 1; i < ku; ++i) {
			u.addLeaf(key[i]);
		}
		for (int i = ku + 1; i < k - 1; ++i) {
			w.addLeaf(key[i]);
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
		u.x = x - u.width / 2 - Node.radius;
		w.x = x + w.width / 2 + Node.radius;
		return v;
	}

	public BNode del(int k) {
		int i = -1;
		while (key[++i] != k) {
		}
		int p = i;
		for (--numKeys; i < numKeys; i++) {
			key[i] = key[i + 1];
		}
		width = _width();
		return new BNode(D, k, x - (numKeys + 1 - 2 * p) * Node.radius, y);
	}

	public BNode delMin() {
		int r = key[0];
		--numKeys;
        System.arraycopy(key, 1, key, 0, numKeys);
		width = _width();
		return new BNode(D, r, x - (numKeys - 1) * Node.radius, y);
	}

	public BNode delMinCh() {
		BNode r = c[0];
		--numChildren;
        System.arraycopy(c, 1, c, 0, numChildren);
		width = _width();
		return r;
	}

	public BNode delMax() {
		BNode r = new BNode(D, key[--numKeys], x + (numKeys - 1) * Node.radius, y);
		width = _width();
		return r;
	}

	public BNode delMaxCh() {
		BNode r = c[--numChildren];
		width = _width();
		return r;
	}

	public void insMin(int k) {
        System.arraycopy(key, 0, key, 1, numKeys++);
		key[0] = k;
		width = _width();
	}

	public void insMinCh(BNode v) {
        System.arraycopy(c, 0, c, 1, numChildren++);
		c[0] = v;
		width = _width();
	}

	public void insMax(int k) {
		key[numKeys++] = k;
		width = _width();
	}

	public void insMaxCh(BNode v) {
		c[numChildren++] = v;
		width = _width();
	}

	public void replace(int x, int y) {
		int i = -1;
		while (key[++i] != x) {
		}
		key[i] = y;
		width = _width();
	}

	String toString(int max) {
		if (numKeys == 0 || max == 0) {
			return "";
		}
		String str = "";
		if (key[0] == INF) {
			str = "\u221e";
		} else if (key[0] == -INF) {
			str = "-\u221e";
		} else {
			str = "" + key[0];
		}
		for (int i = 1; i < Math.min(numKeys, max); ++i) {
			str = str + "  " + key[i];
		}
		return str;
	}

	@Override
	public String toString() {
		return toString(numKeys);
	}

	int _width() {
		if (key[0] != Node.NOKEY && numKeys > 0) {
			return Math.max(Fonts.NORMAL.fm.stringWidth(toString()) + 4,
					2 * Node.radius);
		} else {
			return 2 * Node.radius;
		}
	}

	int pos(int i) {
		if (i < 0) {
			return tox - D.M.screen.V.stringWidth(toString(), Fonts.NORMAL) / 2 - Node.radius;
		}
		if (i >= numKeys) {
			return tox + D.M.screen.V.stringWidth(toString(), Fonts.NORMAL) / 2 + Node.radius;
		}
		if (numKeys <= 1) {
			return x;
		}
		String s = toString(i), t;
		if (i == 0) {
			t = "" + key[0];
		} else {
			t = "  " + key[i];
		}
		return tox - D.M.screen.V.stringWidth(toString(), Fonts.NORMAL) / 2
				+ D.M.screen.V.stringWidth(s, Fonts.NORMAL)
				+ D.M.screen.V.stringWidth(t, Fonts.NORMAL) / 2;
	}

	@Override
	public void drawBg(View V) {
		V.setColor(getBgColor());
		V.fillRoundRectangle(x, y, width / 2, Node.radius, 2 * Node.radius,
				2 * Node.radius);
		V.setColor(getFgColor());
		V.drawRoundRectangle(x, y, width / 2, Node.radius, 2 * Node.radius,
				2 * Node.radius);
		// g.drawLine (x-leftw, y+2, x+rightw, y-2);
	}

	@Override
	public void drawKey(View V) {
		if (key[0] != Node.NOKEY && numKeys > 0) {
			V.drawString(toString(), x, y, Fonts.NORMAL);
		}
	}

	public void drawTree(View v) {
		for (int i = 0; i < numChildren; ++i) {
			v.setColor(Color.black);
			/*
			 * int xx, yy; if (i==0 || i==numChildren-1) { xx = x; yy = y; }
			 * else { xx = (pos(i-1)+pos(i))/2; yy = y+D.radius; }
			 */
			v.drawLine(x, y, c[i].x, c[i].y - Node.radius);
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
			// D.radius +
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
		int x = this.tox, x2 = this.tox, y = this.toy + 2 * Node.radius
				+ ((BTree) D).yspan;
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
			int k = numChildren / 2;
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
		int x = key[0], p = v.numKeys;
		for (int i = 0; i < p; ++i) {
			if (x <= v.key[i]) {
				p = i;
			}
		}
		return (v.pos(p - 1) + v.pos(p)) / 2;
	}

	public void goTo(BNode v) {
		goTo(_goToX(v), v.toy);
	}

	public void goAbove(BNode v) {
		goTo(_goToX(v), v.toy - 2 * Node.radius + 2);
	}

	public void goBelow(BNode v) {
		goTo(_goToX(v), v.toy + 2 * Node.radius - 2);
	}

	/*
	 * public void goToRoot() { if (((BTree)D).root == null) { goTo (D.rootx,
	 * D.rooty); } else { goTo(_goToX(((BTree)D).root), D.rooty); } }
	 * 
	 * public void goAboveRoot() { if (((BTree)D).root == null) { goTo (D.rootx,
	 * D.rooty - 2*D.radius); } else { goTo(_goToX(((BTree)D).root),
	 * D.rooty-2*D.radius); } }
	 */
}
