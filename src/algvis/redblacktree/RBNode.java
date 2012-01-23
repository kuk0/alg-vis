package algvis.redblacktree;

import java.awt.Color;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;

public class RBNode extends BSTNode {
	boolean red = true;

	public RBNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public RBNode(DataStructure D, int key) {
		super(D, key);
	}

	@Override
	public RBNode getLeft() {
		return (RBNode) super.getLeft();
	}

	@Override
	public RBNode getRight() {
		return (RBNode) super.getRight();
	}

	@Override
	public RBNode getParent() {
		return (RBNode) super.getParent();
	}

	public RBNode getParent2() {
		RBNode p = getParent();
		if (p == null) return ((RB)D).NULL;
		else return p;
	}

	@Override
	public void draw(View v) {
		if (state == Node.INVISIBLE || key == NULL) {
			return;
		}
		bgColor(red ? Color.red : Color.black);
		fgColor(red ? Color.black : Color.white);
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
		if (red && getParent() != null) {
			v.drawWideLine(x, y, getParent().x, getParent().y);
		} else {
			v.drawWideLine(x - 1, y, x + 1, y);
		}
	}

	public void drawTree2(View v) {
		if (((RB) D).mode24) {
			drawBigNodes(v);
		}
		drawTree(v);
	}
}
