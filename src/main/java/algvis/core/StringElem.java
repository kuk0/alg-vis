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

import algvis.core.history.HashtableStoreSupport;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.undo.StateEditable;

public class StringElem implements StateEditable {
    private static final int span = 12;
    private final int x;
    private final int y;
    private int len;
    private final String s;
    private ArrayList<Color> col;
    private ArrayList<Boolean> marked;
    protected final String hash = Integer.toString(hashCode());

    public StringElem(String s, int x, int y) {
        this.s = s;
        this.x = x;
        this.y = y;
        len = s.length();
        col = new ArrayList<Color>();
        marked = new ArrayList<Boolean>();
        for (int i = 0; i < len; ++i) {
            col.add(NodeColor.DARKER.bgColor);
            marked.add(false);
        }
    }

    public void setColor(Color c, int a, int b) {
        if (a < 0) {
            a = 0;
        }
        if (b > len) {
            b = len;
        }
        for (int i = a; i < b; ++i) {
            col.set(i, c);
        }
    }

    public void mark(int i) {
        marked.set(i - 1, true);
    }

    public void unmark(int i) {
        marked.set(i - 1, false);
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
            if (marked.get(i)) {
                v.drawArrow(x0, y - 32, x0, y - 22);
            }
            x0 += span;
        }
        v.setColor(NodeColor.NORMAL.fgColor);
        v.drawRoundRectangle(x, y, len * span / 2 + 7, Node.RADIUS,
            2 * Node.RADIUS, 2 * Node.RADIUS);
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        HashtableStoreSupport.store(state, hash + "len", len);
        HashtableStoreSupport.store(state, hash + "col", col.clone());
        HashtableStoreSupport.store(state, hash + "marked", marked.clone());
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        final Object len = state.get(hash + "len");
        if (len != null) {
            this.len = (Integer) HashtableStoreSupport.restore(len);
        }
        final Object col = state.get(hash + "col");
        if (col != null) {
            this.col = (ArrayList<Color>) HashtableStoreSupport.restore(col);
        }
        final Object marked = state.get(hash + "marked");
        if (marked != null) {
            this.marked = (ArrayList<Boolean>) HashtableStoreSupport
                .restore(marked);
        }
    }
}
