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
    public static final NodeColor DARKER = new NodeColor(Color.BLACK,
        new Color(0xCDCD00));

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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NodeColor && bgColor == ((NodeColor) obj).bgColor
            && fgColor == ((NodeColor) obj).fgColor;
    }
}
