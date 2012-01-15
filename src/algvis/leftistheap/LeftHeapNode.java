package algvis.leftistheap;

import java.awt.Color;
import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.MeldablePQ;
import algvis.core.View;

public class LeftHeapNode extends BSTNode {
	// LeftHeapNode left, right, parent;
	Color color = Color.yellow;
	int height = 1;
	int rank = 1;
	boolean doubleArrow = false;
	//mozno by bolo dobre spravit rightline a leftline globalnejsie, aby nemuselo cele drawTree byt prepisane 
	boolean rightline = true; // visibility of the line leading to the right son
	boolean leftline = true; // visibility of the line leading to the left son

	public LeftHeapNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		bgKeyColor();
	}

	public LeftHeapNode(DataStructure D, int key) {
		super(D, key);
		bgKeyColor();
		setState(Node.UP);
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

	// pripojit this ako noveho rodica v a v dat ako praveho syna
	public void linkup(LeftHeapNode v) {
		if ((this.getParent() != null) && (v != null)) {
			LeftHeapNode tmp = this.getParent();
			// this.parent = v;
			v.setRight(this);
			this.setParent(v);
			v.setParent(tmp);
			v.getParent().setRight(v);
		}
	}

	// vyhodi sa zo zoznamu
	public void unlink() {
		/*
		 * if (parent != null) { if (parent.child == this) { if (right == this)
		 * parent.child = null; else parent.child = right; } parent.rank--;
		 * parent = null; } left.right = right; right.left = left; left = right
		 * = this;
		 */
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
			x1 = x; y1 = y; x2 = dir.x; y2 = dir.y;
		} else {
			x2 = x; y2 = y; x1 = dir.x; y1 = dir.y;
		}
		v.drawDoubleArrow(x1+2*D.radius,y1,x2-2*D.radius,y2);
	}

	// docasne a mozno aj navzdy skopirovane z AAtree koli vykreslovaniu ranku
	@Override
	public void draw(View v) {
		super.draw(v);
		drawDoubleArrow(v);
		String str = new String("" + rank);
		if (this.getParent() != null && this.getParent().getLeft() == this) {
			v.drawString(str, x - D.radius, y - D.radius, 7);
		} else {
			v.drawString(str, x + D.radius, y - D.radius, 7);
		}

	}

	public void repos(int px, int py) { // , LeftHeapNode w) {
		this.goTo(px, py);
		// tox = px;
		// toy = py;

		if (this.getRight() != null) {
			this.getRight().repos(px + getRight().leftw, py
					+ (D.yspan + 2 * D.radius));
		}
		if (this.getLeft() != null) {
			this.getLeft().repos(px - getLeft().rightw, py
					+ (D.yspan + 2 * D.radius));
		}
	}

	// spravit nejake globalnejsie pre MPQ?
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
		// highlightTree(this);
	}

	
	@Override
	public void drawTree(View v) {
		
		if (this.state != INVISIBLE) {
			/*
			if (thread) {
				v.setColor(Color.red);
			} else { */
				v.setColor(Color.black);
			//} 
			
			if ((getLeft() != null) && (getLeft().state != INVISIBLE)) {
				if (leftline) {
					v.drawLine(x, y, getLeft().x, getLeft().y);
				} else {
					v.drawDashedLine(x, y, getLeft().x, getLeft().y);
				}				
			}
			if ((getRight() != null) && (getRight().state != INVISIBLE)) {
				if (rightline) {
					v.drawLine(x, y, getRight().x, getRight().y);
				} else {
					v.drawDashedLine(x, y, getRight().x, getRight().y);
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

	//@Override
	public LeftHeapNode getRight(){
		return (LeftHeapNode) this.right;
	}

	//@Override
	public LeftHeapNode getLeft(){
		return (LeftHeapNode) this.left;
	}

	//@Override
	public void setRight(LeftHeapNode v){
		this.right = v;
	}

	//@Override
	public void setLeft(LeftHeapNode v){
		this.left = v;
	}

	//@Override
	public LeftHeapNode getParent(){
		return (LeftHeapNode) this.parent;
	}

	//@Override
	public void setParent(LeftHeapNode v){
		this.parent = v;
	}

}
