package algvis.heap;

import java.awt.Graphics;

import algvis.bst.BSTNode;
import algvis.core.Node;
import algvis.core.PriorityQueue;
import algvis.core.View;
import algvis.core.VisPanel;

public class Heap extends PriorityQueue {
	public static String dsName = "heap";
	int n = 0;
	BSTNode root = null, v = null, v2 = null;

	public Heap(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new HeapInsert(this, x));
	}

	@Override
	public void delete() {
		start(new HeapDelete(this));
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		if (v == null) {
			// TODO: vypindat
		} else start(new HeapDecrKey(this, (BSTNode)v, delta));
	}
	
	@Override
	public void clear() {
		root = null;
		n = 0;
		setStats();
	}

	@Override
	public void clean() {
		v = v2 = null;
	}

	@Override
	public String stats() {
		if (n == 0) {
			return M.a.getString("size") + ": 0 (" + M.a.getString("emptyheap")
					+ ")";
		} else if (n == 1000) {
			return M.a.getString("size") + ": 1000 ("
					+ M.a.getString("fullheap") + ")";
		} else {
			return M.a.getString("size") + ": " + n;
		}
	}

	@Override
	public void draw(Graphics G, View V) {
		if (root != null) {
			root.moveTree();
			root.drawTree(G, V);
		}
		if (v != null) {
			v.move();
			v.draw(G, V);
		}
		if (v2 != null) {
			v2.move();
			v2.draw(G, V);
		}
	}

	public void reposition() {
		if (root != null) {
			root._reposition();
			M.S.V.setBounds(x1, y1, x2, y2);
		}
	}
	
	public void mouseClicked(int x, int y) {
		if (root == null) return;
		BSTNode v = root.find(x, y);
		if (v != null) {
			if (v.marked) {
				v.unmark();
				chosen = null;
			} else {
				if (chosen != null) chosen.unmark();
				v.mark();
				chosen = v;
			}
		}
	}
}
