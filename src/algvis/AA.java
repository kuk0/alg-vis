package algvis;

public class AA extends BST {
	boolean mode23 = false;

	public AA(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new AAInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new BSTFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new AADelete(this, x));
	}

	@Override
	public void clear() {
		root = null;
		setStats();
	}

	public BSTNode skew(BSTNode w) {
		if (w.left != null && ((AANode) w.left).level == ((AANode) w).level) {
			w = w.left;
			rotate(w);
			reposition();
		}
		return w;
	}

	public BSTNode split(BSTNode w) {
		BSTNode r = w.right;
		if (r != null && r.right != null
				&& ((AANode) r.right).level == ((AANode) w).level) {
			w = r;
			rotate(w);
			((AANode) w).level++;
			reposition();
		}
		return w;
	}
}
