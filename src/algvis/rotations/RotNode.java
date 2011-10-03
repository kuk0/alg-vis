package algvis.rotations;

import java.awt.Color;
import java.awt.Graphics;

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

	public void linkLeft(BSTNode v) {
		oldleft = left;
		super.linkleft(v);
	}

	public void linkRight(BSTNode v) {
		oldright = right;
		super.linkright(v);
	}

	public void removeOldEdges() {
		oldleft = oldright = null;
	}

	@Override
	public void drawTree(Graphics g, View v) {
		g.setColor(Color.black);
		if (oldleft != null) {
			v.drawLine(g, x, y, oldleft.x, oldleft.y);
		} else if (left != null) {
			v.drawLine(g, x, y, left.x, left.y);
		}
		if (oldright != null) {
			v.drawLine(g, x, y, oldright.x, oldright.y);
		} else if (right != null) {
			v.drawLine(g, x, y, right.x, right.y);
		}
		if (left != null) {
			((RotNode) left).drawTree(g, v);
		}
		if (right != null) {
			((RotNode) right).drawTree(g, v);
		}
		draw(g, v);
	}

}
