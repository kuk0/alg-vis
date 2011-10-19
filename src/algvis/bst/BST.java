package algvis.bst;

import java.awt.Graphics;

import algvis.core.Dictionary;
import algvis.core.StringUtils;
import algvis.core.View;
import algvis.core.VisPanel;

public class BST extends Dictionary {
	public static String dsName = "bst";
	public BSTNode root = null, v = null;

	public BST(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new BSTInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new BSTFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new BSTDelete(this, x));
	}

	@Override
	public void clear() {
		root = null;
		setStats();
	}

	@Override
	public void clean() {
		v = null;
	}

	@Override
	public String stats() {
		if (root == null) {
			return M.a.getString("size") + ": 0;   " + M.a.getString("height")
					+ ": 0 =  1.00\u00b7" + M.a.getString("opt") + ";   "
					+ M.a.getString("avedepth") + ": 0";
		} else {
			root.calcTree();
			return M.a.getString("size")
					+ ": "
					+ root.size
					+ ";   "
					+ M.a.getString("height")
					+ ": "
					+ root.height
					+ " = "
					+ StringUtils.format(root.height / (Math.floor(lg(root.size)) + 1), 2,
							5) + "\u00b7" + M.a.getString("opt") + ";   "
					+ M.a.getString("avedepth") + ": "
					+ StringUtils.format(root.sumh / (double) root.size, 2, -5);
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
	}

	protected void leftrot(BSTNode v) {
		BSTNode u = v.parent;
		if (u.isRoot()) {
			root = v;
			v.parent = null;
		} else {
			if (u.isLeft()) {
				u.parent.linkleft(v);
			} else {
				u.parent.linkright(v);
			}
		}
		if (v.left == null) {
			u.right = null;
		} else {
			u.linkright(v.left);
		}
		v.linkleft(u);
	}

	protected void rightrot(BSTNode v) {
		BSTNode u = v.parent;
		if (u.isRoot()) {
			root = v;
			v.parent = null;
		} else {
			if (u.isLeft()) {
				u.parent.linkleft(v);
			} else {
				u.parent.linkright(v);
			}
		}
		if (v.right == null) {
			u.left = null;
		} else {
			u.linkleft(v.right);
		}
		v.linkright(u);
	}

	public void rotate(BSTNode v) {
		if (v.isLeft()) {
			rightrot(v);
		} else {
			leftrot(v);
		}
		reposition();
		if (v.left != null) {
			v.left.calc();
		}
		if (v.right != null) {
			v.right.calc();
		}
		v.calc();
	}

	public void reposition() {
		if (root != null) {
			root._reposition();
			M.S.V.setBounds(x1, y1, x2, y2);
			//System.out.println(x1+" "+y1+" "+x2+" "+y2);
		}
	}
}
