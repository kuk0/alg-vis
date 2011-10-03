package algvis;

public class SplayInsert extends SplayAlg {
	public SplayInsert(Splay T, int x) {
		super(T, x);
		T.vv = v = new SplayNode(T, x);
		v.bgColor(Node.INSERT);
	}

	@Override
	public void run() {
		if (T.root == null) {
			T.root = v;
			v.goToRoot();
			setText("newroot");
			mysuspend();
		} else {
			v.goAboveRoot();
			BSTNode w = find(K);
			splay(w);

			setHeader("insertion");
			w.bgColor(Node.NORMAL);
			if (w.key == K) {
				setText("alreadythere");
				v.goDown();
				v.bgColor(Node.NOTFOUND);
				return;
			} else if (w.key < K) {
				setText("splayinsertleft");
				mysuspend();
				v.linkleft(w);
				v.linkright(w.right);
				w.right = null;
			} else {
				setText("splayinsertright");
				mysuspend();
				v.linkright(w);
				v.linkleft(w.left);
				w.left = null;
			}
			T.root = v;
			T.reposition();
			mysuspend();
		}
		setText("done");
		v.bgColor(Node.NORMAL);
		T.vv = null;
	}
}
