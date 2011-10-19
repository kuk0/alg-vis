package algvis.scapegoattree;

import java.awt.Graphics;

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
	public void clean() {
		v = null;
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
	}

	@Override
	public String stats() {
		if (root == null) {
			return "#" + M.a.getString("nodes") + ": 0;   #"
					+ M.a.getString("deleted") + ": 0;   "
					+ M.a.getString("height") + ": 0 =  1.00\u00b7"
					+ M.a.getString("opt") + ";   " + M.a.getString("avedepth")
					+ ": 0";
		} else {
			root.calcTree();
			return "#"
					+ M.a.getString("nodes")
					+ ": "
					+ root.size
					+ ";   #"
					+ M.a.getString("deleted")
					+ ": "
					+ del
					+ ";   "
					+ M.a.getString("height")
					+ ": "
					+ root.height
					+ " = "
					+ StringUtils.format(root.height
							/ (Math.floor(lg(root.size - del)) + 1), 2, 5)
					+ "\u00b7" + M.a.getString("opt") + ";   "
					+ M.a.getString("avedepth") + ": "
					+ StringUtils.format(root.sumh / (double) root.size, 2, -5);
		}
	}
}
