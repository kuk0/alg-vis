package algvis;

import java.awt.Graphics;

public class SkipList extends Dictionary {
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
	public void clean() {
		v = null;
	}

	@Override
	public String stats() {
		if (root == null) {
			return M.a.getString("size") + ": 0;   " + M.a.getString("height")
					+ ": 0;   #" + M.a.getString("excess") + ": 0";
		} else {
			return M.a.getString("size") + ": " + n + ";   "
					+ M.a.getString("height") + ": " + height + ";   #"
					+ M.a.getString("excess") + ": " + e;
		}
	}

	@Override
	public void draw(Graphics G, View V) {
		if (root != null) {
			root.moveSkipList();
			root.drawSkipList(G, V);
		}
		if (v != null) {
			v.move();
			v.draw(G, V);
		}
	}

	public void reposition() {
		x1 = 0;
		y1 = 0;
		root._reposition();
		M.S.V.setBounds(x1, y1, x2, y2);
	}
}
