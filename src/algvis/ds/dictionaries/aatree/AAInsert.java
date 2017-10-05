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
package algvis.ds.dictionaries.aatree;

import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTInsert;

public class AAInsert extends AAAlg {

    public AAInsert(AA T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        setHeader("insert", K);
        AANode w = (AANode) new BSTInsert(T, K)
            .insert(new AANode(T, K, ZDepth.ACTIONNODE)).orElse(null);

        if (w != null) {
            pause();

            // bubleme nahor
            while (w != null) {
                w.mark();
                // addStep(w, REL.BOTTOM, "aaok");
                // skew
                if (w.leftPseudoNode()) {
                    AANode l = w.getLeft();
                    skew(w, "aaskew");
                    w.unmark();
                    w = l;
                    w.mark();
                }
                // split
                if (w.pseudoNodeTooBig()) {
                    AANode r = w.getRight();
                    split(w, "aasplit");
                    w.unmark();
                    w = r;
                    w.mark();
                }
                pause();
                w.unmark();
                w = w.getParent();
            }
            T.reposition();
            addNote("done");
        }
    }
}
