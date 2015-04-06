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

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.ui.view.REL;

abstract class SkipAlg extends Algorithm {
    final SkipList L;
    SkipNode v;
    SkipNode p[];
    final int K;

    SkipAlg(SkipList L, int x) {
        super(L.panel);
        this.L = L;
        K = x;
    }

    SkipNode find() {
        SkipNode w = L.getRoot();
        v.goToRoot();
        pause();

        for (int i = L.height - 1;; --i) {
            while (w.getRight().getKey() <= K) {
                addStep("skiplist-next", K);
                addStep(v, REL.TOP, "skiplist-next", K);
                pause();
                w = w.getRight();
                w.colorBefore(NodeColor.DARKER);
                v.goTo(w);
                pause();
            }
            if (w.getRight().getKey() > K) {
                w.getRight().colorAfter(NodeColor.DARKER);
            }
            p[i] = w;
            if (w.getDown() == null) {
                break;
            }
            addStep("skiplist-down", K);
            addStep(v, REL.TOP, "skiplist-down", K);
            pause();
            w = w.getDown();
            v.goTo(w);
            pause();
        }
        L.getRoot().colorAfter(NodeColor.NORMAL);
        return w;
    }
}
