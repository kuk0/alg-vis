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

package algvis.ds.intervaltree;

import algvis.core.visual.ZDepth;

public class IntervalInsert extends IntervalAlg {
    private final int K;

    public IntervalInsert(IntervalTree T, int x) {
        super(T);
        this.T = T;
        if (x > 333) {
            x = x / 3;
        }
        K = x;
    }

    @Override
    public void runAlgorithm() {
        setHeader("insert", K);
        v = new IntervalNode(T, K, ZDepth.ACTIONNODE);
        v.setInterval(T.numLeafs + 1, T.numLeafs + 1);
        // v.setColor(NodeColor.INSERT);

        T.reposition();

        if (T.root == null) {
            T.root = v;
            v = null;
            if (T.root != null) {
                T.numLeafs++;
                addStep("newroot");
            }
            T.reposition();
            // heap #1 is empty; done;
            return;
        }

        int tmp = T.numLeafs;
        while (tmp % 2 == 0) {
            tmp /= 2;
        }

        if (tmp == 1) {
            T.extend();
            addStep("intervalextend");
            pause();
        }
        T.reposition();
        // pause();

        /*
         * T.root.linkRight(v); T.reposition();
         */
        /*
         * pridaj na T.numLeafs + 1 novy prvok;
         */

        IntervalNode w;
        // T.numLeafs++;
        final int n = T.numLeafs;
        int k = 1 << 10;
        if (n == 0) {
            T.root = w = v;
            v.goToRoot();
            pause();
        } else {
            while ((k & n) == 0) {
                k >>= 1;
            }
            // k >>= 1;
            w = T.root;
            while (k > 1) {
                w = ((n & k) == 0) ? w.getLeft() : w.getRight();
                k >>= 1;
            }
            if ((k & n) == 0) {
                w.linkLeft(v);
            } else {
                w.linkRight(v);
            }
            T.reposition();
        }

        T.numLeafs++;
        addNote("intervalinsert");
        v.mark();
        pause();
        v.unmark();
        /*
         * uprav strom na min/max z intervalu;
         */
        // w = w.getParent();
        // toto prerobit na iba prechod od prave pridaneho vrcholu po koren
        adjustValues(w);
        addNote("done");
    }
}
