package algvis;

public class SplayFind extends SplayAlg {
	public SplayFind(Splay T, int x) {
		super(T, x);
		T.vv = v = new SplayNode(T, x);
		v.bgColor(Node.FIND);
	}

	@Override
	public void run() {
		if (T.root == null) {
			v.goToRoot();
			setText("bstfindempty");
			mysuspend();
			v.goDown();
			v.bgColor(Node.NOTFOUND);
			setHeader("search");
			setText("bstfindnotfound");
		} else {
			v.goAboveRoot();
			BSTNode w = find(K);
			splay(w);

			setText("splayinroot");
			mysuspend();

			setHeader("search");
			w.bgColor(Node.NORMAL);
			v.goToRoot();
			if (w.key == v.key) {
				setText("found");
				v.bgColor(Node.FOUND);
			} else {
				setText("notfound");
				v.bgColor(Node.NOTFOUND);
				v.goDown();
			}
			mysuspend();
		}
	}
}
