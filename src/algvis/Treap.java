package algvis;

public class Treap extends BST {
	public Treap(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new TreapInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new BSTFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new TreapDelete(this, x));
	}

	@Override
	public void clear() {
		root = null;
		setStats();
	}
}
