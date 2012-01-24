package algvis.bst;

import algvis.core.Commentary.State;
import algvis.core.Dictionary;
import algvis.core.LayoutListener;
import algvis.core.StringUtils;
import algvis.core.View;
import algvis.core.VisPanel;
import algvis.scenario.commands.SetCommentaryStateCommand;
import algvis.scenario.commands.bstnode.SetBSTNodeVCommand;
import algvis.scenario.commands.bstnode.SetBSTRootCommand;
import algvis.scenario.commands.node.Wait4NodeCommand;

public class BST extends Dictionary implements LayoutListener { //, ClickListener {
	public static String dsName = "bst";
	public BSTNode root = null, v = null;
	boolean order = false;

	@Override
	public String getName() {
		return "bst";
	}

	public BST(VisPanel M) {
		super(M, dsName);
		scenario.enable(true);
		//M.screen.V.setDS(this);
	}

	public BSTNode setNodeV(BSTNode v) {
		if (this.v != v) {
			scenario.add(new SetBSTNodeVCommand(this, v, this.v));
			this.v = v;
		}
		if (v != null) {
			scenario.add(new Wait4NodeCommand(v));
		}
		return v;
	}

	public BSTNode setRoot(BSTNode root) {
		if (this.root != root) {
			scenario.add(new SetBSTRootCommand(this, root, this.root));
			this.root = root;
		}
		return root;
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
		if (root != null || v != null) {
			scenario.addingNextStep();
			setRoot(null);
			setNodeV(null);
			State commState = M.C.getState();
			M.C.clear();
			scenario.add(new SetCommentaryStateCommand(M.C, commState));
			setStats();
			M.screen.V.resetView();
		}
	}

	@Override
	public String stats() {
		if (root == null) {
			return M.S.L.getString("size") + ": 0;   "
					+ M.S.L.getString("height") + ": 0 =  1.00\u00b7"
					+ M.S.L.getString("opt") + ";   "
					+ M.S.L.getString("avedepth") + ": 0";
		} else {
			root.calcTree();
			return M.S.L.getString("size")
					+ ": "
					+ root.size
					+ ";   "
					+ M.S.L.getString("height")
					+ ": "
					+ root.height
					+ " = "
					+ StringUtils
							.format(root.height
									/ (Math.floor(lg(root.size)) + 1), 2, 5)
					+ "\u00b7" + M.S.L.getString("opt") + ";   "
					+ M.S.L.getString("avedepth") + ": "
					+ StringUtils.format(root.sumh / (double) root.size, 2, -5);
		}
	}

	/**
	 * Move and draw all the objects on the scene (in this case the BST and one
	 * auxiliary node v).
	 */
	@Override
	public void draw(View V) {
		if (root != null) {
			BSTNode.i = 0;
			root.moveTree();
			root.drawTree(V);
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
	}

	protected void leftrot(BSTNode v) {
		BSTNode u = v.getParent();
		if (v.getLeft() == null) {
			u.unlinkRight();
		} else {
			u.linkRight(v.getLeft());
		}
		if (u.isRoot()) {
			setRoot(v);
		} else {
			if (u.isLeft()) {
				u.getParent().linkLeft(v);
			} else {
				u.getParent().linkRight(v);
			}
		}
		v.linkLeft(u);
	}

	protected void rightrot(BSTNode v) {
		BSTNode u = v.getParent();
		if (v.getRight() == null) {
			u.unlinkLeft();
		} else {
			u.linkLeft(v.getRight());
		}
		if (u.isRoot()) {
			setRoot(v);
		} else {
			if (u.isLeft()) {
				u.getParent().linkLeft(v);
			} else {
				u.getParent().linkRight(v);
			}
		}
		v.linkRight(u);
	}

	/**
	 * Rotation is specified by a single vertex v; if v is a left child of its
	 * parent, rotate right, if it is a right child, rotate left. This method
	 * also recalculates positions of all nodes and their statistics.
	 */
	public void rotate(BSTNode v) {
		if (v.isLeft()) {
			rightrot(v);
		} else {
			leftrot(v);
		}
		reposition();
		if (v.getLeft() != null) {
			v.getLeft().calc();
		}
		if (v.getRight() != null) {
			v.getRight().calc();
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
			M.screen.V.setBounds(x1, y1, x2, y2);
		}
	}

	@Override
	public void changeLayout() {
		reposition();
	}
	
	/*
	public void mouseClicked(int x, int y) {
		if (root != null) {
			BSTNode w = root.find(x, y);
			if (w != null) {
				w.markSubtree = !w.markSubtree;
			}
		}
	}*/
}
