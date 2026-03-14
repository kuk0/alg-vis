/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public enum Fonts {
    NORMAL(initSourceSans(9.0f)), SMALL(initSourceSans(7.0f)), TYPEWRITER(
        initTT());

    public final Font font;
    public FontMetrics fm;

    private Fonts(Font f) {
        font = f;
    }

    private static Font sourceSansBase;

    private static Font initSourceSans(float size) {
        Font f = null;
        try {
            if (sourceSansBase == null) {
                sourceSansBase = Font.createFont(Font.TRUETYPE_FONT, Fonts.class
                    .getResourceAsStream("SourceSansPro-Regular.otf"));
            }
            f = sourceSansBase.deriveFont(size);
        } catch (final Exception e) {
            e.printStackTrace();
            // Last resort: use a logical font (may trigger remapping under
            // CheerpJ)
            f = new Font(Font.SANS_SERIF, Font.PLAIN, (int) size);
        }
        return f;
    }

    /** Return SourceSansPro at the given size, for use outside this enum. */
    public static Font getSourceSans(float size) {
        if (sourceSansBase != null) {
            return sourceSansBase.deriveFont(size);
        }
        return new Font(Font.SANS_SERIF, Font.PLAIN, (int) size);
    }

    /** Return SourceSansPro-Italic at the given size. */
    public static Font getSourceSansItalic(float size) {
        try {
            Font f = Font.createFont(Font.TRUETYPE_FONT,
                Fonts.class.getResourceAsStream("SourceSansPro-It.otf"));
            return f.deriveFont(size);
        } catch (final Exception e) {
            e.printStackTrace();
            return getSourceSans(size).deriveFont(Font.ITALIC);
        }
    }

    private static Font initTT() {
        Font f = null;
        try {
            f = Font.createFont(Font.TRUETYPE_FONT,
                Fonts.class.getResourceAsStream("FreeMonoBold.ttf"));
            f = f.deriveFont(10.0f);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static void init(Graphics g) {
        for (final Fonts f : Fonts.values()) {
            f.fm = g.getFontMetrics(f.font);
        }
    }
}
