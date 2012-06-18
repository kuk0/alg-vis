package algvis.intervaltree;

import java.awt.Color;

import algvis.bst.BST;
import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.gui.Fonts;
import algvis.gui.view.View;
import algvis.intervaltree.IntervalTrees.mimasuType;

public class IntervalNode extends BSTNode{
	//boolean leaf = true;
	int b=1, e=1; // zaciatok a koniec intervalu, ktory reprezentuje
	public enum focusType {
	    FALSE, // ziadny obdlznik 
	    TIN, // zeleny obdlznik
	    TOUT, // cerveny obdlznik
	    TWAIT // iba obvod obdlznika
	}
	focusType focused;
	boolean markedColor = false;

	public IntervalNode(DataStructure D, int key) {
		super(D, key);
		focused = focusType.FALSE;
	}
	
	public IntervalNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		bgKeyColor();
	}

	public IntervalNode(IntervalNode v) {
		this(v.D, v.getKey(), v.x, v.y);
	}
	
	@Override
	public void drawKey(View v) {
		v.setColor(getFgColor());
		if (getKey() != NOKEY) {
			v.drawString(toString(), x, y, Fonts.NORMAL);
		}
		
		if (!isLeaf()){
			String str = new String("" + this.e);
			v.drawString(str, x + Node.radius, y - Node.radius, Fonts.SMALL);
			str = new String("" + this.b);
			v.drawString(str, x - Node.radius, y - Node.radius, Fonts.SMALL);
		} else {
			String str = new String("" + this.e);
			v.drawString(str, x, y + Node.radius + 5, Fonts.SMALL);
		}
	}
	

	public static final NodeColor TREE = new NodeColor(Color.BLACK, new Color(0xFDFF9A));//0xFEFFC3));
	public static final NodeColor EMPTY = new NodeColor(Color.BLACK, new Color(0xF0F0F0));
	@Override
	protected void drawBg(View v) {
		if (getKey() != NOKEY) {
			if (!isLeaf()){
				this.setColor(TREE);
			}
		} else {
			this.setColor(EMPTY);
		}
		
		if (!isLeaf()){
			v.setColor(this.getBgColor());
			v.fillCircle(x, y, Node.radius);
			v.setColor(this.getFgColor());
			v.drawCircle(x, y, Node.radius);
			if (marked) {
				v.drawCircle(x, y, Node.radius + 2);
			}
		} else {
			if (getKey() != NOKEY) {
				if (this.markedColor){
					this.setColor(IN);
				} else {
					this.setColor(NodeColor.NORMAL);
				}
			} else {
				this.setColor(EMPTY);
			}
			v.setColor(getBgColor());
			v.fillSqr(x, y, Node.radius + 1);// + IntervalTree.minsepx);
			v.setColor(Color.BLACK); // fgcolor);
			v.drawSqr(x, y, Node.radius + 1);// + IntervalTree.minsepx);
			//DOROBIT!!!
			if (marked) {
				v.drawSqr(x, y, Node.radius - 1);
			}
		}
	}
	
	int i;
	@Override
	public void drawTree(View v) {
		i = 0;
		drawTree2(v);
	}
	
	public static final NodeColor IN = new NodeColor(Color.BLACK, new Color(0xAAFF95));
	public static final NodeColor OUT = new NodeColor(Color.BLACK, new Color(0xFC9A79));
	public static final NodeColor WAIT = new NodeColor(Color.BLACK, new Color(0xFFFFFF));

	private void drawTree2(View v) {
		switch (focused) {
		case TIN:
			this.setColor(IN);
			break;
		case TOUT:
			this.setColor(OUT);
			break;
		case TWAIT:
			this.setColor(WAIT);
			break;
		case FALSE:
			break;
		}
		switch(focused){
		case TIN: case TOUT: case TWAIT:
			v.setColor(this.getFgColor());
			int c = (e - b + 1);
			int d = (int)(Math.log10(c)/Math.log10(2));
			System.out.println(d + " =vyska-1, minsepy= " +IntervalTree.minsepy);
			int width = (c)*IntervalTree.minsepx;
			int height = (d)*IntervalTree.minsepy + 4 + 2*Node.radius;
			v.drawRoundRectangle(x, y + height/2 - Node.radius - 3, width/2, height/2, 8, 8);
			v.setColor(this.getBgColor());
			v.fillRoundRectangle(x, y + height/2 - Node.radius - 3, width/2, height/2, 8, 8);
		default: break;
		}
		if (state != INVISIBLE && getParent() != null) {
			v.setColor(Color.black);
			v.drawLine(x, y, getParent().x, getParent().y);
		}
		if (getLeft() != null) {
			//System.out.println("kreslim lavy " + getLeft().key + " " + this.key);
			getLeft().drawTree2(v);
		}
		if (D instanceof BST && ((BST) D).order) { // && D.M.S.layout ==
													// Layout.SIMPLE
			v.setColor(Color.LIGHT_GRAY);
			++i;
			if (i%10 == 0) {
				v.drawLine(x, y, x, -22);
			} else {
				v.drawLine(x, y, x, -20);
			}
			if (i%10 == 0) {
				v.drawString("" + i, x, -29, Fonts.NORMAL);
			} else if (i%10 == 5) {
				v.drawString("5", x, -27, Fonts.NORMAL);
			} else {
				v.drawString("" + i%10, x, -27, Fonts.SMALL);
			}
		}
		if (getRight() != null) {
			getRight().drawTree2(v);
		}
		draw(v);
	}
	
	public void rebox() {
		/*
		 * if there is a left child, leftw = width of the box enclosing the
		 * whole left subtree, i.e., leftw+rightw; otherwise the width is the
		 * node radius plus some additional space called xspan
		 */
		leftw = (getLeft() == null) ? ((IntervalTree)D).getMinsepx() / 2
				: getLeft().leftw + getLeft().rightw;
		// rightw is computed analogically
		rightw = (getRight() == null) ? ((IntervalTree)D).getMinsepx() / 2
				: getRight().leftw + getRight().rightw;
	}
	
	
	public boolean prec(IntervalNode v) {
		if (((IntervalTree) D).minTree == mimasuType.MIN) {
			return getKey() < v.getKey();
		} else {
			return getKey() > v.getKey();
		}
	}

	/**
	 * Precedes or equals (see prec).
	 */
	public boolean preceq(IntervalNode v) {
		if (((IntervalTree) D).minTree == mimasuType.MIN) {
			return getKey() <= v.getKey();
		} else {
			return getKey() >= v.getKey();
		}
	}

	
	
	@Override
	public IntervalNode getRight() {
		return (IntervalNode) super.getRight();
	}
	
	public void setRight(IntervalNode v) {
		super.setRight((BSTNode) v);
	}

	@Override
	public IntervalNode getLeft() {
		return (IntervalNode) super.getLeft();
	}

	public void setLeft(IntervalNode v) {
		super.setLeft((BSTNode) v);
	}

	@Override
	public IntervalNode getParent() {
		return (IntervalNode) super.getParent();
	}

	public void setParent(IntervalNode v) {
		super.setParent((BSTNode) v);
	}
	
	public void setInterval(int i, int j){
		b = i;
		e = j;
	}
	
	public void markColor(){
		markedColor = true;
	}
	
	public void unmarkColor(){
		markedColor = false;
	}
	
	public IntervalNode find(int x, int y) {
		if (inside(x, y))
			return this;
		if (getLeft() != null) {
			IntervalNode tmp = getLeft().find(x, y);
			if (tmp != null)
				return tmp;
		}
		if (getRight() != null) {
			return getRight().find(x, y);
		}
		return null;
	}

}
