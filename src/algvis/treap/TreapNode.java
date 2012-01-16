package algvis.treap;

import java.awt.Color;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;

public class TreapNode extends BSTNode {
	// public TreapNode left=null, right=null, parent=null;
	double p;

	public TreapNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		p = Math.random();
		bgPColor();
	}

	public TreapNode(DataStructure D, int key) {
		this(D, key, 0, 0);
		getReady();
	}

	public void bgPColor() {
		bgColor(new Color(255, 255 - (int) Math.round(100 * p), 0));
	}

	public void linkleft(TreapNode v) {
		left = v;
		if (v != null) {
			v.parent = this;
		}
	}

	public void linkright(TreapNode v) {
		right = v;
		if (v != null) {
			v.parent = this;
		}
	}
}
