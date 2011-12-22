package algvis.bst;

import algvis.core.Dictionary;
import algvis.core.StringUtils;
import algvis.core.View;
import algvis.core.VisPanel;
import algvis.scenario.SetBSTNodeVCommand;
import algvis.scenario.SetBSTRootCommand;

public class BST extends Dictionary {
	public static String dsName = "bst";
	/*
	 * TODO this.v could be "movingNode" in DataStracture or rather
	 * DataStracture could have array of "movingNodes" (i.e. because of Heap),
	 * so then we need only one "SetMovingNodesCommand" for all DataStractures
	 * and no "SetBSTNodeVCommand", "SetHeapNodeVCommand",
	 * "SetHeapNodeV2Command",...
	 */
	public BSTNode root = null, v = null;

	public BST(VisPanel M) {
		super(M, dsName);
	}

	public BSTNode setNodeV(BSTNode v) {
		scenario.add(new SetBSTNodeVCommand(this, v, this.v));
		return this.v = v;
	}
	
	public BSTNode setRoot(BSTNode root) {
		scenario.add(new SetBSTRootCommand(this, root, this.root));
		return this.root = root;
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
		scenario.startMacro();
		setRoot(null);
		setNodeV(null);
		setStats();
		scenario.endMacro();
	}

	@Override
	public String stats() {
		if (root == null) {
			return M.L.getString("size") + ": 0;   " + M.L.getString("height")
					+ ": 0 =  1.00\u00b7" + M.L.getString("opt") + ";   "
					+ M.L.getString("avedepth") + ": 0";
		} else {
			root.calcTree();
			return M.L.getString("size")
					+ ": "
					+ root.size
					+ ";   "
					+ M.L.getString("height")
					+ ": "
					+ root.height
					+ " = "
					+ StringUtils.format(root.height / (Math.floor(lg(root.size)) + 1), 2,
							5) + "\u00b7" + M.L.getString("opt") + ";   "
					+ M.L.getString("avedepth") + ": "
					+ StringUtils.format(root.sumh / (double) root.size, 2, -5);
		}
	}

	/**
	 * Move and draw all the objects on the scene (in this case the BST and one auxilliary node v).
	 */
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

	protected void leftrot(BSTNode v) {
		BSTNode u = v.parent;
		if (v.left == null) {
			u.unlinkRight();
		} else {
			u.linkRight(v.left);
		}
		if (u.isRoot()) {
			setRoot(v);
			v.unsetParent();
		} else {
			if (u.isLeft()) {
				u.parent.linkLeft(v);
			} else {
				u.parent.linkRight(v);
			}
		}
		v.linkLeft(u);
	}

	protected void rightrot(BSTNode v) {
		BSTNode u = v.parent;
		if (v.right == null) {
			u.unlinkLeft();
		} else {
			u.linkLeft(v.right);
		}
		if (u.isRoot()) {
			setRoot(v);
			v.unsetParent();
		} else {
			if (u.isLeft()) {
				u.parent.linkLeft(v);
			} else {
				u.parent.linkRight(v);
			}
		}
		v.linkRight(u);
	}

	/**
	 * Rotation is specified by a single vertex v; if v is a left child of its parent,
	 * rotate right, if it is a right child, rotate left.
	 * This method also recalculates positions of all nodes and their statistics.
	 */
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

	/**
	 * Recalculate positions of all nodes in the tree.
	 */
	public void reposition() {
		if (root != null) {
			x1 = x2 = y1 = y2 = 0;
			root.reposition();
			M.S.V.setBounds(x1, y1, x2, y2);
			//System.out.println(x1+" "+y1+" "+x2+" "+y2);
		}
	}
}
