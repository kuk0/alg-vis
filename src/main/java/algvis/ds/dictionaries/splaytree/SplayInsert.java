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
            addStep("newroot");
            pause();
        } else {
            v.goAboveRoot();
            final SplayNode w = find(K);
            splay(w);

            w.setColor(NodeColor.NORMAL);
            if (w.getKey() == K) {
                addStep("alreadythere");
                v.goDown();
                v.setColor(NodeColor.NOTFOUND);
                removeFromScene(v);
                return;
            } else if (w.getKey() < K) {
                addNote("splay-insert-left", K);
                addStep("splay-insert-left2", K);
                pause();
                v.linkLeft(w);
                v.linkRight(w.getRight());
                w.setRight(null);
            } else {
                addNote("splay-insert-right", K);
                addStep("splay-insert-right2", K);
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
