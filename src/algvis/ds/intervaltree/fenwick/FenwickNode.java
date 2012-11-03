package algvis.ds.intervaltree.fenwick;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

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
			// idx
			v.drawString(Integer.toString(idx), x, y, Fonts.NORMAL);

			// stored value
			// TODO show stored value from this leaf or some parent

			// real value
			if (type == FenwickNodeType.Leaf) {
				v.drawString(Integer.toString(realValue), x, y + 4 * rh,
						Fonts.NORMAL);
			}
		}
	}

	// TODO move to FenwickTree ?
	static final double rw = 1.5 * Node.RADIUS + 1;
	static final double rh = Node.RADIUS;

	@Override
	protected void drawBg(View v) {
		if (type == FenwickNodeType.Node || type == FenwickNodeType.FakeNode) {
			if (type == FenwickNodeType.Node) {
				v.setColor(getBgColor());
			} else {
				// TODO configurable somewhere
				v.setColor(Color.WHITE);
			}
			v.fillRoundRectangle(x, y, getRangeWidth(), Node.RADIUS,
					Node.RADIUS * 2, Node.RADIUS * 2);
			v.setColor(getFgColor());
			v.drawRoundRectangle(x, y, getRangeWidth(), Node.RADIUS,
					Node.RADIUS * 2, Node.RADIUS * 2);
		} else {
			// 3 boxes - idx, stored value, real value
			for (int i = 0; i < 3; i++) {
				double rx = x;
				double ry = y + i * 2 * rh;

				v.setColor(getBgColor());
				v.fillRect(rx, ry, rw, rh);
				v.setColor(getFgColor());
				v.drawRectangle(new Rectangle2D.Double(rx - rw, ry - rh,
						rw * 2, rh * 2));
			}
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

	@Override
	protected void rebox() {
		// TODO: this should really be done in BSTNode with polymorphism
		leftw = (getLeft() == null) ? ((FenwickTree) D).getMinsepx() / 2
				: getLeft().leftw + getLeft().rightw;
		rightw = (getRight() == null) ? ((FenwickTree) D).getMinsepx() / 2
				: getRight().leftw + getRight().rightw;
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
