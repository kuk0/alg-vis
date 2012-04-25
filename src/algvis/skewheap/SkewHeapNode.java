package algvis.skewheap;

import java.awt.Color;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.MeldablePQ;
import algvis.core.Node;
import algvis.core.View;

public class SkewHeapNode extends BSTNode {
	Color color = Color.yellow;
	int height = 1;
	//int rank = 1;   //<----<<
	boolean doubleArrow = false;
	boolean dashedrightl = false; // if true the line leading to the right son is dashed
	boolean dashedleftl = false;  // if true the line leading to the left son is dashed

	public SkewHeapNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		bgKeyColor();
	}

	public SkewHeapNode(DataStructure D, int key) {
		super(D, key);
		bgKeyColor();
	}

	public SkewHeapNode(SkewHeapNode v) {
		this(v.D, v.getKey(), v.x, v.y);
	}
	
	public boolean prec(Node v) {
		if (((MeldablePQ) D).minHeap) {
			return this.getKey() < v.getKey();
		} else {
			return this.getKey() > v.getKey();
		}
	}

	public boolean preceq(Node v) {
		if (((MeldablePQ) D).minHeap) {
			return this.getKey() <= v.getKey();
		} else {
			return this.getKey() >= v.getKey();
		}
	}

	public void linkup(SkewHeapNode v) {
		if ((this.getParent() != null) && (v != null)) {
			SkewHeapNode tmp = this.getParent();
			v.setRight(this);
			this.setParent(v);
			v.setParent(tmp);
			v.getParent().setRight(v);
		}
	}

	public void swapChildren() {
		SkewHeapNode tmp = this.getLeft();
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
		v.drawDoubleArrow(x1 + 2 * SkewHeapNode.radius, y1, x2 - 2 * SkewHeapNode.radius, y2);
	}

	@Override
	public void draw(View v) {
		super.draw(v);
		drawDoubleArrow(v);
	}

	public void repos(int px, int py) {
		this.goTo(px, py);

		if (this.getRight() != null) {
			this.getRight().repos(px + getRight().leftw,
					py + (SkewHeap.minsepy));// + 2 * SkewHeapNode.radius));
		}
		if (this.getLeft() != null) {
			this.getLeft().repos(px - getLeft().rightw,
					py + (SkewHeap.minsepy));// + 2 * SkewHeapNode.radius));
		}
	}

	private void lowlight() {
		bgColor(new Color(200, 200 - getKey() / 10, 0));
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

			
			//if (thread) { v.setColor(Color.red); } else {			
			v.setColor(Color.black);
			//}

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
	public SkewHeapNode getRight() {
		return (SkewHeapNode) super.getRight();
	}

	public void setRight(SkewHeapNode v) {
		super.setRight((BSTNode) v);
	}

	@Override
	public SkewHeapNode getLeft() {
		return (SkewHeapNode) super.getLeft();
	}

	public void setLeft(SkewHeapNode v) {
		super.setLeft((BSTNode) v);
	}

	@Override
	public SkewHeapNode getParent() {
		return (SkewHeapNode) super.getParent();
	}

	public void setParent(SkewHeapNode v) {
		super.setParent((BSTNode) v);
	}



}
