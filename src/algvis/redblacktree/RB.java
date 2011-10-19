package algvis.redblacktree;

import java.awt.Graphics;

import algvis.bst.BST;
import algvis.bst.BSTNode;
import algvis.core.Node;
import algvis.core.StringUtils;
import algvis.core.View;
import algvis.core.VisPanel;

public class RB extends BST {
	public static String dsName = "redblack";
	RBNode NULL = new RBNode(this, Node.NULL);
	boolean mode24 = false;

	public RB(VisPanel M) {
		super(M);
		root = NULL.parent = NULL;
		NULL.red = false;
		NULL.size = NULL.height = NULL.sumh = 0;
	}

	@Override
	public void insert(int x) {
		start(new RBInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new RBFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new RBDelete(this, x));
	}

	@Override
	public void clear() {
		root = NULL.parent = NULL;
		setStats();
	}

	@Override
	public String stats() {
		if (root == NULL) {
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
		if (root != NULL) {
			root.moveTree();
			root.drawTree(G, V);
		}
		if (v != null) {
			v.move();
			v.draw(G, V);
		}
	}

	@Override
	protected void leftrot(BSTNode v) {
		BSTNode u = v.parent;
		if (u.isRoot()) {
			root = v;
			v.parent = NULL;
		} else {
			if (u.isLeft()) {
				u.parent.linkleft(v);
			} else {
				u.parent.linkright(v);
			}
		}
		u.linkright(v.left);
		v.linkleft(u);
	}

	@Override
	protected void rightrot(BSTNode v) {
		BSTNode u = v.parent;
		if (u.isRoot()) {
			root = v;
			v.parent = NULL;
		} else {
			if (u.isLeft()) {
				u.parent.linkleft(v);
			} else {
				u.parent.linkright(v);
			}
		}
		u.linkleft(v.right);
		v.linkright(u);
	}

	@Override
	public void rotate(BSTNode v) {
		if (v.isLeft()) {
			rightrot(v);
		} else {
			leftrot(v);
		}
		reposition();
		v.left.calc();
		v.right.calc();
		v.calc();
	}
}
