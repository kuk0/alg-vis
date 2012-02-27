package algvis.skiplist;

import algvis.core.Alignment;
import algvis.core.ClickListener;
import algvis.core.Dictionary;
import algvis.core.Node;
import algvis.core.View;
import algvis.core.VisPanel;

public class SkipList extends Dictionary implements ClickListener {
	public static String dsName = "skiplist";
	SkipNode sent;
	int height = 1, n = 0, e = 0;

	@Override
	public String getName() {
		return "skiplist";
	}

	public SkipList(VisPanel M) {
		super(M);
		scenario.enable(true);
		M.screen.V.setDS(this);
		M.screen.V.align = Alignment.LEFT;
		setRoot(new SkipNode(this, -Node.INF));
		getRoot().linkright(sent = new SkipNode(this, Node.INF));
		reposition();
	}

	@Override
	public void insert(int x) {
		start(new SkipInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new SkipFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new SkipDelete(this, x));
	}

	@Override
	public void clear() {
		if (n != 0 || scenario.hasNext()) {
			scenario.newAlgorithm();
			scenario.newStep();
			height = 1;
			n = e = 0;
			setRoot(new SkipNode(this, -Node.INF));
			getRoot().linkright(sent = new SkipNode(this, Node.INF));
			setV(null);
			M.C.clear();
			setStats();
			reposition();
			// M.screen.V.resetView(); TODO toto bolo v BST.clear()
		}
	}

	@Override
	public String stats() {
		if (getRoot() == null) {
			return M.S.L.getString("size") + ": 0;   "
					+ M.S.L.getString("height") + ": 0;   #"
					+ M.S.L.getString("excess") + ": 0";
		} else {
			return M.S.L.getString("size") + ": " + n + ";   "
					+ M.S.L.getString("height") + ": " + height + ";   #"
					+ M.S.L.getString("excess") + ": " + e;
		}
	}

	@Override
	public void draw(View V) {
		if (getRoot() != null) {
			getRoot().moveSkipList();
			getRoot().drawSkipList(V);
		}
		if (getV() != null) {
			getV().move();
			getV().draw(V);
		}
	}

	public void reposition() {
		x1 = 0;
		y1 = 0;
		getRoot()._reposition();
		M.screen.V.setBounds(x1, y1, x2, y2);
	}

	public void mouseClicked(int x, int y) {
		if (getRoot() != null) {
			Node w = getRoot().find(x, y);
			if (w != null) {
				M.B.I.setText("" + w.key);
			}
		}
	}

	public SkipNode getRoot() {
		return (SkipNode) super.getRoot();
	}

	public SkipNode setRoot(SkipNode root) {
		super.setRoot(root);
		return root;
	}

	public SkipNode getV() {
		return (SkipNode) super.getV();
	}

	public void setV(SkipNode v) {
		super.setV(v);
	}
}
