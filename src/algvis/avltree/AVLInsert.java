package algvis.avltree;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;
import algvis.core.Node;

public class AVLInsert extends Algorithm {
	AVL T;
	BSTNode v;
	int K;

	public AVLInsert(AVL T, int x) {
		super(T);
		this.T = T;
		v = T.setNodeV(new AVLNode(T, K = x, T.up()));
		v.setState(Node.ALIVE);
		v.bgColor(Colors.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		BSTNode w = T.root;
		if (T.root == null) {
			T.setRoot(v);
			v.goToRoot();
			setText("newroot");
			mysuspend();
			v.bgColor(Colors.NORMAL);
			T.setNodeV(null);
		} else {
			v.goAboveRoot();
			setText("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					setText("alreadythere");
					v.goDown();
					v.bgColor(Colors.NOTFOUND);
					finish();
					return;
				} else if (w.key < K) {
					setText("bstinsertright", K, w.key);
					if (w.right != null) {
						w = w.right;
					} else {
						w.linkRight(new AVLNode(v));
						break;
					}
				} else {
					setText("bstinsertleft", K, w.key);
					if (w.left != null) {
						w = w.left;
					} else {
						w.linkLeft(new AVLNode(v));
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}

			T.reposition();
			setText("avlinsertbal");
			mysuspend();
			
			v.bgColor(Colors.NORMAL);
			T.setNodeV(null);
			// bubleme nahor
			while (w != null) {
				w.mark();
				w.calc();
				setText("avlupdatebal");
				mysuspend();
				if (((AVLNode) w).balance() == -2) {
					if (((AVLNode) w.left).balance() != +1) { // R-rot
						setText("avlr");
						w.unmark();
						w = w.left;
						w.mark();
						w.setArc(w.parent);
						mysuspend();
						w.noArc();
						T.rotate(w);
					} else { // LR-rot
						setText("avllr");
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
						setText("avll");
						w.unmark();
						w = w.right;
						w.mark();
						w.setArc(w.parent); 
						mysuspend();
						w.noArc();
						T.rotate(w);
					} else { // RL-rot
						setText("avlrl");
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
			// w.unmark();
		}
		T.reposition();
		setText("done");
		finish();
	}
}
