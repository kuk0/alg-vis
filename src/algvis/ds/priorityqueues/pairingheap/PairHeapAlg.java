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
package algvis.ds.priorityqueues.pairingheap;

import algvis.core.Algorithm;
import algvis.core.DataStructure;
import algvis.ui.view.REL;

public abstract class PairHeapAlg extends Algorithm {
    public enum Pairing {
        NAIVE, LRRL // , FB, BF, MULTI, LAZYM
    }

    final PairingHeap H;
    PairHeapNode v;

    PairHeapAlg(DataStructure D) {
        super(D.panel);
        this.H = (PairingHeap) D;
    }

    void link(int i, int j) {

        if (!H.root[i].prec(H.root[j])) {
            final PairHeapNode w = H.root[i];
            H.root[i] = H.root[j];
            H.root[j] = w;
        }
        H.root[i].addChildLeft(H.root[j]);
        H.reposition();
    }

    void linkchlr(int i, int j) {
        // if (H.root[j].state == -1){
        PairHeapNode w = H.root[j].getChild();
        H.root[j].deleteChild(H.root[j].leftmostChild());
        if (!H.root[i].prec(w)) {
            final PairHeapNode w1 = H.root[i];
            H.root[i] = w;
            w = w1;
        }
        H.root[i].addChildLeft(w);
        H.reposition();
        // }
    }

    // H.root[i].shift(0, - PairingHeap.minsepy);
    // problemy s posunutim
    // zobereme hocake dieta a ostatne k nemu prilinkujeme.
    void pairNaive(int i) {
        final int j = H.root[i].numChildren();
        if (j > 0) {
            if (j == 1) {
                H.root[i] = H.root[i].getChild();
                H.root[i].setState(0);
                return;
            }
            addStep(H.root[i], REL.TOP, "pairnaive"); // pri naive sa vyberie hocktory a prilinkuju
            // sa k nemu ostatne
            pause();

            H.root[0] = H.root[i];
            H.root[i] = H.root[i].getChild();
            H.root[i].setParent(null);
            H.root[i].setState(0);
            H.root[0].deleteChild(H.root[0].leftmostChild()); // getChild().setParent(null);

            H.reposition();
            pause();
            for (int k = 1; k < j; k++) {
                linkchlr(i, 0);
                pause();
            }
        } else {
            // vymazat neviditelneho roota
            H.root[i] = null;
        }
    }

    void linkchrl(int i, int j) {
        // if (H.root[j].state == -1){
        PairHeapNode w = (PairHeapNode) H.root[j].rightmostChild();
        H.root[j].deleteChild(H.root[j].rightmostChild());
        if (!H.root[i].prec(w)) {
            final PairHeapNode w1 = H.root[i];
            H.root[i] = w;
            w = w1;
        }
        H.root[i].addChildLeft(w);
        H.reposition();
        // }
    }

    void pairLRRL(int i) {
        int j = H.root[i].numChildren();
        if (j > 0) {
            if (j == 1) {
                H.root[i] = H.root[i].getChild();
                H.root[i].setState(0);
                return;
            }

            // najprv pri parovani sa pracuje len s root[i]

            /*
             * H.root[0] = H.root[i]; H.root[i] = H.root[i].getChild();
             * H.root[i].setParent(null); H.root[i].setState(0);
             * H.root[0].deleteChild
             * (H.root[0].leftmostChild());//getChild().setParent(null);
             */

            H.reposition();
            addStep(H.root[i], REL.TOP, "pairlrrl1");
            pause();
            PairHeapNode w = H.root[i].getChild();
            PairHeapNode wr = H.root[i].getChild().getRight();

            for (int k = 1; k <= j / 2; k++) {
                if (w.prec(wr)) {
                    w.setRight(w.getRight().getRight());
                    w.addChildLeft(wr);
                    // w.getRight().setRight(null);
                    w = w.getRight();
                } else {
                    wr.addChildLeft(w);
                    w = wr.getRight();
                }
                if (w != null) {
                    wr = w.getRight();
                }
            }

            H.reposition();
            addStep(H.root[i], REL.TOP, "pairlrrl2"); // a teraz sa vyberie jeden vrchol a polinkuju
            // sa sprava dolava
            pause();

            j = H.root[i].numChildren();
            if (j > 0) {
                if (j == 1) {
                    H.root[i] = H.root[i].getChild();
                    H.root[i].setState(0);
                    return;
                }

                H.root[0] = H.root[i];
                H.root[i] = (PairHeapNode) H.root[i].rightmostChild();
                H.root[i].setParent(null);
                H.root[i].setState(0);
                H.root[0].deleteChild(H.root[0].rightmostChild());// getChild().setParent(null);

                H.reposition();
                // addStep(); //pri naive sa vyberie hocktory a prilinkuju sa k
                // nemu ostatne
                pause();
                for (int k = 1; k < j; k++) {
                    linkchrl(i, 0);
                    pause();
                }
            } else {
                // vymazat neviditelneho roota
                H.root[i] = null;
            }
        } else {
            // vymazat neviditelneho roota
            H.root[i] = null;
        }
    }
}
