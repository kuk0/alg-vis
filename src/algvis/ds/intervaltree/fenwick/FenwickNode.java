package algvis.ds.intervaltree.fenwick;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.Fonts;
import algvis.ui.view.View;

public class FenwickNode extends BSTNode {

	public enum FenwickNodeType {
		Leaf, EmptyLeaf, Node, FakeNode,
	}

	public FenwickNodeType type;
	public int idx;
	public int rangeMin;
	public int rangeMax;
	public int realValue;

	private FenwickNode(DataStructure D, FenwickNodeType type, int idx,
			int rangeMin, int rangeMax, int realValue) {
		super(D, 47, ZDepth.ACTIONNODE); // Key is never used

		this.type = type;
		this.idx = idx;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		this.realValue = realValue;
	}

	public static FenwickNode createEmptyLeaf(DataStructure D, int idx) {
		return new FenwickNode(D, FenwickNodeType.EmptyLeaf, idx, 0, 0, 0);
	}

	public static FenwickNode createLeaf(DataStructure D, int idx, int value) {
		return new FenwickNode(D, FenwickNodeType.Leaf, idx, 0, 0, value);
	}

	public static FenwickNode createNode(DataStructure D, int rangeMin,
			int rangeMax) {
		return new FenwickNode(D, FenwickNodeType.Node, rangeMax, rangeMin,
				rangeMax, 0);
	}

	public static FenwickNode createFakeNode(DataStructure D, int rangeMin,
			int rangeMax) {
		return new FenwickNode(D, FenwickNodeType.FakeNode, rangeMax, rangeMin,
				rangeMax, 0);
	}

	public void insert(int x) {
		if (type == FenwickNodeType.EmptyLeaf) {
			this.realValue = x;
			type = FenwickNodeType.Leaf;
			return;
		}

		if (type == FenwickNodeType.Leaf) {
			return; // TODO error message / exception
		}

		// Put in the left-most empty node (leaf)
		if (!getLeft().isFull()) {
			getLeft().insert(x);
		} else {
			getRight().insert(x);
		}
	}

	@Override
	protected void drawKey(View v) {
		v.setColor(getFgColor());
		// TODO isnode/isleaf/...
		if (type == FenwickNodeType.Node || type == FenwickNodeType.FakeNode) {
			v.drawString(getRangeLabel(), x, y, Fonts.NORMAL);
		} else {
			v.drawString(Integer.toString(realValue), x, y, Fonts.NORMAL);
		}
	}

	@Override
	protected void drawBg(View v) {
		if (type == FenwickNodeType.Node || type == FenwickNodeType.FakeNode) {
			v.setColor(getBgColor());
			v.fillRoundRectangle(x, y, getRangeWidth(), Node.RADIUS,
					Node.RADIUS * 2, Node.RADIUS * 2);
			v.setColor(getFgColor());
			v.drawRoundRectangle(x, y, getRangeWidth(), Node.RADIUS,
					Node.RADIUS * 2, Node.RADIUS * 2);
		} else {

			super.drawBg(v);
		}
	}

	private int getRangeWidth() {
		return Math.max(Node.RADIUS,
				Fonts.NORMAL.fm.stringWidth(getRangeLabel()));
	}

	private String getRangeLabel() {
		return rangeMin + "-" + rangeMax;
	}

	private void alignSubtreeRight() {
		if (getLeft() != null) {
			getLeft().alignSubtreeRight();
		}

		if (getRight() != null) {
			getRight().alignSubtreeRight();
			this.tox = getRight().tox;
		}
	}

	@Override
	public void reposition() {
		super.reposition();
		alignSubtreeRight();
	}

	public boolean isFull() {
		if (type == FenwickNodeType.Leaf) {
			return true;
		}
		if (type == FenwickNodeType.EmptyLeaf) {
			return false;
		}

		if (getLeft() == null || !getLeft().isFull()) {
			return false;
		}
		if (getRight() == null || !getRight().isFull()) {
			return false;
		}

		return true;
	}

	@Override
	public FenwickNode getLeft() {
		return (FenwickNode) super.getLeft();
	}

	@Override
	public FenwickNode getRight() {
		return (FenwickNode) super.getRight();
	}

	@Override
	public FenwickNode getParent() {
		return (FenwickNode) super.getParent();
	}
}
