package algvis.ds.intervaltree.fenwick;

import java.awt.geom.Rectangle2D;

import algvis.core.DataStructure;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTNode;

public class FenwickNode extends BSTNode {

	public enum FenwickNodeType
	{
		Leaf,
		EmptyLeaf,
		Node,
		FakeNode,
	}
	
	public int idx;
	public FenwickNodeType type;
	
	public FenwickNode(DataStructure D, int idx, int value, FenwickNodeType type) {
		super(D, value, ZDepth.ACTIONNODE);
		this.idx = idx;
		this.type = type;
	}

	public void insert(int x) {
		if (type == FenwickNodeType.EmptyLeaf)
		{
			this.key = x;
			type = FenwickNodeType.Leaf;
			return;
		}
		
		if (type == FenwickNodeType.Leaf)
		{
			return; // TODO error message / exception
		}
		
		// Put in the left-most empty node (leaf)
		if (!getLeft().isFull())
		{
			getLeft().insert(x);
		}
		else
		{
			getRight().insert(x);
		}
	}
	
	public boolean isFull() {
		if (type == FenwickNodeType.Leaf)
		{
			return true;
		}
		if (type == FenwickNodeType.EmptyLeaf)
		{
			return false;
		}
		
		if (getLeft() == null || !getLeft().isFull())
		{
			return false;
		}
		if (getRight() == null || !getRight().isFull())
		{
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
	
	public void setLeftWithParent(FenwickNode n) {
		setLeft(n);
		if (n != null)
		{
			n.setParent(this);
		}
	}
	
	public void setRightWithParent(FenwickNode n) {
		setRight(n);
		if (n != null)
		{
			n.setParent(this);
		}
	}
	
}
