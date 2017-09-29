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

import algvis.core.MyRandom;
import algvis.core.Node;
import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ui.view.REL;

public class SkipInsert extends SkipAlg {

    public SkipInsert(SkipList L, int x) {
        super(L, x);
    }

    @Override
    public void runAlgorithm() {
        setHeader("insert", K);
        p = new SkipNode[L.height];
        v = new SkipNode(L, K, ZDepth.ACTIONNODE);
        v.setColor(NodeColor.INSERT);
        addToScene(v);
        addStep(L.getRoot(), REL.TOP, "skipinsertstart");
        SkipNode w = find();


        if (w.getKey() == v.getKey()) {
            addStep(v, REL.BOTTOM, "alreadythere");
            pause();
            v.setColor(NodeColor.NOTFOUND);
            v.goDown();
            removeFromScene(v);
            addNote("done");
            return;
        }
        final SkipNode inserted = v;

        L.n++;
        addStep(v, REL.BOTTOM, "skipinsertafter");
        pause();
        SkipNode z, oldv = null;
        addStep(v, REL.BOTTOM, "skiplist-tossing");
        addNote("skiplist-tossing");
        int i = 0;
        do {
            if (i > 0) {
                addStep(oldv, REL.TOP, "skiplist-head", "" + i);
                pause();
                L.e++;
            }
            addToScene(v);
            if (i < L.height) {
                w = p[i++];
                z = w.getRight();
                w.linkright(v);
                z.linkleft(v);
                if (oldv != null) {
                    v.linkdown(oldv);
                }
                L.reposition();
                oldv = v;
                v = new SkipNode(L, v.getKey(), v.tox, -10);
            } else {
                v.linkdown(oldv);
                final SkipNode oldr = L.getRoot(), olds = L.sent;
                v.linkleft(L.setRoot(new SkipNode(L, -Node.INF, L.getZDepth())));
                v.linkright(L.sent = new SkipNode(L, Node.INF, L.getZDepth()));
                L.getRoot().linkdown(oldr);
                L.sent.linkdown(olds);
                L.reposition();
                oldv = v;
                v = new SkipNode(L, v.getKey(), v.tox, -10);
                ++i;
                ++L.height;
            }
            removeFromScene(oldv);
            pause();
        } while (MyRandom.heads());

        addStep(oldv, REL.TOP, "skiplist-tail", "" + i);
        pause();
        addNote("done");

        inserted.setColor(NodeColor.NORMAL);
    }
}
