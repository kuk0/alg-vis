package algvis;

public class SkipAlg extends Algorithm {
	SkipList L;
	SkipNode v;
	SkipNode p[];
	int K;

	public SkipAlg(SkipList L, int x) {
		super(L.M);
		this.L = L;
		L.v = v = new SkipNode(L, x);
		K = x;
	}
	
	public SkipNode find() {
		SkipNode w = L.root;
		v.goToRoot();
		mysuspend();

		for (int i = L.height - 1;; --i) {
			while (w.right.key < K) {
				setText("skipnext");
				w = w.right;
				v.goTo(w);
				mysuspend();
			}
			setText("skipdown");
			p[i] = w;
			if (w.down == null) {
				break;
			}
			w = w.down;
			v.goTo(w);
			mysuspend();
		}
		return w;
	}
}
