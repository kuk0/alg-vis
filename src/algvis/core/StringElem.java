package algvis.core;

import algvis.ds.DataStructure;
import algvis.gui.Fonts;
import algvis.gui.view.View;

import java.awt.*;
import java.util.ArrayList;

public class StringElem {
	private static final int span = 12;
	//private final DataStructure D;
	private final int x;
    private final int y;
    private int len;
	private final String s;
	private final ArrayList<Color> col;

	public StringElem(DataStructure D, String s, int x, int y) {
		//this.D = D;
		this.s = s;
		this.x = x;
		this.y = y;
		len = s.length();
		col = new ArrayList<Color>();
		for (int i = 0; i < len; ++i)
			// if (i % 2 == 1)
			// col.add(NodeColor.NORMAL.bgColor);
			// else
			col.add(NodeColor.DARKER.bgColor);
	}

	public void setColor(Color c, int a, int b) {
		if (a < 0)
			a = 0;
		if (b > len)
			len = b;
		for (int i = a; i < b; ++i)
			col.set(i, c);
	}

	public void draw(View v) {
		v.setColor(NodeColor.NORMAL.bgColor);
		v.fillRoundRectangle(x, y, len * span / 2 + 7, Node.RADIUS,
				2 * Node.RADIUS, 2 * Node.RADIUS);
		int x0 = x - len * span / 2 + 6;
		v.setColor(col.get(0));
		v.fillRoundRectangle(x0 - 6, y, 7, Node.RADIUS, 2 * Node.RADIUS,
				2 * Node.RADIUS);
		v.setColor(col.get(len - 1));
		v.fillRoundRectangle(x + len * span / 2, y, 7, Node.RADIUS,
				2 * Node.RADIUS, 2 * Node.RADIUS);

		for (int i = 0; i < len; ++i) {
			v.setColor(col.get(i));
			v.fillRect(x0, y, 6, Node.RADIUS);
			v.setColor(NodeColor.NORMAL.fgColor);
			v.drawString("" + (i + 1), x0, y - Node.RADIUS - 5, Fonts.SMALL);
			v.drawString("" + s.charAt(i), x0, y - 1, Fonts.TYPEWRITER);
			x0 += span;
		}
		v.setColor(NodeColor.NORMAL.fgColor);
		v.drawRoundRectangle(x, y, len * span / 2 + 7, Node.RADIUS,
				2 * Node.RADIUS, 2 * Node.RADIUS);
	}
}
