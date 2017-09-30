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

import algvis.core.Node;
import algvis.core.visual.ZDepth;
import algvis.ds.intervaltree.IntervalNode.focusType;
import algvis.ds.intervaltree.IntervalTrees.mimasuType;

public class IntervalFindMin extends IntervalAlg {
    private int i;
    private int j;
    private IntervalNode maxi;
    private static final int ninf = -2147483648;
    private static final int pinf = 2147483647;

    public IntervalFindMin(IntervalTree T, int i, int j) {
        super(T);
        this.T = T;
        this.i = i;
        this.j = j;
    }

    @Override
    public void runAlgorithm() {
        if (i > j) {
            final int tmp = j;
            j = i;
            i = tmp;
        }
        if (i < 1 || i > T.numLeafs) {
            i = 1;
        }
        if (j > T.numLeafs || j < 1) {
            j = T.numLeafs;
        }
        if (T.minTree == mimasuType.MAX) {
            maxi = new IntervalNode(T, ninf, ZDepth.NODE);
            setHeader("findmax", i, j);
        } else if (T.minTree == mimasuType.MIN) {
            maxi = new IntervalNode(T, pinf, ZDepth.NODE);
            setHeader("findmin", i, j);
        } else {
            maxi = new IntervalNode(T, 0, ZDepth.NODE);
            setHeader("findsum", i, j);
        }
        T.markColor(T.root, i, j);
        // System.out.println(i + " " + j);
        // System.out.println(T.root.b + " " + T.root.e);

        // kazdy vrchol ma zapamatany interval, ktory reprezentuje (je to
        // b-e+1=2^k

        if (T.root != null) {
            // We have to find the nodes that represent the
            // interval <i,j>. We will search for these nodes with DFS.
            addNote("intervalfind", i, j);
            find(T.root, i, j);
            pause();
            if (T.minTree == mimasuType.MAX) {
                addStep("maximum", maxi.getKey());
            } else if (T.minTree == mimasuType.MIN) {
                addStep("minimum", maxi.getKey());
            } else if (T.minTree == mimasuType.SUM) {
                addStep("sumimum", maxi.getKey());
            }
            if (T.minTree != mimasuType.SUM) {
                T.unfocus(T.root);
                maxi.mark();
                T.markColor(T.root, i, j);
            }
            pause();
            // if (T.minTree == mimasuType.SUM){
            // }
            T.unfocus(T.root);
            addNote("done");
        } else {
            // addNote(); //strom je prazdny/zly interval
        }
    }

    void find(IntervalNode w, int b, int e) {

        w.mark();
        // w.markSubtree = true;

        if ((w.b > e) || (w.e < b)) {
            if (w.getKey() != Node.NOKEY) {
                addStep("intervalout", i, j, w.getKey(), w.b, w.e); // mimo intervalu
            } else {
                addStep("intervalempty", w.b, w.e); // prazdny vrchol
            }
            w.focused = focusType.TOUT;
            pause();
            w.unmark();
            w.focused = focusType.FALSE;
            return;
        }

        if ((w.b >= b) && (w.e <= e)) {
            if (T.minTree != mimasuType.SUM) {
                if (w.prec(maxi)) {
                    maxi = w;
                }
            } else {
                maxi.setKey(maxi.getKey() + w.getKey());
            }
            addStep("intervalin", i, j, w.getKey(), w.b, w.e); // dnu intervalu
            w.focused = focusType.TIN;
            pause();
            // w.unmark();
            // w.unfocus();
            return;
        }

        if ((w.b <= b) || (w.e >= e)) {
            addStep("intervalpart", i, j, w.getKey(), w.b, w.e); // neprazdny
                                                                 // prienik
            w.focused = focusType.TOUT;
            pause();
            w.focused = focusType.TWAIT;
            find(w.getLeft(), b, e);
            find(w.getRight(), b, e);
        }
        w.unmark();
        w.focused = focusType.FALSE;
    }
}
