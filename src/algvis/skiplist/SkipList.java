package algvis.skiplist;

import algvis.core.Dictionary;
import algvis.core.Node;
import algvis.core.View;
import algvis.core.VisPanel;

public class SkipList extends Dictionary {
	public static String dsName = "skiplist";
	SkipNode root, sent, v = null;
	int height = 1, n = 0, e = 0;

	public SkipList(VisPanel M) {
		super(M);
		root = new SkipNode(this, -Node.INF);
		root.linkright(sent = new SkipNode(this, Node.INF));
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
		height = 1;
		n = e = 0;
		root = new SkipNode(this, -Node.INF);
		root.linkright(sent = new SkipNode(this, Node.INF));
		reposition();
		setStats();
	}

	@Override
	public String stats() {
		if (root == null) {
			return M.L.getString("size") + ": 0;   " + M.L.getString("height")
					+ ": 0;   #" + M.L.getString("excess") + ": 0";
		} else {
			return M.L.getString("size") + ": " + n + ";   "
					+ M.L.getString("height") + ": " + height + ";   #"
					+ M.L.getString("excess") + ": " + e;
		}
	}

	@Override
	public void draw(View V) {
		if (root != null) {
			root.moveSkipList();
			root.drawSkipList(V);
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
	}

	public void reposition() {
		x1 = 0;
		y1 = 0;
		root._reposition();
		M.S.V.setBounds(x1, y1, x2, y2);
	}
}
