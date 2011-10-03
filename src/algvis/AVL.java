package algvis;

public class AVL extends BST {
	public AVL(VisPanel M) {
		super(M);
	}

	@Override
	public void insert(int x) {
		start(new AVLInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new BSTFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new AVLDelete(this, x));
	}

	@Override
	public void clear() {
		root = null;
		setStats();
	}
}
