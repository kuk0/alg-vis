package algvis.core;

import java.awt.Color;
import java.awt.Graphics;

public class Node {
	public DataStructure D;
	public int key;
	public static final int INF = 99999, NOKEY = -1, NULL = 100000, INVISIBLE = -1,
			ALIVE = 0, UP = 1, DOWN = 2, LEFT = 3, RIGHT = 4, NOARROW = -10000,
			DIRARROW = -10001, TOARROW = -10002;
	public int x, y, tox, toy, steps;
	Node dir = null;
	public int state; // ALIVE, UP, DOWN, LEFT, RIGHT
	public boolean marked = false;
	int arrow = Node.NOARROW; // NOARROW or angle (0=E, 45=SE, 90=S, 135=SW,
	// 180=W)
	boolean arc = false;

	public Color fgcolor, bgcolor;
	public static final Color NORMAL = Color.yellow;
	public static final Color INSERT = new Color(0x3366ff); // 2b65ec);
	public static final Color FIND = Color.lightGray;
	public static final Color FOUND = Color.green;
	public static final Color NOTFOUND = Color.red;
	public static final Color DELETE = Color.red;

	public Node() {
	}

	public Node(DataStructure D, int key, int x, int y) {
		this.D = D;
		this.key = key;
		this.x = tox = x;
		this.y = toy = y;
		steps = 0;
		setColor(Color.black, NORMAL);
	}

	public Node(DataStructure D, int key) {
		this(D, key, 0, 0);
		setState(Node.UP);
		x = 0;
	}

	public Node(Node v) {
		this(v.D, v.key, v.x, v.y);
	}

	public void setState(int s) {
		state = s;
	}

	public void setColor(Color fg, Color bg) {
		fgcolor = fg;
		bgcolor = bg;
	}

	public void fgColor(Color fg) {
		fgcolor = fg;
	}

	public void bgColor(Color bg) {
		bgcolor = bg;
	}

	public void bgKeyColor() {
		bgColor(new Color(255, 255 - key / 10, 0));
	}

	public void mark() {
		marked = true;
	}

	public void unmark() {
		marked = false;
	}

	public void pointAbove(Node w) {
		dir = w;
		arrow = Node.DIRARROW;
	}

	public void pointTo(Node w) {
		dir = w;
		arrow = Node.TOARROW;
	}

	public void pointInDir(int angle) {
		dir = null;
		arrow = angle;
	}

	public void noArrow() {
		arrow = Node.NOARROW;
	}

	public void setArc(Node w) {
		dir = w;
		arc = true;
	}

	public void noArc() {
		arc = false;
	}

	public void drawBg(Graphics g, View v) {
		g.setColor(bgcolor);
		v.fillCircle(g, x, y, D.radius);
		g.setColor(Color.BLACK); // fgcolor);
		v.drawCircle(g, x, y, D.radius);
		if (marked) {
			v.drawCircle(g, x, y, D.radius + 2);
		}
	}

	@Override
	public String toString() {
		if (key == INF) {
			return "\u221e";
		} else if (key == -INF) {
			return "-\u221e";
		} else {
			return "" + key;
		}
	}

	public void drawKey(Graphics g, View v) {
		g.setColor(fgcolor);
		if (key != NOKEY) {
			v.drawString(g, toString(), x, y, 9);
		}
	}

	public void drawArrow(Graphics g, View v) {
		if (arrow == Node.NOARROW || (arrow < 0 && dir == null)) {
			return;
		}
		double dx, dy;
		if (arrow < 0) {
			dx = dir.x - x;
			if (arrow == DIRARROW) {
				dy = dir.y - 2 * D.radius - D.yspan - y;
			} else if (arrow == TOARROW) {
				dy = dir.y - y;
			} else {
				// vypindaj
				return;
			}
			double d = Math.sqrt(dx * dx + dy * dy);
			dx /= d;
			dy /= d;
		} else {
			dx = Math.cos(arrow * Math.PI / 180);
			dy = Math.sin(arrow * Math.PI / 180);
		}
		double x1, y1, x2, y2;
		x1 = x + 1.5 * D.radius * dx;
		y1 = y + 1.5 * D.radius * dy;
		if (arrow == TOARROW) {
			x2 = dir.x - 1.5 * D.radius * dx;
			y2 = dir.y - 1.5 * D.radius * dy;
		} else {
			x2 = x1 + 2 * D.radius * dx;
			y2 = y1 + 2 * D.radius * dy;
		}
		v.drawArrow(g, (int) x1, (int) y1, (int) x2, (int) y2);
	}

	// predpokladame ze dir je otec this
	public void drawArc(Graphics g, View v) {
		if (!arc || dir == null) {
			return;
		}
		int x = dir.x, y = this.y - D.radius - D.yspan, a = Math.abs(this.x
				- dir.x), b = Math.abs(this.y - dir.y);
		if (this.x > dir.x) {
			v.drawArcArrow(g, x - a, y - b, 2 * a, 2 * b, 0, 90);
		} else {
			v.drawArcArrow(g, x - a, y - b, 2 * a, 2 * b, 180, 90);
		}
	}

	public void draw(Graphics g, View v) {
		if (state == Node.INVISIBLE || state == Node.UP || key == NULL) {
			return;
		}
		drawBg(g, v);
		drawKey(g, v);
		drawArrow(g, v);
		drawArc(g, v);
	}

	public boolean inside(int x, int y) {
		return (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) <= D.radius
				* D.radius;
	}

	public void goTo(int tox, int toy) {
		this.tox = tox;
		this.toy = toy;
		steps = D.M.STEPS;
	}

	public void goTo(Node v) {
		goTo(v.tox, v.toy);
	}

	// public void goAbove (int tox, int toy) { goTo (tox, toy - 2*D.radius -
	// D.yspan); }
	public void goAbove(Node v) {
		goTo(v.tox, v.toy - 2 * D.radius - D.yspan);
	}

	// public void goNextTo (int tox, int toy) { goTo (tox + 2*D.radius +
	// D.xspan, toy); }
	public void goNextTo(Node v) {
		goTo(v.tox + 2 * D.radius + D.xspan, v.toy);
	}

	public void goToRoot() {
		goTo(D.rootx, D.rooty);
	}

	public void goAboveRoot() {
		goTo(D.rootx, D.rooty - 2 * D.radius - D.yspan);
	}

	public void goDown() {
		// goTo(tox, (int)(D.M.S.V.viewY + D.M.S.V.viewH) + 2 * D.radius);
		state = Node.DOWN;
	}

	public void goLeft() {
		// goTo(-D.radius, D.sheight + 3 * D.radius);
		state = Node.LEFT;
	}

	public void goRight() {
		// goTo(D.swidth + D.radius, D.sheight + 3 * D.radius);
		state = Node.RIGHT;
	}

	public void move() {
		switch (state) {
		case Node.ALIVE:
		case Node.INVISIBLE:
			if (steps > 0) {
				x += (tox - x) / steps;
				y += (toy - y) / steps;
				--steps;
			}
			break;
		case Node.UP:
			y = (int) (D.M.S.V.viewY - D.M.S.V.viewH) - D.radius;
			setState(Node.ALIVE);
			move();
			break;
		case Node.DOWN:
		case Node.LEFT:
		case Node.RIGHT:
			y += 20;
			if (state == Node.LEFT) {
				x -= 20;
			}
			if (state == Node.RIGHT) {
				x += 20;
			}
			if (!D.M.S.V.inside(x, y - D.radius)) {
				setState(Node.INVISIBLE);
			}
			break;
		}
	}
}
