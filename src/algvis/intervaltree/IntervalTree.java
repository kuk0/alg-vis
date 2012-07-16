package algvis.intervaltree;

import algvis.core.Node;
import algvis.gui.VisPanel;
import algvis.gui.view.ClickListener;
import algvis.gui.view.View;
import algvis.intervaltree.IntervalNode.focusType;

public class IntervalTree extends IntervalTrees implements ClickListener {
	public static String dsName = "intervaltree";
	IntervalNode root = null;
    private IntervalNode v = null;
    private IntervalNode v2 = null;
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
		v = null;
		v2 = null;
		numLeafs = 0;
	}

	@Override
	public void draw(View V) {
		if (getRoot() != null) {
			getRoot().moveTree();
			getRoot().drawTree(V);
		}

		if (v != null) {
			v.move();
			v.draw(V);
		}
		if (v2 != null) {
			v2.move();
			v2.draw(V);
		}
	}

	@Override
	public void mouseClicked(int x, int y) {
		// System.out.println("bolo kliknute");
		if (root == null)
			return;
		IntervalNode v = root.find(x, y);
		if (v != null && v.isLeaf()) {
			if (v.marked) {
				v.unmark();
				chosen = null;
			} else {
				if (chosen != null)
					chosen.unmark();
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

	public IntervalNode getV() {
		return v;
	}

	public IntervalNode setV(IntervalNode v) {
		this.v = v;
		return v;
	}

	public void reposition() {
		x1 = x2 = y1 = y2 = 0;
		if (getRoot() != null) {
			getRoot().reposition();
		}
		M.screen.V.setBounds(x1, y1, x2, y2);
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
		IntervalNode w = new IntervalNode(this, Node.NOKEY);
		if (h > 0) {
			IntervalNode tmp1 = generateEmpty(h - 1);
			IntervalNode tmp2 = generateEmpty(h - 1);
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
		IntervalNode w = new IntervalNode(this, 0); // pre suctovy strom je 0,
													// min je +inf, max je -inf
		w.setKey(Node.NOKEY);
		IntervalNode w2 = root;
		w.setLeft(w2);
		w2.setParent(w);
		root = w;

		numL = numLeafs + 1;
		IntervalNode tmp = generateEmpty(getHeight() - 1);
		root.setRight(tmp);
		tmp.setParent(root);
		System.out.println(this.getHeight());
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

}
