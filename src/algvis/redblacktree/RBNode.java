package algvis.redblacktree;

import algvis.bst.BSTNode;
import algvis.core.NodeColor;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;
import algvis.scenario.commands.rbnode.SetRedCommand;

public class RBNode extends BSTNode {
	private boolean red = true;

	public RBNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public RBNode(DataStructure D, int key) {
		super(D, key);
	}

	public boolean isRed() {
		return red;
	}

	public void setRed(boolean red) {
		if (this.red != red) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetRedCommand(this, red));
			}
			this.red = red;
		}
	}

	@Override
	public RBNode getLeft() {
		return (RBNode) super.getLeft();
	}

	public RBNode getLeft2() {
		RBNode r = getLeft();
		if (r == null)
			return ((RB) D).NULL;
		else
			return r;
	}

	@Override
	public RBNode getRight() {
		return (RBNode) super.getRight();
	}

	public RBNode getRight2() {
		RBNode r = getRight();
		if (r == null)
			return ((RB) D).NULL;
		else
			return r;
	}

	@Override
	public RBNode getParent() {
		return (RBNode) super.getParent();
	}

	public RBNode getParent2() {
		RBNode p = getParent();
		if (p == null)
			return ((RB) D).NULL;
		else
			return p;
	}

	@Override
	public void draw(View v) {
		if (state == Node.INVISIBLE || key == NULL) {
			return;
		}
		boolean a = D.scenario.isAddingEnabled();
		D.scenario.enableAdding(false);
		setColor(isRed() ? NodeColor.RED : NodeColor.BLACK);
		D.scenario.enableAdding(a);
		super.draw(v);
	}

	public void drawBigNodes(View v) {
		if (key == NULL) {
			return;
		}
		if (getLeft() != null) {
			getLeft().drawBigNodes(v);
		}
		if (getRight() != null) {
			getRight().drawBigNodes(v);
		}
		if (isRed() && getParent() != null) {
			v.drawWideLine(x, y, getParent().x, getParent().y);
		} else {
			v.drawWideLine(x - 1, y, x + 1, y);
		}
	}

	public void drawRBTree(View v) {
		if (((RB) D).mode24) {
			drawBigNodes(v);
		}
		drawTree(v);
	}
}
