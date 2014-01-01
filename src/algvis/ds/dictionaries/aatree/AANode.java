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
package algvis.ds.dictionaries.aatree;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.Fonts;
import algvis.ui.view.View;

public class AANode extends BSTNode {
    private AANode(DataStructure D, int key, int x, int y, int zDepth) {
        super(D, key, x, y, zDepth);
        setLevel(1);
    }

    public AANode(DataStructure D, int key, int zDepth) {
        this(D, key, 0, Node.UPY, zDepth);
    }

    @Override
    public AANode getLeft() {
        return (AANode) super.getLeft();
    }

    @Override
    public AANode getRight() {
        return (AANode) super.getRight();
    }

    @Override
    public AANode getParent() {
        return (AANode) super.getParent();
    }

    @Override
    public void draw(View v) {
        if (state == Node.INVISIBLE || getKey() == NULL) {
            return;
        }
        drawBg(v);
        drawKey(v);
        drawArrow(v);
        drawArc(v);
        final String str = "" + getLevel();
        v.drawString(str, x + Node.RADIUS, y - Node.RADIUS, Fonts.SMALL);
    }

    void drawBigNodes(View v) {
        if (getLeft() != null) {
            getLeft().drawBigNodes(v);
        }
        if (getRight() != null) {
            getRight().drawBigNodes(v);
        }
        if (getParent() != null && getParent().getLevel() == getLevel()) {
            v.drawWideLine(x, y, getParent().x, getParent().y);
        } else {
            v.drawWideLine(x - 1, y, x + 1, y);
        }
    }

    public void drawTree2(View v) {
        if (((AA) D).mode23) {
            drawBigNodes(v);
        }
        drawTree(v);
    }
}
