package algvis.ds.intervaltree.fenwick;

import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

public class FenwickTree extends DataStructure {
	public static String adtName = "intervaltrees";
	public static String dsName = "fenwicktree";

	public FenwickNode root = null;

	protected FenwickTree(VisPanel M) {
		super(M);
	}

	@Override
	public String getName() {
		return dsName;
	}

	@Override
	public String stats() {
		return "";
	}

	public int getMinsepx() {
		return Node.RADIUS * 3 + 2;
	}

	@Override
	public void insert(int x) {
		start(new FenwickInsert(this, x));
	}

	public void prefixSum(int idxmax) {
		// Range check
		idxmax = Math.max(1, Math.min(idxmax, root.rangeMax));
		start(new FenwickPrefixSum(this, idxmax));
	}

	private void reposition() {
		if (root != null) {
			root.reposition();
			Rectangle2D bounds = root.getBoundingBox();
			panel.screen.V.setBounds((int) bounds.getMinX(),
					(int) bounds.getMinY(), (int) bounds.getMaxX(),
					(int) bounds.getMaxY());
		}
	}

	public void extend() {
		FenwickNode r = FenwickNode.createNode(this, 1, root.idx * 2);
		r.linkLeft(root);
		r.updateStoredValue(root.getStoredValue());

		FenwickNode s = createEmptySubtree(root.idx + 1, root.idx * 2, true);
		r.linkRight(s);

		root = r;
		reposition();
	}

	private FenwickNode createEmptySubtree(int idxlo, int idxhi, boolean fake) {
		if (idxlo == idxhi) {
			return FenwickNode.createEmptyLeaf(this, idxlo);
		}

		FenwickNode n;
		if (fake) {
			n = FenwickNode.createFakeNode(this, idxlo, idxhi);
		} else {
			n = FenwickNode.createNode(this, idxlo, idxhi);
		}
		int midleft = (idxlo + idxhi) / 2;

		n.linkLeft(createEmptySubtree(idxlo, midleft, false));
		n.linkRight(createEmptySubtree(midleft + 1, idxhi, true));

		return n;
	}

	@Override
	public void clear() {
		// Start with a minimal tree (2 values with one parent)
		root = createEmptySubtree(1, 2, false);
		reposition();
	}

	@Override
	public void draw(View v) {
		if (root != null) {
			root.drawTree(v);
		}
	}

	@Override
	protected void move() throws ConcurrentModificationException {
		if (root != null) {
			root.moveTree();
		}
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return root == null ? null : root.getBoundingBox();
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "root", root);
		if (root != null) {
			root.storeState(state);
		}
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object root = state.get(hash + "root");
		if (root != null) {
			this.root = (FenwickNode) HashtableStoreSupport.restore(root);
		}

		if (this.root != null) {
			this.root.restoreState(state);
		}
	}

}
