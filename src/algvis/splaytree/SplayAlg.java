package algvis.splaytree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;

public class SplayAlg extends Algorithm {
	Splay T;
	BSTNode s, v;
	int K;
	
	public SplayAlg(Splay T, int x) {
		super(T);
		this.T = T;
		if (T.root != null) {
			T.v = s = new SplayNode(T, K = x);
			s.bgColor(Colors.FIND);
			setHeader("splay");
		}
	}
	
	public BSTNode find(int K) {
		BSTNode w = T.root;
		s.goTo(w);
		setText("splaystart");
		mysuspend();
		while (true) {
			if (w.key == K) {
				break;
			} else if (w.key < K) { // right
				if (w.right == null) {
					break;
				}
				w = w.right;
				setText("bstfindright", K, w.key);
			} else { // left
				if (w.left == null) {
					break;
				}
				w = w.left;
				setText("bstfindleft", K, w.key);
			}
			s.goTo(w);
			mysuspend();
		}
		w.bgColor(Colors.FIND);
		T.v = null;
		setText("splayfound");
		mysuspend();
		return w;
	}
	
	public void splay(BSTNode w) {
		while (!w.isRoot()) {
			if (w.parent.isRoot()) {
				setText("splayroot");
				w.setArc(w.parent);
				mysuspend();
				w.noArc();
				T.rotate(w);
			} else {
				if (w.isLeft() == w.parent.isLeft()) {
					if (w.isLeft()) {
						setText("splayzigzigleft");
					} else {
						setText("splayzigzigright");
					}
					w.parent.setArc(w.parent.parent);
					mysuspend();
					w.parent.noArc();
					T.rotate(w.parent);
					w.setArc(w.parent);
					mysuspend();
					w.noArc();
					T.rotate(w);
				} else {
					if (!w.isLeft()) {
						setText("splayzigzagleft");
					} else {
						setText("splayzigzagright");
					}
					w.setArc(w.parent);
					mysuspend();
					w.noArc();
					T.rotate(w);
					w.setArc(w.parent);
					mysuspend();
					w.noArc();
					T.rotate(w);
				}
			}
		}
		T.root = w;
	}
}
