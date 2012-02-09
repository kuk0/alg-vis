package algvis.core;

import java.awt.Color;
import java.awt.geom.Point2D;

import algvis.scenario.commands.node.ArcCommand;
import algvis.scenario.commands.node.ArrowCommand;
import algvis.scenario.commands.node.MarkCommand;
import algvis.scenario.commands.node.MoveCommand;
import algvis.scenario.commands.node.SetBgColorCommand;
import algvis.scenario.commands.node.SetFgColorCommand;
import algvis.scenario.commands.node.SetStateCommand;

/**
 * The Class Node. This is a basic element of the visualization. Nodes can be
 * drawn, they can move, change color, become marked/unmarked, or point in some
 * direction. Nodes are by default drawn as circles with their key in the
 * middle.
 */
public class Node {
	public static int N = 0;
	public int id;
	public DataStructure D;
	public int key;
	/**
	 * x, y - node position tox, toy - the position, where the node is heading
	 * steps - the number of steps to reach the destination
	 */
	public int x, y, tox, toy, steps;
	/** the state of a node - either ALIVE, DOWN, LEFT, or RIGHT. */
	public int state = ALIVE;
	private NodeColor color = NodeColor.NORMAL;
	public boolean marked = false;
	public Node dir = null;
	public int arrow = Node.NOARROW; // NOARROW or angle (0=E, 45=SE, 90=S,
										// 135=SW,
	// 180=W)
	boolean arc = false;
	public static int STEPS = 10;
	public static int radius = 10;

	/**
	 * the key values are generally integers from 1 to 999 (inclusive) special
	 * values of a key are: INF (infinity) or -INF (-infinity), these are drawn
	 * nicely as the numeral 8 sideways NOKEY (drawn as an empty circle), and
	 * NULL (not drawn at all)
	 */
	public static final int INF = 99999, NOKEY = -1, NULL = 100000;
	/**
	 * a node can be in several different states: INVISIBLE (default starting
	 * state, not drawn), ALIVE (visible), DOWN, LEFT, and RIGHT (the node moves
	 * down, or diagonally left or right until it gets out of the screen, and
	 * then turns INVISIBLE)
	 */
	public static final int INVISIBLE = -1, ALIVE = 0, DOWN = 2, LEFT = 3,
			RIGHT = 4;
	public static final int NOARROW = -10000, DIRARROW = -10001,
			TOARROW = -10002;

	public Node() {
	}

	public Node(DataStructure D, int key, int x, int y) {
		this.id = Node.N;
		++Node.N;
		this.D = D;
		this.key = key;
		this.x = tox = x;
		this.y = toy = y;
		steps = 0;
	}

	public Node(DataStructure D, int key) {
		this(D, key, 0, 0);
		getReady();
	}

	public Node(Node v) {
		this(v.D, v.key, v.x, v.y);
	}

	/**
	 * position the node above top of the screen and set its state to ALIVE, so
	 * the node is ready to come to screen now
	 */
	protected void getReady() {
		if (D.M.screen.V.at != null) {
			Point2D p = D.M.screen.V.r2v(0, 0);
			toy = y = (int) p.getY() - 5 * Node.radius;
		} else {
			/*
			 * TODO because of rotations and skiplist constructor inserts (at
			 * that time "AffineTransform at" not exists)
			 */
			tox = x = 0;
			toy = y = -5 * Node.radius;
			// System.out.println(getClass().getName() + " " + key);
		}
	}

	public void setState(int s) {
		if (state != s) {
			D.scenario.add(new SetStateCommand(this, s));
			state = s;
		}
	}

	public NodeColor getColor() {
		return color;
	}
	
	public void setColor(NodeColor color) {
		fgColor(color.fgColor);
		bgColor(color.bgColor);
		this.color = color;
	}

	public void fgColor(Color fg) {
		if (fg != color.fgColor) {
			if (D != null) {
				D.scenario.add(new SetFgColorCommand(this, fg));
			}
			color = new NodeColor(fg, color.bgColor);
		}
	}

	public void bgColor(Color bg) {
		if (bg != color.bgColor) {
			if (D != null) {
				D.scenario.add(new SetBgColorCommand(this, bg));
			}
			color = new NodeColor(color.fgColor, bg);
		}
	}
	
	public Color getFgColor() {
		return color.fgColor;
	}
	
	public Color getBgColor() {
		return color.bgColor;
	}

	/**
	 * Set background color depending on the key (the higher the key, the darker
	 * the color).
	 */
	public void bgKeyColor() {
		bgColor(Color.white);
		// bgColor(new Color(255, 255 - key / 10, 0));
	}

	public void mark() {
		if (!marked) {
			D.scenario.add(new MarkCommand(this, true));
			marked = true;
		}
	}

	public void unmark() {
		if (marked) {
			D.scenario.add(new MarkCommand(this, false));
			marked = false;
		}
	}

	/**
	 * Draw an arrow pointing above the node w.
	 * 
	 * @param w
	 */
	public void pointAbove(Node w) {
		if (dir != w || arrow != Node.DIRARROW) {
			dir = w;
			arrow = Node.DIRARROW;
			D.scenario.add(new ArrowCommand(this, true));
		}
	}

	/**
	 * Draw an arrow pointing to the node w.
	 * 
	 * @param w
	 */
	public void pointTo(Node w) {
		if (dir != w || arrow != Node.TOARROW) {
			dir = w;
			arrow = Node.TOARROW;
			D.scenario.add(new ArrowCommand(this, true));
		}
	}

	/**
	 * Point in direction angle. The angle in degrees should be a nonnegative
	 * integer 0 = RIGHT, then clockwise: 90 = DOWN, 180 = LEFT
	 * 
	 * @param angle
	 */
	public void pointInDir(int angle) {
		if (dir != null || arrow != angle) {
			dir = null;
			arrow = angle;
			D.scenario.add(new ArrowCommand(this, true));
		}
	}

	/**
	 * Stop drawing an arrow.
	 */
	public void noArrow() {
		if (dir != null || arrow != Node.NOARROW) {
			D.scenario.add(new ArrowCommand(this, false));
			dir = null;
			arrow = Node.NOARROW;
		}
	}

	/**
	 * Draw an arc pointing to node w. Assumption: w is above this node.
	 * 
	 * @param w
	 */
	public void setArc(Node w) {
		if (dir != w || arc == false) {
			dir = w;
			arc = true;
			D.scenario.add(new ArcCommand(this, dir, true));
		}
	}

	/**
	 * Stop drawing an arc.
	 */
	public void noArc() {
		if (arc == true) {
			arc = false;
			D.scenario.add(new ArcCommand(this, dir, false));
		}
	}

	/**
	 * Draw bg.
	 * 
	 * @param g
	 *            where to draw
	 * @param v
	 *            view
	 */
	protected void drawBg(View v) {
		v.setColor(getBgColor());
		v.fillCircle(x, y, Node.radius);
		v.setColor(Color.BLACK); // fgcolor);
		v.drawCircle(x, y, Node.radius);
		if (marked) {
			v.drawCircle(x, y, Node.radius + 2);
		}
	}

	/**
	 * Convert the key into a string (INF is converted to "8" sideways).
	 */
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

	public void drawKey(View v) {
		v.setColor(getFgColor());
		if (key != NOKEY) {
			v.drawString(toString(), x, y, 9);
		}
	}

	public void drawArrow(View v) {
		if (arrow == Node.NOARROW || (arrow < 0 && dir == null)) {
			return;
		}
		double dx, dy;
		if (arrow < 0) {
			dx = dir.x - x;
			if (arrow == DIRARROW) {
				dy = dir.y - DataStructure.minsepy - y;
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
		x1 = x + 1.5 * Node.radius * dx;
		y1 = y + 1.5 * Node.radius * dy;
		if (arrow == TOARROW) {
			x2 = dir.x - 1.5 * Node.radius * dx;
			y2 = dir.y - 1.5 * Node.radius * dy;
		} else {
			x2 = x1 + 2 * Node.radius * dx;
			y2 = y1 + 2 * Node.radius * dy;
		}
		v.setColor(Color.BLACK);
		v.drawArrow((int) x1, (int) y1, (int) x2, (int) y2);
	}

	// Assumption: dir (the node we are pointing to) is above this node
	public void drawArc(View v) {
		if (!arc || dir == null) {
			return;
		}
		int x = dir.x, y = this.y - DataStructure.minsepy + Node.radius, a = Math.abs(this.x
				- dir.x), b = Math.abs(this.y - dir.y);
		v.setColor(Color.BLACK);
		if (this.x > dir.x) {
			v.drawArcArrow(x - a, y - b, 2 * a, 2 * b, 0, 90);
		} else {
			v.drawArcArrow(x - a, y - b, 2 * a, 2 * b, 180, 90);
		}
	}

	public void draw(View v) {
		if (state == Node.INVISIBLE || key == NULL) {
			return;
		}
		drawBg(v);
		drawKey(v);
		drawArrow(v);
		drawArc(v);
		if (v.output) {
			System.out.println("  Node("+id+","+key+","+cpos()+","+(marked?1:0)+")");
		}
	}

	/**
	 * Is the given point inside the node? (Used mainly to decide whether a user
	 * clicked at the node.)
	 */
	public boolean inside(int x, int y) {
		return (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) <= Node.radius
				* Node.radius;
	}

	/**
	 * Set new coordinates, where the node should go.
	 */
	public void goTo(int tox, int toy) {
		if (this.tox != tox || this.toy != toy) {
			D.scenario.add(new MoveCommand(this, tox, toy));
			this.tox = tox;
			this.toy = toy;
			this.steps = STEPS;
		}
	}

	/**
	 * Go to the same position where node v is going.
	 */
	public void goTo(Node v) {
		goTo(v.tox, v.toy);
	}

	// public void goAbove (int tox, int toy) { goTo (tox, toy - 2*D.radius -
	// D.yspan); }
	/**
	 * Go above node v (or more precisely: above the position where v is going).
	 */
	public void goAbove(Node v) {
		goTo(v.tox, v.toy - DataStructure.minsepy);
	}

	// public void goNextTo (int tox, int toy) { goTo (tox + 2*D.radius +
	// D.xspan, toy); }
	/**
	 * Go next to node v (precisely to the right of where v is going).
	 */
	public void goNextTo(Node v) {
		goTo(v.tox + DataStructure.minsepx, v.toy);
	}

	/**
	 * Go to the root position.
	 */
	public void goToRoot() {
		goTo(DataStructure.rootx, DataStructure.rooty);
	}

	/**
	 * Go above the root position.
	 */
	public void goAboveRoot() {
		int toy = DataStructure.rooty - DataStructure.minsepy;
		goTo(DataStructure.rootx, toy);
	}

	/**
	 * Go downwards out of the screen.
	 */
	public void goDown() {
		setState(DOWN);
	}

	/**
	 * Go left downwards out of the screen.
	 */
	public void goLeft() {
		setState(LEFT);
	}

	/**
	 * Go right downwards out of the screen.
	 */
	public void goRight() {
		setState(RIGHT);
	}

	/**
	 * Make one step towards the destination (tox, toy). In the special states
	 * DOWN, LEFT, or RIGHT, go downwards off the screen.
	 */
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
		case Node.DOWN:
		case Node.LEFT:
		case Node.RIGHT:
			toy = y += 20;
			if (state == Node.LEFT) {
				tox = x -= 20;
			} else if (state == Node.RIGHT) {
				tox = x += 20;
			}
			if (!D.M.screen.V.inside(x, y - Node.radius)) {
				state = Node.INVISIBLE;
			}
			break;
		}
	}
	
	public String cpos() {
		return "("+x+"+"+y+"*1j)";
	}
}
