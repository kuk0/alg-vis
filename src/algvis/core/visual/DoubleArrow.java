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

package algvis.core.visual;

import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;

import algvis.ui.view.View;

public class DoubleArrow extends VisualElement {
    int x1, y1, x2, y2;

    public DoubleArrow(int x1, int y1, int x2, int y2) {
        super(0);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    protected void draw(View v) throws ConcurrentModificationException {
        v.drawDoubleArrow(x1, y1, x2, y2);
    }

    @Override
    protected void move() throws ConcurrentModificationException {
    }

    @Override
    protected Rectangle2D getBoundingBox() {
        return new Rectangle2D.Double(x1, y1, x2, y2);
    }
}