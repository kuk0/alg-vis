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

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.ds.intervaltree.IntervalTrees.mimasuType;
import algvis.ui.view.REL;

abstract class IntervalAlg extends Algorithm {
    IntervalTree T;
    IntervalNode v;

    IntervalAlg(IntervalTree T) {
        super(T.panel);
        this.T = T;
    }

    void adjustValues(IntervalNode w) {
        while (w != null) {
            w.mark();
            if ((w.getRight() != null) && (w.getLeft() != null)) {
                if (T.minTree == mimasuType.MIN) {
                    if (w.getRight().getKey() == Node.NOKEY) {
                        w.setKey(w.getLeft().getKey());
                        addStep(w, REL.TOP, "intervalkeyempty", w.getLeft().getKeyS());
                    } else {
                        w.setKey(Math.min(w.getRight().getKey(), w.getLeft()
                            .getKey()));
                        addStep(w, REL.TOP, "intervalmin", w.getRight().getKeyS(), w
                            .getLeft().getKeyS());
                    }
                } else if (T.minTree == mimasuType.MAX) {
                    w.setKey(Math.max(w.getRight().getKey(), w.getLeft()
                        .getKey()));
                    if (w.getRight().getKey() != Node.NOKEY) {
                        addStep(w, REL.TOP, "intervalmax", w.getRight().getKeyS(), w
                            .getLeft().getKeyS());
                    } else {
                        addStep(w, REL.TOP, "intervalkeyempty", w.getLeft().getKeyS());
                    }
                } else if (T.minTree == mimasuType.SUM) {
                    if (w.getRight().getKey() != Node.NOKEY) {
                        w.setKey(w.getRight().getKey() + w.getLeft().getKey());
                        addStep(w, REL.TOP, "intervalsum", w.getRight().getKeyS(), w
                            .getLeft().getKeyS());
                    } else {
                        w.setKey(w.getLeft().getKey());
                        addStep(w, REL.TOP, "intervalkeyempty", w.getLeft().getKeyS());
                    }
                }
                w.setInterval(w.getLeft().b, w.getRight().e);
                // System.out.println(w.getKey() + " " + w.b + " " + w.e);
                pause();
                // System.out.println(w.b + " " + w.e);
            }
            w.unmark();
            w = w.getParent();
        }
    }

}
