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
package algvis.ds.dictionaries.treap;

import algvis.core.Algorithm;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTInsert;
import algvis.ui.view.REL;

public class TreapInsert extends Algorithm {
    private final Treap T;
    private final int K;

    public TreapInsert(Treap T, int x) {
        super(T.panel);
        this.T = T;
        K = x;
    }

    @Override
    public void runAlgorithm() {
        setHeader("insert", K);
        TreapNode v = (TreapNode) new BSTInsert(T, K)
            .insert(new TreapNode(T, K, ZDepth.ACTIONNODE)).orElse(null);

        if (v != null) {
            pause();
            // bubleme nahor
            addStep(v, REL.BOTTOM, "treapbubbleup");
            v.mark();
            pause();
            while (!v.isRoot() && v.getParent().p < v.p) {
                T.rotate(v);
                pause();
            }
            v.unmark();
            addNote("done");
        }
    }
}
