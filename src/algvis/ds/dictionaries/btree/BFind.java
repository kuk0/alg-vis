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

public class BFind extends BAlg {

    public BFind(BTree T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        final BNode v = new BNode(T, K);
        v.setColor(NodeColor.FIND);
        addToScene(v);
        setHeader("search");
        if (T.getRoot() == null) {
            v.goToRoot();
            addStep(T.getBoundingBoxDef(), 200, REL.TOP, "empty");
            pause();
            v.goDown();
            v.setColor(NodeColor.NOTFOUND);
            removeFromScene(v);
            addStep(T.getBoundingBoxDef(), 200, REL.TOP, "notfound");
        } else {
            BNode w = T.getRoot();
            v.goTo(w);
            addStep(v, REL.TOP, "bstfindstart");
            pause();

            while (true) {
                if (w.isIn(K)) {
                    addStep(w, REL.BOTTOM, "found");
                    v.goDown();
                    v.setColor(NodeColor.FOUND);
                    break;
                }
                if (w.isLeaf()) {
                    addStep(w, REL.BOTTOM, "notfound");
                    v.setColor(NodeColor.NOTFOUND);
                    v.goDown();
                    break;
                }
                w = goToChild(w, v);
            }
        }
        removeFromScene(v);
    }
}
