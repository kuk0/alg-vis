package algvis.leftistheap;

import java.awt.Color;
import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Fonts;
import algvis.core.MeldablePQ;
import algvis.visual.Node;
import algvis.visual.View;

public class LeftHeapNode extends BSTNode {
	Color color = Color.yellow;
	int height = 1;
	int rank = 1;
	boolean doubleArrow = false;
	boolean dashedrightl = false; // if true the line leading to the right son is dashed
	boolean dashedleftl = false; // if true the line leading to the left son is dashed

	public LeftHeapNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		bgKeyColor();
	}

	public LeftHeapNode(DataStructure D, int key) {
		super(D, key);
		bgKeyColor();
	}

	public LeftHeapNode(LeftHeapNode v) {
		this(v.D, v.key, v.x, v.y);
	}

	/**
	 * v.prec(w) iff v precedes w in the heap order, i.e., should be higher in
	 * the heap v precedes w if v.key < w.key when we have a min heap, but v
	 * precedes w if v.key > w.key when we have a max heap
	 */

	public boolean prec(Node v) {
		if (((MeldablePQ) D).minHeap) {
			return this.key < v.key;
		} else {
			return this.key > v.key;
		}
	}

	/**
	 * Precedes or equals (see prec).
	 */
	public boolean preceq(Node v) {
		if (((MeldablePQ) D).minHeap) {
			return this.key <= v.key;
		} else {
			return this.key >= v.key;
		}
	}

	public void linkup(LeftHeapNode v) {
		if ((this.getParent() != null) && (v != null)) {
			LeftHeapNode tmp = this.getParent();
			v.setRight(this);
			this.setParent(v);
			v.setParent(tmp);
			v.getParent().setRight(v);
		}
	}

	public void swapChildren() {
		LeftHeapNode tmp = this.getLeft();
		this.setLeft(this.getRight());
		this.setRight(tmp);
	}

	public void setDoubleArrow(Node w) {
		dir = w;
		doubleArrow = true;
	}

	public void noDoubleArrow() {
		doubleArrow = false;
	}

	private void drawDoubleArrow(View v) {
		if (!doubleArrow || dir == null) {
			return;
		}
		int x1, y1, x2, y2;
		if (x < dir.x) {
			x1 = x;
			y1 = y;
			x2 = dir.x;
			y2 = dir.y;
		} else {
			x2 = x;
			y2 = y;
			x1 = dir.x;
			y1 = dir.y;
		}
		v.drawDoubleArrow(x1 + 2 * LeftHeapNode.radius, y1, x2 - 2
				* LeftHeapNode.radius, y2);
	}

	@Override
	public void draw(View v) {
		super.draw(v);
		drawDoubleArrow(v);
		String str = new String("" + rank);
		if (rank != -1) {
			if (this.getParent() != null && this.getParent().getLeft() == this) {
				v.drawString(str, x - LeftHeapNode.radius, y
						- LeftHeapNode.radius, Fonts.SMALL);
			} else {
				v.drawString(str, x + LeftHeapNode.radius, y
						- LeftHeapNode.radius, Fonts.SMALL);
			}
		}
	}

	public void repos(int px, int py) {
		this.goTo(px, py);

		if (this.getRight() != null) {
			this.getRight().repos(px + getRight().leftw,
					py + (LeftHeap.minsepy));// + 2 * LeftHeapNode.radius));
		}
		if (this.getLeft() != null) {
			this.getLeft()
					.repos(px - getLeft().rightw, py + (LeftHeap.minsepy));// + 2 * LeftHeapNode.radius));
		}
	}

	private void lowlight() {
		bgColor(new Color(200, 200 - key / 10, 0));
	}

	private void highlight() {
		bgKeyColor();
	}

	public void lowlightTree() {
		lowlight();
		if (getLeft() != null) {
			getLeft().lowlightTree();
		}
		if (getRight() != null) {
			getRight().lowlightTree();
		}
	}

	public void highlightTree() {
		highlight();
		if (getLeft() != null) {
			getLeft().highlightTree();
		}
		if (getRight() != null) {
			getRight().highlightTree();
		}
	}

	@Override
	public void drawTree(View v) {

		if (this.state != INVISIBLE) {

			/*
			 * if (thread) { v.setColor(Color.red); } else {
			 */
			v.setColor(Color.black);
			// }

			if ((getLeft() != null) && (getLeft().state != INVISIBLE)) {
				if (dashedleftl) {
					v.drawDashedLine(x, y, getLeft().x, getLeft().y);
				} else {
					v.drawLine(x, y, getLeft().x, getLeft().y);
				}
			}
			if ((getRight() != null) && (getRight().state != INVISIBLE)) {
				if (dashedrightl) {
					v.drawDashedLine(x, y, getRight().x, getRight().y);
				} else {
					v.drawLine(x, y, getRight().x, getRight().y);
				}
			}
		}
		if (getLeft() != null) {
			getLeft().drawTree(v);
		}
		if (getRight() != null) {
			getRight().drawTree(v);
		}
		draw(v);
	}

	@Override
	public LeftHeapNode getRight() {
		return (LeftHeapNode) super.getRight();
	}

	public void setRight(LeftHeapNode v) {
		super.setRight((BSTNode) v);
	}

	@Override
	public LeftHeapNode getLeft() {
		return (LeftHeapNode) super.getLeft();
	}

	public void setLeft(LeftHeapNode v) {
		super.setLeft((BSTNode) v);
	}

	@Override
	public LeftHeapNode getParent() {
		return (LeftHeapNode) super.getParent();
	}

	public void setParent(LeftHeapNode v) {
		super.setParent((BSTNode) v);
	}

}
