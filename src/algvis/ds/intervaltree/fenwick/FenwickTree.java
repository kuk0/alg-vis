package algvis.ds.intervaltree.fenwick;

import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;

import algvis.core.DataStructure;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

public class FenwickTree extends DataStructure {
	public static String adtName = "intervaltrees";
	public static String dsName = "fenwicktree";
	
	FenwickNode root = null;
	
	protected FenwickTree(VisPanel M) {
		super(M);
		//M.screen.V.setDS(this);
	}

	@Override
	public String getName() {
		return dsName;
	}

	@Override
	public String stats() {
		return "";
	}

	@Override
	public void insert(int x) {
		if (root == null)
		{
			root = FenwickNode.createEmptyLeaf(this, 1);
		}
	
		if (root.isFull())
		{
			extend();			
		}
		
		root.insert(x);
	}

	private void reposition() {
		if (root != null)
		{
			root.reposition();
			Rectangle2D bounds = root.getBoundingBox();
			panel.screen.V.setBounds((int)bounds.getMinX(), (int)bounds.getMinY(),
					(int)bounds.getMaxX(), (int)bounds.getMaxY());
		}
	}

	// TODO move to static method in fenwicknode / fenwickalgo
	private void extend() {
		FenwickNode r = FenwickNode.createNode(this, 1, root.idx*2);
		FenwickNode s = createEmptySubtree(root.idx+1, root.idx*2);
		
		r.setLeftWithParent(root);
		r.setRightWithParent(s);
		root = r;
		
		reposition();
	}
	
	private FenwickNode createEmptySubtree(int idxlo, int idxhi)
	{
		if (idxlo == idxhi)
		{
			return FenwickNode.createEmptyLeaf(this, idxlo);
		}
		// TODO node vs. fakenode
		FenwickNode n = FenwickNode.createNode(this, idxlo, idxhi);
		int midleft = (idxlo + idxhi)/2;
		
		n.setLeftWithParent(createEmptySubtree(idxlo, midleft));
		n.setRightWithParent(createEmptySubtree(midleft+1, idxhi));
		
		return n;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(View v) {
		if (root != null)
		{
			root.drawTree(v);
		}
	}

	@Override
	protected void move() throws ConcurrentModificationException {
		if (root != null)
		{
			root.moveTree();
		}
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return root == null ? null : root.getBoundingBox();
	}

}
