package algvis.rotations;

import java.awt.Color;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.View;

public class RotNode extends BSTNode {
	BSTNode oldleft, oldright;

	public RotNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public RotNode(DataStructure D, int key) {
		super(D, key);
	}

	@Override
	public void linkLeft(BSTNode v) {
		oldleft = left;
		super.linkLeft(v);
	}

	@Override
	public void linkRight(BSTNode v) {
		oldright = right;
		super.linkRight(v);
	}

	public void removeOldEdges() {
		oldleft = oldright = null;
	}

	@Override
	public void drawTree(View v) {
		v.setColor(Color.black);
		if (oldleft != null) {
			v.drawLine(x, y, oldleft.x, oldleft.y);
		} else if (left != null) {
			v.drawLine(x, y, left.x, left.y);
		}
		if (oldright != null) {
			v.drawLine(x, y, oldright.x, oldright.y);
		} else if (right != null) {
			v.drawLine(x, y, right.x, right.y);
		}
		if (left != null) {
			((RotNode) left).drawTree(v);
		}
		if (right != null) {
			((RotNode) right).drawTree(v);
		}
		draw(v);
	}

}
