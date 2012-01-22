package algvis.heap;

import algvis.bst.BSTNode;
import algvis.core.ClickListener;
import algvis.core.Node;
import algvis.core.PriorityQueue;
import algvis.core.View;
import algvis.core.VisPanel;

public class Heap extends PriorityQueue implements ClickListener {
	public static String dsName = "heap";
	int n = 0;
	BSTNode root = null, v = null, v2 = null;

	public String getName() {
		return "heap";
	}
	
	public Heap(VisPanel M) {
		super(M, dsName);
		M.screen.V.setDS(this);
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
		root = v = v2 = null;
		n = 0;
		setStats();
	}

	@Override
	public String stats() {
		if (n == 0) {
			return M.S.L.getString("size") + ": 0 (" + M.S.L.getString("emptyheap")
					+ ")";
		} else if (n == 1000) {
			return M.S.L.getString("size") + ": 1000 ("
					+ M.S.L.getString("fullheap") + ")";
		} else {
			return M.S.L.getString("size") + ": " + n;
		}
	}

	@Override
	public void draw(View V) {
		if (root != null) {
			root.moveTree();
			root.drawTree(V);
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
		if (v2 != null) {
			v2.move();
			v2.draw(V);
		}
	}

	public void reposition() {
		if (root != null) {
			root.reposition();
			M.screen.V.setBounds(x1, y1, x2, y2);
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
