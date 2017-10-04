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

import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ui.view.REL;

public class SplayFind extends SplayAlg {

    public SplayFind(SplayTree T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        setHeader("find", K);
        final SplayNode v = new SplayNode(T, K, ZDepth.ACTIONNODE);
        v.setColor(NodeColor.FIND);
        addToScene(v);
        if (T.getRoot() == null) {
            v.goToRoot();
            addStep(v, REL.BOTTOM, "bstfindempty");
            pause();
            v.goDown();
            v.setColor(NodeColor.NOTFOUND);
            addStep(v, REL.BOTTOM, "bstfindnotfound");
        } else {
            v.goAboveRoot();
            final SplayNode w = find(K);
            splay(w);

            addStep(w, REL.BOTTOM, "splayinroot");
            pause();

            w.setColor(NodeColor.NORMAL);
            v.goToRoot();
            if (w.getKey() == v.getKey()) {
                addStep(w, REL.BOTTOM, "found");
                v.setColor(NodeColor.FOUND);
            } else {
                addStep(w, REL.BOTTOM, "notfound");
                v.setColor(NodeColor.NOTFOUND);
                v.goDown();
            }
            pause();
        }
        removeFromScene(v);
    }
}
