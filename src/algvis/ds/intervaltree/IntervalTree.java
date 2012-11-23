package algvis.ds.intervaltree;

import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.core.visual.ZDepth;
import algvis.ds.intervaltree.IntervalNode.focusType;
import algvis.ui.VisPanel;
import algvis.ui.view.ClickListener;
import algvis.ui.view.View;

public class IntervalTree extends IntervalTrees implements ClickListener {
	public static String dsName = "intervaltree";
	IntervalNode root = null;
	int numLeafs = 0; // pocet obsadenych listov
	public static final int minsepx = 22;

	public IntervalTree(VisPanel M) {
		super(M);
		M.screen.V.setDS(this);
	}

	@Override
	public String getName() {
		return "intervaltree";
	}

	@Override
	public String stats() {
		return "";
	}

	@Override
	public void insert(int x) {
		start(new IntervalInsert(this, x));
	}

	@Override
	public void clear() {
		root = null;
		numLeafs = 0;
	}

	@Override
	public void draw(View V) {
		if (getRoot() != null) {
			getRoot().drawTree(V);
		}
	}

	@Override
	protected void move() {
		if (getRoot() != null) {
			getRoot().moveTree();
		}
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return root == null ? null : root.getBoundingBox();
	}

	@Override
	protected void endAnimation() {
		if (root != null) {
			root.endAnimation();
		}
	}

	@Override
	protected boolean isAnimationDone() {
		return root == null || root.isAnimationDone();
	}

	@Override
	public void mouseClicked(int x, int y) {
		// System.out.println("bolo kliknute");
		if (root == null) {
			return;
		}
		final IntervalNode v = root.find(x, y);
		if (v != null && v.isLeaf()) {
			if (v.marked) {
				v.unmark();
				chosen = null;
			} else {
				if (chosen != null) {
					chosen.unmark();
				}
				v.mark();
				chosen = v;
				// System.out.println(v.key + " tento je vybraty");
			}
		}

	}

	@Override
	public void changeKey(Node v, int value) {
		if (v == null) {
			// TODO: vypindat
		} else {
			start(new IntervalChangeKey(this, (IntervalNode) v, value));
		}

	}

	@Override
	public void ofinterval(int b, int e) {
		start(new IntervalFindMin(this, b, e));
		// start(new IntervalInsert(this, b));
	}

	IntervalNode getRoot() {
		return root;
	}

	public IntervalNode setRoot(IntervalNode root) {
		this.root = root;
		return this.root;
	}

	public void reposition() {
		x1 = x2 = y1 = y2 = 0;
		if (getRoot() != null) {
			getRoot().reposition();
		}
		panel.screen.V.setBounds(x1, y1, x2, y2);
	}

	int getHeight() {
		int tmp = numLeafs;
		int res = 1;
		while (tmp > 1) {
			tmp /= 2;
			res++;
		}
		if (numLeafs > 0) {
			return res;
		} else {
			return 0;
		}
	}

	private int numL;

	IntervalNode generateEmpty(int h) {
		final IntervalNode w = new IntervalNode(this, Node.NOKEY, ZDepth.NODE);
		if (h > 0) {
			final IntervalNode tmp1 = generateEmpty(h - 1);
			final IntervalNode tmp2 = generateEmpty(h - 1);
			w.setLeft(tmp1);
			tmp1.setParent(w);
			w.setRight(tmp2);
			tmp2.setParent(w);
			w.setInterval(tmp1.b, tmp2.e);
		} else {
			w.setInterval(numL, numL);
			numL++;
		}

		return w;

	}

	public void extend() {
		final IntervalNode w = new IntervalNode(this, 0, ZDepth.NODE); // pre suctovy
																		// strom je
																		// 0,
		// min je +inf, max je -inf
		w.setKey(Node.NOKEY);
		final IntervalNode w2 = root;
		w.setLeft(w2);
		w2.setParent(w);
		root = w;

		numL = numLeafs + 1;
		final IntervalNode tmp = generateEmpty(getHeight() - 1);
		root.setRight(tmp);
		tmp.setParent(root);
		// System.out.println(this.getHeight());
		reposition();
		// root.add();

	}

	public int getMinsepx() {
		return IntervalTree.minsepx;
	}

	public void unfocus(IntervalNode w) {
		if (w == null) {
			return;
		}
		w.focused = focusType.FALSE;
		w.unmark();
		w.unmarkColor();
		unfocus(w.getLeft());
		unfocus(w.getRight());
	}

	public void markColor(IntervalNode w, int i, int j) {
		if (w == null) {
			return;
		}
		if (w.isLeaf() && w.b >= i && w.e <= j) {
			w.markColor();
		}
		markColor(w.getLeft(), i, j);
		markColor(w.getRight(), i, j);
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "root", root);
		HashtableStoreSupport.store(state, hash + "numLeafs", numLeafs);
		if (root != null) {
			root.storeState(state);
		}
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		final Object root = state.get(hash + "root");
		if (root != null) {
			this.root = (IntervalNode) HashtableStoreSupport.restore(root);
		}
		final Object numLeafs = state.get(hash + "numLeafs");
		if (numLeafs != null) {
			this.numLeafs = (Integer) HashtableStoreSupport.restore(numLeafs);
		}

		if (this.root != null) {
			this.root.restoreState(state);
		}
	}
}
