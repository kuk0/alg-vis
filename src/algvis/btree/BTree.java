package algvis.btree;

import algvis.core.Dictionary;
import algvis.core.View;
import algvis.core.VisPanel;

public class BTree extends Dictionary {
	public static String dsName = "btree";
	int order = 5;
	BNode root = null, v = null;
	int xspan = 5, yspan = 15;

	public BTree(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new BInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new BFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new BDelete(this, x));
	}

	@Override
	public void clear() {
		root = v = null;
		setStats();
	}

	@Override
	public String stats() {
		if (root == null) {
			return "#" + M.S.L.getString("nodes") + ": 0;   #"
					+ M.S.L.getString("keys") + ": 0 = 0% "
					+ M.S.L.getString("full") + ";   " + M.S.L.getString("height")
					+ ": 0";
		} else {
			root.calcTree();
			return "#" + M.S.L.getString("nodes") + ": " + root.nnodes + ";   "
					+ "#" + M.S.L.getString("keys") + ": " + root.nkeys + " = "
					+ (100 * root.nkeys) / (root.nnodes * (order - 1)) + "% "
					+ M.S.L.getString("full") + ";   " + M.S.L.getString("height")
					+ ": " + root.height;
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
	}

	public void reposition() {
		if (root != null) {
			root._reposition();
			M.screen.V.setBounds(x1, y1, x2, y2);
		}
	}
}
