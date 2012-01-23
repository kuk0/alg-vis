package algvis.treap;

import java.awt.Color;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;

public class TreapNode extends BSTNode {
	// public TreapNode left=null, right=null, parent=null;
	double p;

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

	public void linkleft(TreapNode v) {
		setLeft(v);
		if (v != null) {
			v.setParent(this);
		}
	}

	public void linkright(TreapNode v) {
		setRight(v);
		if (v != null) {
			v.setParent(this);
		}
	}
}
