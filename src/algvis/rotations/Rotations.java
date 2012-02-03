package algvis.rotations;

import java.util.Random;

import algvis.bst.BST;
import algvis.bst.BSTNode;
import algvis.core.Alignment;
import algvis.core.ClickListener;
import algvis.core.DataStructure;
import algvis.core.InputField;
import algvis.core.Layout;
import algvis.core.View;
import algvis.core.VisPanel;

public class Rotations extends DataStructure implements ClickListener {
	public static String adtName = "dictionary";
	public static String dsName = "rotations";
	public BST T;
	BSTNode v;
	public boolean subtrees = false;

	@Override
	public String getName() {
		return "rotations";
	}

	public Rotations(VisPanel M) {
		super(M);
		T = new BST(M);
		random(20);
		M.screen.V.setDS(this);
		M.screen.V.align = Alignment.LEFT;
	}

	public void rotate(int x) {
		v = T.root;
		while (v != null && v.key != x) {
			if (v.key < x) {
				v = v.getRight();
			} else {
				v = v.getLeft();
			}
		}
		if (v == null) {
			// vypis ze taky vrchol neexistuje
			return;
		} else {
			start(new Rotate(this, v));
		}
		T.root.calcTree();
	}

	@Override
	public void insert(int x) {
		BSTNode v = new BSTNode(T, x);
		BSTNode w = T.root;
		if (w == null) {
			T.root = v;
		} else {
			while (true) {
				if (w.key == x) {
					break;
				} else if (w.key < x) {
					if (w.getRight() == null) {
						w.linkRight(v);
						break;
					} else {
						w = w.getRight();
					}
				} else {
					if (w.getLeft() == null) {
						w.linkLeft(v);
						break;
					} else {
						w = w.getLeft();
					}
				}
			}
		}
		reposition();
	}

	@Override
	public void clear() {
		T.root = null;
	}

	@Override
	public void draw(View V) {
		if (v != null && v.getParent() != null) {
			V.drawWideLine(v.x, v.y, v.getParent().x, v.getParent().y);
		}
		if (T.root != null) {
			T.root.moveTree();
			T.root.drawTree(V);
		}
	}
	
	public void reposition() {
		T.reposition();
		T.root.repos(T.root.leftw, 0);
		M.screen.V.setBounds(T.x1, T.y1, T.x2, T.y2);
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
		T.root.calcTree();
		//M.screen.V.resetView();
		M.pause = p;
	}

	@Override
	public void mouseClicked(int x, int y) {
		if (T.root == null)
			return;
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
	
	
	@Override
	public Layout getLayout() {
		return Layout.SIMPLE;
	}
}