package algvis;

import java.awt.Color;
import java.awt.Graphics;

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

	/*
	 * public void draw (Graphics g) { SkipList T = (SkipList)M.D;
	 * g.setColor(color); g.fillOval(x-T.radius, y-T.radius, 2*T.radius,
	 * 2*T.radius); g.setColor(Color.black); g.drawOval(x-T.radius, y-T.radius,
	 * 2*T.radius, 2*T.radius);
	 * 
	 * Font font = new Font("Helvetica", Font.PLAIN, T.fontsize); FontMetrics fm
	 * = g.getFontMetrics(font); String str = new String(""+key); if (key ==
	 * 999) str = "\u221e"; if (key == -999) str = "-\u221e"; g.setFont(font);
	 * g.drawString(str, x-fm.stringWidth(str)/2,
	 * y-fm.getHeight()/2+fm.getAscent()); }
	 */
	public void drawBg(Graphics g, View v) {
		g.setColor(bgcolor);
		v.fillSqr(g, x, y, D.radius);
		g.setColor(Color.BLACK); // fgcolor);
		v.drawSqr(g, x, y, D.radius);
		if (marked) {
			v.drawSqr(g, x, y, D.radius + 2);
		}
	}

	public void drawSkipList(Graphics G, View V) {
		if (left == null && down != null) {
			G.setColor(Color.black);
			V.drawLine(G, x, y, down.x, down.y);
			down.drawSkipList(G, V);
		}
		if (right != null) {
			G.setColor(Color.black);
			V.drawArrow(G, x, y, right.x-D.radius, right.y);
			right.drawSkipList(G, V);
		}
		draw(G, V);
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
