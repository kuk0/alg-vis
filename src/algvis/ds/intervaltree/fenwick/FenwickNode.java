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
	public int storedValue;

	private FenwickNode(DataStructure D, FenwickNodeType type, int idx,
			int rangeMin, int rangeMax, int realValue) {
		super(D, 47, ZDepth.ACTIONNODE); // Key is never used

		this.type = type;
		this.idx = idx;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		this.realValue = realValue;
		this.storedValue = 0;
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

	/**
	 * Insert new value into tree but don't update values
	 * 
	 * @param x
	 *            Value to store
	 * @return Inserted node
	 */
	public FenwickNode insertOnly(int x) {
		if (type == FenwickNodeType.Leaf) {
			return null; // TODO error message / exception
		}

		if (type == FenwickNodeType.EmptyLeaf) {
			this.realValue = x;
			type = FenwickNodeType.Leaf;

			return this;
		}

		// Put in the left-most empty node (leaf)
		if (!getLeft().isFull()) {
			return getLeft().insertOnly(x);
		} else {
			return getRight().insertOnly(x);
		}
	}

	public void insert(int x) {
		FenwickNode node = insertOnly(x);
		node.updateStoredValue(x);
	}

	public void updateStoredValueStep(int dx) {
		storedValue += dx;
	}

	public void updateStoredValue(int dx) {
		updateStoredValueStep(dx);
		if (getParent() != null) {
			getParent().updateStoredValue(dx);
		}
	}

	public int getStoredValue() {
		if ((type == FenwickNodeType.Leaf || type == FenwickNodeType.EmptyLeaf)
				&& idx % 2 == 1) {
			// Odd leaves store just their value
			return storedValue;
		}

		if (type == FenwickNodeType.Node) {
			// First "real" parent node is also the one that has a path to the
			// desired leaf using only right edges
			return storedValue;
		}

		// For fake nodes return sum of children
		int sum = 0;
		if (getLeft() != null) sum += getLeft().getStoredValue();
		if (getRight() != null) sum += getRight().getStoredValue();
		
		return sum;		
	}

	@Override
	protected void drawKey(View v) {
		v.setColor(getFgColor());
		// TODO isnode/isleaf/...
		if (type == FenwickNodeType.Node || type == FenwickNodeType.FakeNode) {
			// stored value inside the node,
			// sum of stored values for fake node
			v.drawString(Integer.toString(getStoredValue()), x, y, Fonts.NORMAL);

			// TODO better positioning
			// range labels
			v.drawStringLeft(Integer.toString(rangeMin), x - Node.RADIUS, y
					- Node.RADIUS - Fonts.SMALL.fm.getHeight() / 2, Fonts.SMALL);
			v.drawStringRight(Integer.toString(rangeMax), x + Node.RADIUS, y
					- Node.RADIUS - Fonts.SMALL.fm.getHeight() / 2, Fonts.SMALL);
		} else {

			// real value
			if (type == FenwickNodeType.Leaf) {
				v.drawString(Integer.toString(realValue), x, y, Fonts.NORMAL);
			}

			// idx
			v.drawString(Integer.toString(idx), x, y + 2 * rh, Fonts.SMALL);

			if (idx == 1) {
				drawLabels(v);
			}
		}
	}

	// TODO move?
	static final int labelPadding = 8;

	private void drawLabels(View v) {
		v.setColor(getFgColor());
		double rx = x - labelPadding - rw;

		// TODO not aligned properly, bug in drawString?
		v.drawStringLeft("Real value:", rx, y, Fonts.NORMAL);
		v.drawStringLeft("idx:", rx, y + 2 * rh, Fonts.SMALL);
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
				v.setColor(Color.LIGHT_GRAY);
			}
			v.fillRoundRectangle(x, y, getRangeWidth(), Node.RADIUS,
					Node.RADIUS * 2, Node.RADIUS * 2);
			v.setColor(getFgColor());
			v.drawRoundRectangle(x, y, getRangeWidth(), Node.RADIUS,
					Node.RADIUS * 2, Node.RADIUS * 2);
			if (marked) {
				// TODO better marking
				v.drawRoundRectangle(x, y, getRangeWidth(), Node.RADIUS,
						Node.RADIUS * 2 + 4, Node.RADIUS * 2 + 4);
			}
		} else {

			if (idx % 2 == 0) {
				// Even leaves never have stored value
				v.setColor(Color.LIGHT_GRAY);
			} else {
				v.setColor(getBgColor());
			}

			v.fillRect(x, y, rw, rh);
			v.setColor(getFgColor());
			v.drawRectangle(new Rectangle2D.Double(x - rw, y - rh, rw * 2,
					rh * 2));
			if (marked) {
				// TODO better marking
				v.drawRectangle(new Rectangle2D.Double(x - rw + 2, y - rh + 2,
						rw * 2 - 4, rh * 2 - 4));
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
	
	public int countLeaves() {
		if (type == FenwickNodeType.Leaf) {
			return 1;
		}
		
		if (type == FenwickNodeType.EmptyLeaf) {
			return 0;
		}
		
		int sum = 0;
		if (getLeft() != null) sum += getLeft().countLeaves();
		if (getRight() != null) sum += getRight().countLeaves();
		
		return sum;	
	}
	
	public FenwickNode findByIdx(int idx) {
		if (type == FenwickNodeType.EmptyLeaf || type == FenwickNodeType.Leaf) {
			if (this.idx == idx) {
				return this;
			}
			
			return null;
		}
		
		// Try left/right children
		FenwickNode left = getLeft() != null ? getLeft().findByIdx(idx) : null;
		if (left != null) {
			return left;
		}
		return getRight() != null ? getRight().findByIdx(idx) : null;
	}
	
	/**
	 * Returns true for even-idx leaves and fake nodes,
	 * these don't have real value stored in them
	 * @return
	 */
	public boolean isEvenOrFake() {
		if (type == FenwickNodeType.FakeNode) {
			return true;
		}
		if (type == FenwickNodeType.Node) {
			return false;
		}
		return (idx % 2 == 0);
	}
	
}
