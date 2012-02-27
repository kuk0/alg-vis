package algvis.treap;

import java.awt.Color;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.NodeColor;

public class TreapNode extends BSTNode {
	final double p;

	public TreapNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		p = Math.random();
		bgPColor();
	}

	public TreapNode(DataStructure D, int key) {
		this(D, key, 0, 0);
		getReady();
	}

	@Override
	public TreapNode getLeft() {
		return (TreapNode) super.getLeft();
	}

	@Override
	public TreapNode getRight() {
		return (TreapNode) super.getRight();
	}

	@Override
	public TreapNode getParent() {
		return (TreapNode) super.getParent();
	}

	public void bgPColor() {
		bgColor(new Color(255, 255 - (int) Math.round(100 * p), 0));
	}

	@Override
	public void setColor(NodeColor color) {
		if (color == NodeColor.NORMAL) {
			bgPColor();
		} else {
			super.setColor(color);
		}
	}
}
