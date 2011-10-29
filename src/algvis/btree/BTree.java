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
		root = null;
		setStats();
	}

	@Override
	public String stats() {
		if (root == null) {
			return "#" + M.a.getString("nodes") + ": 0;   #"
					+ M.a.getString("keys") + ": 0 = 0% "
					+ M.a.getString("full") + ";   " + M.a.getString("height")
					+ ": 0";
		} else {
			root.calcTree();
			return "#" + M.a.getString("nodes") + ": " + root.nnodes + ";   "
					+ "#" + M.a.getString("keys") + ": " + root.nkeys + " = "
					+ (100 * root.nkeys) / (root.nnodes * (order - 1)) + "% "
					+ M.a.getString("full") + ";   " + M.a.getString("height")
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
			M.S.V.setBounds(x1, y1, x2, y2);
		}
	}
}
