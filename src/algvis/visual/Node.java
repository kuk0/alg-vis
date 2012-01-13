package algvis.visual;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import algvis.core.Colors;
import algvis.core.View;

public class Node implements VisualElement {
	int key;
	MovingPos pos;
	Color fgcolor, bgcolor;
	boolean marked = false;
	public static final int RADIUS = 10;
	public static final int INF = 99999, NOKEY = -99998;
	
	public Node() {
	}

	public Node(int key, float x, float y) {
		this.key = key;
		pos = new MovingPos(x,y);
		setColor(Color.black, Colors.NORMAL);
	}

	public Node(int key) {
		this(key, 0, 0);
	}

	public Node(Node v) {
		this(v.key, v.pos.x, v.pos.y);
	}

	public void setColor(Color fg, Color bg) {
		fgColor(fg);
		bgColor(bg);
	}

	public void fgColor(Color fg) {
		fgcolor = fg;
	}

	public void bgColor(Color bg) {
		bgcolor = bg;
	}

	public void mark() {
		marked = true;
	}

	public void unmark() {
		marked = false;
	}

	void drawBg(View v) {
		v.setColor(bgcolor);
		v.fillCircle((int)pos.x, (int)pos.y, RADIUS);
		v.setColor(Color.BLACK); // fgcolor);
		v.drawCircle((int)pos.x, (int)pos.y, RADIUS);
		if (marked) {
			v.drawCircle((int)pos.x, (int)pos.y, RADIUS + 2);
		}
	}
	
	public String toString() {
		if (key == INF) {
			return "\u221e";
		} else if (key == -INF) {
			return "-\u221e";
		} else {
			return "" + key;
		}
	}

	void drawKey(View v) {
		v.setColor(fgcolor);
		if (key != NOKEY) {
			v.drawString(toString(), (int)pos.x, (int)pos.y, 9);
		}
	}

	public void draw(View v) {
		drawBg(v);
		drawKey(v);
	}

	public void update() {
		pos.update();
	}

	public Rectangle2D boundingBox() {
		return new Rectangle2D.Float(pos.x-10, pos.y-10, pos.x+10, pos.y+10);
	}
}
