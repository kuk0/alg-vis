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
	
	public FenwickNodeType type;
	public int idx;
	public int rangeMin;
	public int rangeMax;
	public int realValue;	
	
	private FenwickNode(DataStructure D, FenwickNodeType type, int idx, int rangeMin, int rangeMax, int realValue) {
		super(D, 0, ZDepth.ACTIONNODE);
		
		this.type = type;
		this.idx = idx;
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		this.realValue = realValue;
	}
	
	public static FenwickNode createEmptyLeaf(DataStructure D, int idx)
	{
		return new FenwickNode(D, FenwickNodeType.EmptyLeaf, idx, 0, 0, 0);
	}
	public static FenwickNode createLeaf(DataStructure D, int idx, int value)
	{
		return new FenwickNode(D, FenwickNodeType.Leaf, idx, 0, 0, value);
	}
	public static FenwickNode createNode(DataStructure D, int rangeMin, int rangeMax)
	{
		return new FenwickNode(D, FenwickNodeType.Node, rangeMax, rangeMin, rangeMax, 0);
	}
	public static FenwickNode createFakeNode(DataStructure D, int rangeMin, int rangeMax)
	{
		return new FenwickNode(D, FenwickNodeType.FakeNode, rangeMax, rangeMin, rangeMax, 0);
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
