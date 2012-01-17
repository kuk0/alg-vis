package algvis.avltree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;

public class AVLInsert extends Algorithm {
	AVL T;
	AVLNode v;
	int K;

	public AVLInsert(AVL T, int x) {
		super(T);
		this.T = T;
		v = (AVLNode) T.setNodeV(new AVLNode(T, K = x));
		v.bgColor(Colors.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		BSTNode w = T.root;
		if (T.root == null) {
			T.setRoot(v);
			v.goToRoot();
			addStep("newroot");
			mysuspend();
			v.bgColor(Colors.NORMAL);
			T.setNodeV(null);
		} else {
			v.goAboveRoot();
			addStep("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					addStep("alreadythere");
					v.goDown();
					v.bgColor(Colors.NOTFOUND);
					finish();
					return;
				} else if (w.key < K) {
					addStep("bstinsertright", K, w.key);
					if (w.right != null) {
						w = w.right;
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					addStep("bstinsertleft", K, w.key);
					if (w.left != null) {
						w = w.left;
					} else {
						w.linkLeft(v);
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}

			v.bgColor(Colors.NORMAL);
			T.reposition();
			addNote("avlinsertbal");
			mysuspend();
			
			v.bgColor(Colors.NORMAL);
			T.setNodeV(null);
			// bubleme nahor
			while (w != null) {
				w.mark();
				w.calc();
				addStep("avlupdatebal");
				mysuspend();
				if (((AVLNode) w).balance() == -2) {
					if (((AVLNode) w.left).balance() != +1) { // R-rot
						addStep("avlr");
						w.unmark();
						w = w.left;
						w.mark();
						w.setArc(w.parent);
						mysuspend();
						w.noArc();
						T.rotate(w);
					} else { // LR-rot
						addStep("avllr");
						w.unmark();
						w = w.left.right;
						w.mark();
						w.setArc(w.parent);
						w.parent.setArc(w.parent.parent);
						mysuspend();
						w.noArc();
						w.parent.noArc();
						T.rotate(w);
						mysuspend();
						T.rotate(w);
					}
					mysuspend();
				} else if (((AVLNode) w).balance() == +2) {
					if (((AVLNode) w.right).balance() != -1) { // L-rot
						addStep("avll");
						w.unmark();
						w = w.right;
						w.mark();
						w.setArc(w.parent); 
						mysuspend();
						w.noArc();
						T.rotate(w);
					} else { // RL-rot
						addStep("avlrl");
						w.unmark();
						w = w.right.left;
						w.mark();
						w.setArc(w.parent);
						w.parent.setArc(w.parent.parent);
						mysuspend();
						w.noArc();
						w.parent.noArc();
						T.rotate(w);
						mysuspend();
						T.rotate(w);
					}
					mysuspend();
				}
				w.unmark();
				w = w.parent;
			}
		}
		T.reposition();
		addNote("done");
		finish();
	}
}
