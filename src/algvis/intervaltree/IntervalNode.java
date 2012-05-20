package algvis.intervaltree;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Fonts;
import algvis.core.Node;
import algvis.core.View;

public class IntervalNode extends BSTNode{
	boolean leaf = true;
	int b=1, e=1; // zaciatok a koniec intervalu, ktory reprezentuje

	public IntervalNode(DataStructure D, int key) {
		super(D, key);
	}
	
	public IntervalNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		bgKeyColor();
	}

	public IntervalNode(IntervalNode v) {
		this(v.D, v.key, v.x, v.y);
	}
	
	@Override
	public void drawKey(View v) {
		v.setColor(getFgColor());
		if (key != NOKEY) {
			v.drawString(toString(), x, y, Fonts.NORMAL);
		}
		
		String str = new String("" + this.e);
		v.drawString(str, x + Node.radius, y - Node.radius, Fonts.SMALL);
		str = new String("" + this.b);
		v.drawString(str, x - Node.radius, y - Node.radius, Fonts.SMALL);
		
		
	}
	
	
	public boolean prec(IntervalNode v) {
		if (((IntervalTree) D).minTree) {
			return this.key < v.key;
		} else {
			return this.key > v.key;
		}
	}

	/**
	 * Precedes or equals (see prec).
	 */
	public boolean preceq(IntervalNode v) {
		if (((IntervalTree) D).minTree) {
			return this.key <= v.key;
		} else {
			return this.key >= v.key;
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

}
