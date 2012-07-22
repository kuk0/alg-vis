/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.core;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.jdom2.Element;

import algvis.gui.Fonts;
import algvis.gui.view.View;
import algvis.scenario.Command;

/**
 * The Class Node. This is a basic element of the visualization. Nodes can be
 * drawn, they can move, change color, become marked/unmarked, or point in some
 * direction. Nodes are by default drawn as circles with their key in the
 * middle.
 */
public class Node {
	public DataStructure D;
	private int key;
	/**
	 * x, y - node position tox, toy - the position, where the node is heading
	 * steps - the number of steps to reach the destination
	 */
	public int x;
    public int y;
    public int tox;
    public int toy;
    protected int steps;
	/** the state of a node - either ALIVE, DOWN, LEFT, or RIGHT. */
	public int state = ALIVE;
	private NodeColor color = NodeColor.NORMAL;
	public boolean marked = false;
	protected Node dir = null;
	private int arrow = Node.NOARROW; // NOARROW or angle (0=E, 45=SE, 90=S,
										// 135=SW, 180=W)
                                        private boolean arc = false;

	private static final int STEPS = 10;
	public static final int radius = 10;

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
	public static final int INVISIBLE = -1;
    private static final int ALIVE = 0;
    private static final int DOWN = 2;
    private static final int LEFT = 3;
    private static final int RIGHT = 4;
	private static final int NOARROW = -10000;
    private static final int DIRARROW = -10001;
    private static final int TOARROW = -10002;

	protected Node() {
	}

	protected Node(DataStructure D, int key, int x, int y) {
		this.D = D;
		this.setKey(key);
		this.x = tox = x;
		this.y = toy = y;
		steps = 0;
	}

	protected Node(DataStructure D, int key) {
		this(D, key, 0, 0);
		getReady();
	}

	public Node(Node v) {
		this(v.D, v.getKey(), v.x, v.y);
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
		}
	}

	public void setState(int s) {
		if (state != s && D.M.scenario.isAddingEnabled()) {
			D.M.scenario.add(new SetStateCommand(s));
		}
		state = s;
		if ((s == Node.LEFT || s == Node.RIGHT || s == Node.DOWN)
				&& D.M.scenario.traverser.isInterrupted()) {
			int k = 0;
			if (s == Node.LEFT) {
				k = -1;
			} else if (s == Node.RIGHT) {
				k = 1;
			}
			while (D.M.screen.V.inside(x, y - Node.radius)) {
				toy = y += 20;
				tox = x += k * 20;
			}
			state = INVISIBLE;
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

	protected void fgColor(Color fg) {
		if (fg != color.fgColor) {
			if (D != null && D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new SetFgColorCommand(fg));
			}
			color = new NodeColor(fg, color.bgColor);
		}
	}

	protected void bgColor(Color bg) {
		if (bg != color.bgColor) {
			if (D != null && D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new SetBgColorCommand(bg));
			}
			color = new NodeColor(color.fgColor, bg);
		}
	}

	protected Color getFgColor() {
		return color.fgColor;
	}

	protected Color getBgColor() {
		return color.bgColor;
	}

	/**
	 * Set background color depending on the key (the higher the key, the darker
	 * the color).
	 */
    protected void bgKeyColor() {
		bgColor(new Color(255, 255 - getKey() / 20, 0));
	}

	public void mark() {
		if (!marked) {
			if (D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new MarkCommand(true));
			}
			marked = true;
		}
	}

	public void unmark() {
		if (marked) {
			if (D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new MarkCommand(false));
			}
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
			if (D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new ArrowCommand(true));
			}
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
			if (D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new ArrowCommand(true));
			}
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
			if (D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new ArrowCommand(true));
			}
		}
	}

	/**
	 * Stop drawing an arrow.
	 */
	public void noArrow() {
		if (dir != null || arrow != Node.NOARROW) {
			if (D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new ArrowCommand(false));
			}
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
		if (dir != w || !arc) {
			dir = w;
			arc = true;
			if (D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new ArcCommand(dir, true));
			}
		}
	}

	/**
	 * Stop drawing an arc.
	 */
	public void noArc() {
		if (arc) {
			arc = false;
			if (D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new ArcCommand(dir, false));
			}
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
		if (getKey() == INF) {
			return "\u221e";
		} else if (getKey() == -INF) {
			return "-\u221e";
		} else {
			return "" + getKey();
		}
	}

	protected void drawKey(View v) {
		v.setColor(getFgColor());
		if (getKey() != NOKEY) {
			v.drawString(toString(), x, y, Fonts.NORMAL);
		}
	}

	protected void drawArrow(View v) {
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
    protected void drawArc(View v) {
		if (!arc || dir == null) {
			return;
		}
		int x = dir.x, y = this.y - DataStructure.minsepy + Node.radius, a = Math
				.abs(this.x - dir.x), b = Math.abs(this.y - dir.y);
		v.setColor(Color.BLACK);
		if (this.x > dir.x) {
			v.drawArcArrow(x - a, y - b, 2 * a, 2 * b, 0, 90);
		} else {
			v.drawArcArrow(x - a, y - b, 2 * a, 2 * b, 180, 90);
		}
	}

	public void draw(View v) {
		if (state == Node.INVISIBLE || getKey() == NULL) {
			return;
		}
		drawBg(v);
		drawKey(v);
		drawArrow(v);
		drawArc(v);
	}

	/**
	 * Is the given point inside the node? (Used mainly to decide whether a user
	 * clicked at the node.)
	 */
    protected boolean inside(int x, int y) {
		return (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) <= Node.radius
				* Node.radius;
	}

	/**
	 * Set new coordinates, where the node should go.
	 */
	public void goTo(int tox, int toy) {
		if (this.tox != tox || this.toy != toy) {
			if (D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new MoveCommand(tox, toy));
			}
			if (D.M.scenario.traverser.isInterrupted()) {
				steps = 0;
				x = this.tox = tox;
				y = this.toy = toy;
			} else {
				this.tox = tox;
				this.toy = toy;
				this.steps = STEPS;
			}
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

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		if (this.key != key) {
			if (D != null && D.M.scenario.isAddingEnabled()) {
				D.M.scenario.add(new SetKeyCommand(key));
			}
			this.key = key;
		}
	}

	private class ArcCommand implements Command {
		private final Node toNode;
		private final boolean setted;

		public ArcCommand(Node toNode, boolean setted) {
			this.toNode = toNode;
			this.setted = setted;
		}

		@Override
		public Element getXML() {
			Element e = new Element("Arc");
			e.setAttribute("fromNode", Integer.toString(getKey()));
			e.setAttribute("toNode", Integer.toString(toNode.getKey()));
			e.setAttribute("setted", Boolean.toString(setted));
			return e;
		}

		@Override
		public void execute() {
			if (setted) {
				setArc(toNode);
			} else {
				noArc();
			}
		}

		@Override
		public void unexecute() {
			if (setted) {
				noArc();
			} else {
				setArc(toNode);
			}
		}
	}

	private class ArrowCommand implements Command {
		private final Node dir;
		private final int arrow;
		private final boolean drawArrow;
		private final String name;

		public ArrowCommand(boolean drawArrow) {
			dir = Node.this.dir;
			arrow = Node.this.arrow;
			this.drawArrow = drawArrow;
			if (drawArrow) {
				name = "arrow";
			} else {
				name = "noArrow";
			}
		}

		@Override
		public void execute() {
			if (drawArrow) {
				Node.this.dir = dir;
				Node.this.arrow = arrow;
			} else {
				noArrow();
			}
		}

		@Override
		public void unexecute() {
			if (drawArrow) {
				noArrow();
			} else {
				Node.this.arrow = arrow;
				Node.this.dir = dir;
			}
		}

		@Override
		public Element getXML() {
			Element e = new Element("node");
			e.setAttribute("action", name);
			e.setAttribute("key", Integer.toString(getKey()));
			if (dir != null) {
				e.setAttribute("toNode", Integer.toString(dir.getKey()));
			} else {
				e.setAttribute("angle", Integer.toString(arrow));
			}
			return e;
		}
	}

	private class MarkCommand implements Command {
		private final boolean marked;

		public MarkCommand(boolean marked) {
			this.marked = marked;
		}

		@Override
		public Element getXML() {
			Element e = new Element("mark");
			e.setAttribute("key", Integer.toString(getKey()));
			e.setAttribute("marked", Boolean.toString(marked));
			return e;
		}

		@Override
		public void execute() {
			if (marked) {
				mark();
			} else {
				unmark();
			}
		}

		@Override
		public void unexecute() {
			if (marked) {
				unmark();
			} else {
				mark();
			}
		}
	}

	private class MoveCommand implements Command {
		private final int fromX, fromY, toX, toY;

		public MoveCommand(int toX, int toY) {
			fromX = tox;
			fromY = toy;
			this.toX = toX;
			this.toY = toY;
		}

		@Override
		public void execute() {
			goTo(toX, toY);
		}

		@Override
		public void unexecute() {
			goTo(fromX, fromY);
		}

		@Override
		public Element getXML() {
			Element e = new Element("node");
			e.setAttribute("action", "move");
			e.setAttribute("key", Integer.toString(getKey()));
			e.setAttribute("posX", Integer.toString(toX));
			e.setAttribute("posY", Integer.toString(toY));
			e.setAttribute("fromPosX", Integer.toString(fromX));
			e.setAttribute("fromPosY", Integer.toString(fromY));
			return e;
		}
	}

	private class SetBgColorCommand implements Command {
		private final Color oldBgColor, newBgColor;

		public SetBgColorCommand(Color newBgColor) {
			oldBgColor = getBgColor();
			this.newBgColor = newBgColor;
		}

		@Override
		public void execute() {
			bgColor(newBgColor);
		}

		@Override
		public void unexecute() {
			bgColor(oldBgColor);
		}

		@Override
		public Element getXML() {
			Element e = new Element("node");
			e.setAttribute("action", "changeColor");
			e.setAttribute("key", Integer.toString(getKey()));
			e.setAttribute("bgColor", newBgColor.toString());
			return e;
		}
	}

	private class SetFgColorCommand implements Command {
		private final Color oldFgColor, newFgColor;

		public SetFgColorCommand(Color newfgColor) {
			oldFgColor = getFgColor();
			this.newFgColor = newfgColor;
		}

		@Override
		public void execute() {
			fgColor(newFgColor);
		}

		@Override
		public void unexecute() {
			fgColor(oldFgColor);
		}

		@Override
		public Element getXML() {
			Element e = new Element("node");
			e.setAttribute("action", "changeColor");
			e.setAttribute("key", Integer.toString(getKey()));
			e.setAttribute("fgColor", newFgColor.toString());
			return e;
		}
	}

	private class SetStateCommand implements Command {
		private final int fromState, toState;
		private final int fromX, fromY;

		public SetStateCommand(int toState) {
			this.toState = toState;
			fromState = state;
			fromX = tox;
			fromY = toy;
		}

		@Override
		public void execute() {
			setState(toState);
		}

		@Override
		public void unexecute() {
			if (toState == Node.LEFT || toState == Node.DOWN
					|| toState == Node.RIGHT) {
				goTo(fromX, fromY);
			}
			setState(fromState);
		}

		@Override
		public Element getXML() {
			Element e = new Element("node");
			e.setAttribute("action", "changeState");
			e.setAttribute("key", Integer.toString(getKey()));
			e.setAttribute("toState", Integer.toString(toState));
			e.setAttribute("fromState", Integer.toString(fromState));
			return e;
		}
	}

	private class SetKeyCommand implements Command {
		private final int fromKey, toKey;

		public SetKeyCommand(int toKey) {
			this.toKey = toKey;
			fromKey = key;
		}

		@Override
		public void execute() {
			setKey(toKey);
		}

		@Override
		public void unexecute() {
			setKey(fromKey);
		}

		@Override
		public Element getXML() {
			Element e = new Element("node");
			e.setAttribute("action", "changeKey");
			e.setAttribute("fromKey", Integer.toString(fromKey));
			e.setAttribute("toKey", Integer.toString(toKey));
			return e;
		}
	}

	public class WaitBackwardsCommand implements Command {

		@Override
		public Element getXML() {
			Element e = new Element("waitBackwards");
			e.setAttribute("nodeKey", Integer.toString(getKey()));
			return e;
		}

		@Override
		public void execute() {
		}

		@Override
		public void unexecute() {
			while (x != tox || y != toy) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					x = tox;
					y = toy;
					break;
				}
			}
		}
	}
}
