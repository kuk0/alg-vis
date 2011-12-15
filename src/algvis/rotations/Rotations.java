package algvis.rotations;

import java.util.Random;

import algvis.bst.BST;
import algvis.bst.BSTNode;
import algvis.core.ClickListener;
import algvis.core.DataStructure;
import algvis.core.InputField;
import algvis.core.View;
import algvis.core.VisPanel;

public class Rotations extends DataStructure implements ClickListener {
	public static String adtName = "dictionary";
	public static String dsName = "rotations";
	BST T;

	public Rotations(VisPanel M) {
		super(M);
		T = new BST(M);
		random(10);
		M.S.V.setDS(this);
	}

	public void rotate(int x) {
		start(new Rotate(T, x));
	}

	@Override
	public void insert(int x) {
		BSTNode v = new RotNode(T, x);
		BSTNode w = T.root;
		if (w == null) {
			T.root = v;
		} else {
			while (true) {
				if (w.key == x) {
					break;
				} else if (w.key < x) {
					if (w.right == null) {
						w.linkright(v);
						break;
					} else {
						w = w.right;
					}
				} else {
					if (w.left == null) {
						w.linkleft(v);
						break;
					} else {
						w = w.left;
					}
				}
			}
		}
		T.reposition();
	}

	@Override
	public void clear() {
		T.root = null;
	}

	@Override
	public void draw(View V) {
		if (T.root != null) {
			T.root.moveTree();
			((RotNode) T.root).drawTree(V);
		}
	}

	@Override
	public String stats() {
		return "";
	}

	@Override
	public void random(int n) {
		Random g = new Random(System.currentTimeMillis());
		boolean p = M.pause;
		M.pause = false;
		for (int i = 0; i < n; ++i) {
			insert(g.nextInt(InputField.MAX + 1));
		}
		M.pause = p;
	}

	public void mouseClicked(int x, int y) {
		if (T.root == null) return;
		BSTNode v = T.root.find(x, y);
		if (v != null) {
			if (v.marked) {
				v.unmark();
				chosen = null;
				M.B.I.setText("");
			} else {
				if (chosen != null)
					chosen.unmark();
				v.mark();
				chosen = v;
				M.B.I.setText("" + chosen.key);
			}
		}
	}
}