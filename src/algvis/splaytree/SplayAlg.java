package algvis.splaytree;

import algvis.core.Algorithm;
import algvis.core.Colors;

public class SplayAlg extends Algorithm {
	Splay T;
	SplayNode s, v;
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

	public SplayNode find(int K) {
		SplayNode w = (SplayNode) T.root;
		s.goTo(w);
		addStep("splaystart");
		mysuspend();
		while (true) {
			if (w.key == K) {
				break;
			} else if (w.key < K) { // right
				if (w.getRight() == null) {
					break;
				}
				w = w.getRight();
				addStep("bstfindright", K, w.key);
			} else { // left
				if (w.getLeft() == null) {
					break;
				}
				w = w.getLeft();
				addStep("bstfindleft", K, w.key);
			}
			s.goTo(w);
			mysuspend();
		}
		w.bgColor(Colors.FIND);
		T.v = null;
		addStep("splayfound");
		mysuspend();
		return w;
	}

	public void splay(SplayNode w) {
		while (!w.isRoot()) {
			if (w.getParent().isRoot()) {
				addStep("splayroot");
				w.setArc(w.getParent());
				mysuspend();
				w.noArc();
				T.rotate(w);
			} else {
				if (w.isLeft() == w.getParent().isLeft()) {
					if (w.isLeft()) {
						addStep("splayzigzigleft");
					} else {
						addStep("splayzigzigright");
					}
					w.getParent().setArc(w.getParent().getParent());
					mysuspend();
					w.getParent().noArc();
					T.rotate(w.getParent());
					w.setArc(w.getParent());
					mysuspend();
					w.noArc();
					T.rotate(w);
				} else {
					if (!w.isLeft()) {
						addStep("splayzigzagleft");
					} else {
						addStep("splayzigzagright");
					}
					w.setArc(w.getParent());
					mysuspend();
					w.noArc();
					T.rotate(w);
					w.setArc(w.getParent());
					mysuspend();
					w.noArc();
					T.rotate(w);
				}
			}
		}
		T.root = w;
	}
}
