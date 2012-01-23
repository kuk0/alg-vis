package algvis.skiplist;

import java.awt.Color;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;

public class SkipNode extends Node {
	SkipNode left = null, right = null, up = null, down = null;
	Color color = Color.yellow;

	public SkipNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public SkipNode(DataStructure D, int key) {
		super(D, key);
	}

	public void linkleft(SkipNode v) {
		left = v;
		v.right = this;
	}

	public void linkright(SkipNode v) {
		right = v;
		v.left = this;
	}

	public void linkup(SkipNode v) {
		up = v;
		v.down = this;
	}

	public void linkdown(SkipNode v) {
		down = v;
		v.up = this;
	}

	public void isolate() {
		left = right = up = down = null;
	}

	@Override
	public void drawBg(View v) {
		v.setColor(bgcolor);
		v.fillSqr(x, y, D.radius);
		v.setColor(Color.BLACK); // fgcolor);
		v.drawSqr(x, y, D.radius);
		if (marked) {
			v.drawSqr(x, y, D.radius + 2);
		}
	}

	public void drawSkipList(View V) {
		if (left == null && down != null) {
			V.setColor(Color.black);
			V.drawLine(x, y, down.x, down.y);
			down.drawSkipList(V);
		}
		if (right != null) {
			V.setColor(Color.black);
			V.drawArrow(x, y, right.x - D.radius, right.y);
			right.drawSkipList(V);
		}
		draw(V);
	}

	public void moveSkipList() {
		if (left == null && down != null) {
			down.moveSkipList();
		}
		if (right != null) {
			right.moveSkipList();
		}
		move();
	}

	public void _reposition() {
		if (D.x2 < this.tox) {
			D.x2 = this.tox;
		}
		if (D.y2 < this.toy) {
			D.y2 = this.toy;
		}
		if (left == null) {
			if (up == null) {
				goToRoot();
			} else {
				goTo(up.tox, up.toy + 2 * D.radius + D.yspan);
			}
			if (down != null) {
				down._reposition();
			}
		} else {
			if (down == null) {
				goNextTo(left);
			} else {
				goTo(down.tox, left.toy);
			}
		}
		if (right != null) {
			right._reposition();
		}
	}
}
