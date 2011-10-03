package algvis;

public class BSTInsert extends Algorithm {
	BST T;
	BSTNode v;
	int K;

	public BSTInsert(BST T, int x) {
		super(T.M);
		this.T = T;
		v = T.v = new BSTNode(T, K = x);
		v.bgColor(Node.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			T.root = v;
			v.goToRoot();
			setText("newroot");
		} else {
			BSTNode w = T.root;
			v.goAboveRoot();
			setText("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					setText("alreadythere");
					v.goDown();
					v.bgColor(Node.NOTFOUND);
					return;
				} else if (w.key < K) {
					if (w.right == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(w.right);
					}
					setText("bstinsertright", K, w.key);
					mysuspend();
					v.noArrow();
					if (w.right != null) {
						w = w.right;
					} else {
						w.linkright(v);
						break;
					}
				} else {
					if (w.left == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(w.left);
					}
					setText("bstinsertleft", K, w.key);
					mysuspend();
					v.noArrow();
					if (w.left != null) {
						w = w.left;
					} else {
						w.linkleft(v);
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}
		}
		T.reposition();
		mysuspend();
		setText("done");
		v.bgColor(Node.NORMAL);
		T.v = null;
	}
}
