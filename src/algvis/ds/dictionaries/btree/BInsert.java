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
package algvis.ds.dictionaries.btree;

import algvis.core.NodeColor;
import algvis.ui.view.REL;

public class BInsert extends BAlg {

    public BInsert(BTree T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        setHeader("insert", K);
        final BNode v = new BNode(T, K);
        v.setColor(NodeColor.INSERT);
        addToScene(v);
        if (T.getRoot() == null) {
            T.setRoot(v);
            v.goAboveRoot();
            addStep(v, REL.TOP, "newroot");
            pause();
            v.setColor(NodeColor.NORMAL);
            removeFromScene(v);
        } else {
            BNode w = T.getRoot();
            v.goAbove(w);
            addStep(v, REL.TOP, "bst-insert-start");
            pause();

            while (true) {
                if (w.isIn(K)) {
                    addStep(w, REL.BOTTOM, "alreadythere");
                    v.goDown();
                    removeFromScene(v);
                    return;
                }
                if (w.isLeaf()) {
                    break;
                }
                w = goToChild(w, v);
            }

            addStep(w, REL.BOTTOM, "binsertleaf");
            w.addLeaf(K);
            if (w.numKeys >= T.order) {
                w.setColor(NodeColor.NOTFOUND);
            }
            removeFromScene(v);
            pause();

            while (w.numKeys >= T.order) {
                addStep(w, REL.BOTTOM, "bsplit");
                final int o = (w.parent != null) ? w.order() : -1;
                w = w.split();
                if (w.parent == null) {
                    break;
                }
                w.parent.c[o] = w;
                pause();
                w.goBelow(w.parent);
                pause();
                w.parent.add(o, w);
                w = w.parent;
                if (w.numKeys >= T.order) {
                    w.setColor(NodeColor.NOTFOUND);
                }
                T.reposition();
                pause();
            }
            if (w.isRoot()) {
                T.setRoot(w);
            }
            T.reposition();
        }
        addNote("done");
    }
}
