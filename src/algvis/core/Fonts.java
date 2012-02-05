package algvis.core;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class Fonts {
	static final int MIN = 5;
	static final int MAX = 33;
	public static Font[] f;
	public static FontMetrics[] fm;

	public static void init(Graphics g) {
		f = new Font[MAX + 1];
		fm = new FontMetrics[MAX + 1];
		for (int i = MIN; i <= MAX; ++i) {
			f[i] = new Font("Sans-serif", Font.PLAIN, i); // "Helvetica"
			fm[i] = g.getFontMetrics(f[i]);
		}
	}
}
