package algvis.core;

import java.awt.Color;

public class NodeColor {
	public static final NodeColor BLACK = new NodeColor(Color.WHITE,
			Color.BLACK);
	public static final NodeColor BLUE = new NodeColor(Color.WHITE, Color.BLUE);
	public static final NodeColor GREEN = new NodeColor(Color.BLACK,
			Color.GREEN);
	public static final NodeColor RED = new NodeColor(Color.BLACK, Color.RED);

	public static final NodeColor NORMAL = new NodeColor(Color.BLACK,
			Color.YELLOW);
	public static final NodeColor INSERT = new NodeColor(Color.WHITE,
			new Color(0x3366ff));
	public static final NodeColor FIND = new NodeColor(Color.BLACK,
			Color.LIGHT_GRAY);
	public static final NodeColor FOUND = GREEN;
	public static final NodeColor NOTFOUND = RED;
	public static final NodeColor DELETE = RED;
	public static final NodeColor CACHED = new NodeColor(Color.BLACK,
			Color.PINK);
	public static final NodeColor DELETED = new NodeColor(Color.BLACK,
			Color.DARK_GRAY);

	public final Color bgColor, fgColor;

	public NodeColor(Color fgColor, Color bgColor) {
		this.fgColor = fgColor;
		this.bgColor = bgColor;
	}
}