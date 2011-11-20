package algvis.scapegoattree;

import algvis.bst.BST;
import algvis.core.StringUtils;
import algvis.core.View;
import algvis.core.VisPanel;

public class GBTree extends BST {
	public static String dsName = "scapegoat";
	double alpha = 1.01;
	int del = 0;

	public GBTree(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new GBInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new GBFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new GBDelete(this, x));
	}

	@Override
	public void clear() {
		del = 0;
		root = null;
		setStats();
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

	@Override
	public String stats() {
		if (root == null) {
			return "#" + M.L.getString("nodes") + ": 0;   #"
					+ M.L.getString("deleted") + ": 0;   "
					+ M.L.getString("height") + ": 0 =  1.00\u00b7"
					+ M.L.getString("opt") + ";   " + M.L.getString("avedepth")
					+ ": 0";
		} else {
			root.calcTree();
			return "#"
					+ M.L.getString("nodes")
					+ ": "
					+ root.size
					+ ";   #"
					+ M.L.getString("deleted")
					+ ": "
					+ del
					+ ";   "
					+ M.L.getString("height")
					+ ": "
					+ root.height
					+ " = "
					+ StringUtils.format(root.height
							/ (Math.floor(lg(root.size - del)) + 1), 2, 5)
					+ "\u00b7" + M.L.getString("opt") + ";   "
					+ M.L.getString("avedepth") + ": "
					+ StringUtils.format(root.sumh / (double) root.size, 2, -5);
		}
	}
}
