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
package algvis.ds.dictionaries.splaytree;

import java.util.Hashtable;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.Fonts;
import algvis.ui.view.View;

public class SplayNode extends BSTNode {
    private int rank = 0;

    public SplayNode(DataStructure D, int key, int zDepth) {
        super(D, key, zDepth);
    }

    @Override
    public SplayNode getLeft() {
        return (SplayNode) super.getLeft();
    }

    @Override
    public SplayNode getRight() {
        return (SplayNode) super.getRight();
    }

    @Override
    public SplayNode getParent() {
        return (SplayNode) super.getParent();
    }

    @Override
    public void calc() {
        super.calc();
        int lp = 0, rp = 0;
        if (getLeft() != null) {
            lp = getLeft().rank;
        }
        if (getRight() != null) {
            rp = getRight().rank;
        }
        rank = (int) Math.floor(D.lg(size));
    }

    @Override
    public void draw(View v) {
        super.draw(v);
        v.drawString("" + rank, x + Node.RADIUS, y - Node.RADIUS, Fonts.SMALL);
    }

    @Override
    public void drawExtNodes(View v) {
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "rank", rank);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object r = state.get(hash + "rank");
        if (r != null) {
            this.rank = (Integer) HashtableStoreSupport.restore(r);
        }
    }
}
