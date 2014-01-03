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

import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.view.View;

public class ShadeTriple extends VisualElement {
    BSTNode u, v, w;

    public ShadeTriple(BSTNode u, BSTNode v, BSTNode w) {
        super(Scene.MAXZ - 1);
        this.u = u;
        this.v = v;
        this.w = w;
    }

    @Override
    protected void draw(View V) {
        BSTNode z;
        if (u != null) {
            z = u.getParent();
            if (z == v || z == w) {
                V.drawWideLine(u.x, u.y, z.x, z.y);
            }
        }
        if (v != null) {
            z = v.getParent();
            if (z == u || z == w) {
                V.drawWideLine(v.x, v.y, z.x, z.y);
            }
        }
        if (w != null) {
            z = w.getParent();
            if (z == u || z == v) {
                V.drawWideLine(w.x, w.y, z.x, z.y);
            }
        }
    }

    @Override
    protected void move() {
    }

    @Override
    protected Rectangle2D getBoundingBox() {
        return null;
    }
}
