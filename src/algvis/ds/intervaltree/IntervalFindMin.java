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
import algvis.ui.view.REL;

public class IntervalFindMin extends IntervalAlg {
    private int i;
    private int j;
    // Aggregation result node. Its meaning depends on
    // the configured aggregation type: maximum, minimum or sum.
    private IntervalNode aggregate;

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
        // Initialize the aggregation accumulator node according to the
        // configured aggregation type (max/min/sum).
        if (T.minTree == mimasuType.MAX) {
            aggregate = new IntervalNode(T, Integer.MIN_VALUE, ZDepth.NODE);
            setHeader("findmax", i, j);
        } else if (T.minTree == mimasuType.MIN) {
            aggregate = new IntervalNode(T, Integer.MAX_VALUE, ZDepth.NODE);
            setHeader("findmin", i, j);
        } else {
            aggregate = new IntervalNode(T, 0, ZDepth.NODE);
            setHeader("findsum", i, j);
        }
        T.markColor(T.root, i, j);

        // kazdy vrchol ma zapamatany interval, ktory reprezentuje (je to
        // b-e+1=2^k

        if (T.root != null) {
            // We have to find the nodes that represent the
            // interval <i,j>. We will search for these nodes with DFS.
            addNote("intervalfind", i, j);
            find(T.root, i, j);
            pause();
            if (T.minTree == mimasuType.MAX) {
                addStep(T, 200, REL.TOP, "maximum", aggregate.getKeyS());
            } else if (T.minTree == mimasuType.MIN) {
                addStep(T, 200, REL.TOP, "minimum", aggregate.getKeyS());
            } else if (T.minTree == mimasuType.SUM) {
                addStep(T, 200, REL.TOP, "sum", aggregate.getKeyS());
            }
            if (T.minTree != mimasuType.SUM) {
                T.unfocus(T.root);
                aggregate.mark();
                T.markColor(T.root, i, j);
            }
            pause();
            T.unfocus(T.root);
            addNote("done");
        } else {
            // addNote(); //strom je prazdny/zly interval
        }
    }

    void find(IntervalNode w, int b, int e) {

        w.mark();

        // Case 1: disjoint (node interval is completely outside query)
        if ((w.b > e) || (w.e < b)) {
            if (w.getKey() != Node.NOKEY) {
                addStep(w, REL.TOP, "intervalout", "" + i, "" + j, w.getKeyS(),
                    "" + w.b, "" + w.e);
            } else {
                addStep(w, REL.TOP, "intervalempty", "" + w.b, "" + w.e);
            }
            w.focused = focusType.TOUT;
            pause();
            w.unmark();
            w.focused = focusType.FALSE;
            return;
        }

        // Case 2: contained (node interval is fully inside query)
        else if ((w.b >= b) && (w.e <= e)) {
            if (T.minTree != mimasuType.SUM) {
                if (w.prec(aggregate)) {
                    aggregate = w;
                }
            } else {
                aggregate.setKey(aggregate.getKey() + w.getKey());
            }
            addStep(w, REL.TOP, "intervalin", "" + i, "" + j, w.getKeyS(),
                "" + w.b, "" + w.e);
            w.focused = focusType.TIN;
            pause();
            return;
        }

        // Case 3: partial intersection (node interval overlaps query but is not contained)
        else {
            addStep(w, REL.TOP, "intervalpart", "" + i, "" + j, w.getKeyS(),
                "" + w.b, "" + w.e);
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
