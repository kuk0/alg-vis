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
package algvis.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public enum Fonts {
	NORMAL(new Font(Font.SANS_SERIF, Font.PLAIN, 9)),
	SMALL(new Font(Font.SANS_SERIF, Font.PLAIN, 7)),
	TYPEWRITER(initTT());
	//new Font("FreeMono", Font.BOLD, 10));

	public Font font;
	public FontMetrics fm;

	private Fonts(Font f) {
		font = f;
	}

	public static Font initTT() {
		Font f = null;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT,
					Fonts.class.getResourceAsStream("FreeMonoBold.ttf"));
			f = f.deriveFont(10.0f);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return f;
	}

	public static void init(Graphics g) {
		for (Fonts f : Fonts.values()) {
			f.fm = g.getFontMetrics(f.font);
		}
	}
}
