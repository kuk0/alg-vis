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
package algvis.ds.dictionaries.skiplist;

import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ui.view.REL;

public class SkipDelete extends SkipAlg {
    public SkipDelete(SkipList L, int x) {
        super(L, x);
    }

    @Override
    public void runAlgorithm() {
        setHeader("delete", K);
        v = new SkipNode(L, K, ZDepth.ACTIONNODE);
        v.setColor(NodeColor.DELETE);
        addToScene(v);
        p = new SkipNode[L.height];
        addStep(L.getRoot(), REL.TOP, "bstdeletestart");
        SkipNode w = find();

        if (w.getKey() != K) {
            addStep(w, REL.BOTTOM, "notfound");
            pause();
            v.setColor(NodeColor.NOTFOUND);
            v.goDown();
            removeFromScene(v);
            return;
        }
        addStep(w, REL.BOTTOM, "skiplist-delete-found");
        pause();
        removeFromScene(v);

        addNote("skiplist-delete-found");

        L.n--;
        L.e++;
        addStep(w, REL.BOTTOM, "skipdelete");
        for (int i = 0; i < L.height; ++i) {
            if (w == null || w.getKey() != K) {
                break;
            }
            L.e--;
            final SkipNode left = w.getLeft(), right = w.getRight(),
                up = w.getUp();
            left.linkright(right);
            if (up != null) {
                up.setDown(null);
            }
            w.setColor(NodeColor.DELETE);
            addToScene(w);
            w.isolate();
            w.goDown();
            pause();
            removeFromScene(w);
            w = up;
            if (i > 0 && left.getKey() == -Node.INF
                && right.getKey() == Node.INF) {
                L.setRoot(left.getDown());
                L.sent = right.getDown();
                L.getRoot().setUp(null);
                L.sent.setUp(null);
                L.height = i;
                break;
            }
        }

        addNote("done");
        L.reposition();
    }
}
