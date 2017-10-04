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

public class SplayInsert extends SplayAlg {
    public SplayInsert(SplayTree T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        setHeader("insert", K);
        final SplayNode v = new SplayNode(T, K, ZDepth.ACTIONNODE);
        v.setColor(NodeColor.INSERT);
        addToScene(v);
        if (T.getRoot() == null) {
            T.setRoot(v);
            v.goToRoot();
            addStep(v, REL.TOP, "newroot");
            pause();
        } else {
            v.goAboveRoot();
            final SplayNode w = find(K);
            splay(w);

            w.setColor(NodeColor.NORMAL);
            if (w.getKey() == K) {
                addStep(w, REL.BOTTOM, "alreadythere");
                v.goDown();
                v.setColor(NodeColor.NOTFOUND);
                removeFromScene(v);
                return;
            } else if (w.getKey() < K) {
                addNote("splay-insert-left", K);
                addStep(w, REL.TOP, "splay-insert-left2", KS);
                pause();
                v.linkLeft(w);
                v.linkRight(w.getRight());
                w.setRight(null);
            } else {
                addNote("splay-insert-right", K);
                addStep(w, REL.TOP, "splay-insert-right2", KS);
                pause();
                v.linkRight(w);
                v.linkLeft(w.getLeft());
                w.setLeft(null);
            }
            T.setRoot(v);
            T.reposition();
            pause();
        }
        addNote("done");
        v.setColor(NodeColor.NORMAL);
        removeFromScene(v);
    }
}
