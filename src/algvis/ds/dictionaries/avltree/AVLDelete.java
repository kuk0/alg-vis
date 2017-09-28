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
package algvis.ds.dictionaries.avltree;

import java.util.HashMap;

import algvis.core.Algorithm;
import algvis.core.StringUtils;
import algvis.ds.dictionaries.bst.BSTDelete;
import algvis.ui.view.REL;

public class AVLDelete extends Algorithm {
    private final AVL T;
    private final int K;

    public AVLDelete(AVL T, int x) {
        super(T.panel);
        this.T = T;
        K = x;
    }

    @Override
    public void runAlgorithm() {
        setHeader("delete", K);
        addNote("bstdeletestart");
        //final BSTFind find = new BSTFind(T, K, this);
        //find.runAlgorithm();
        final BSTDelete delete = new BSTDelete(T, K, this);
        delete.runAlgorithm();

        final HashMap<String, Object> deleteResult = delete.getResult();
        final boolean deleted = (Boolean) deleteResult.get("deleted");

        if (deleted) {
            AVLNode w = (AVLNode) deleteResult.get("parent");
            addStep("avldeletebal");
            pause();

            // update balance on the path up
            // TODO: deduplicate
            while (w != null) {
                w.mark();
                w.calc();
                addStep(w, REL.TOP, "avlupdatebal",
                    StringUtils.signedInt(w.balance()));
                pause();
                if (w.balance() == -2) {
                    if (w.getLeft().balance() != +1) { // R-rot
                        addStep(w, REL.TOP, "avlr");
                        w.unmark();
                        w = w.getLeft();
                        w.mark();
                        w.setArc(w.getParent());
                        pause();
                        w.noArc();
                        T.rotate(w);
                    } else { // LR-rot
                        addStep(w, REL.TOP, "avllr");
                        w.unmark();
                        w = w.getLeft().getRight();
                        w.mark();
                        w.setArc(w.getParent());
                        w.getParent().setArc(w.getParent().getParent());
                        pause();
                        w.noArc();
                        w.getParent().noArc();
                        T.rotate(w);
                        pause();
                        T.rotate(w);
                    }
                    pause();
                } else if (w.balance() == +2) {
                    if (w.getRight().balance() != -1) { // L-rot
                        addStep(w, REL.TOP, "avll");
                        w.unmark();
                        w = w.getRight();
                        w.mark();
                        w.setArc(w.getParent());
                        pause();
                        w.noArc();
                        T.rotate(w);
                    } else { // RL-rot
                        addStep(w, REL.TOP, "avlrl");
                        w.unmark();
                        w = w.getRight().getLeft();
                        w.mark();
                        w.setArc(w.getParent());
                        w.getParent().setArc(w.getParent().getParent());
                        pause();
                        w.noArc();
                        w.getParent().noArc();
                        T.rotate(w);
                        pause();
                        T.rotate(w);
                    }
                    pause();
                }
                w.unmark();
                w = w.getParent();
            }

            T.reposition();
            addNote("done");
        }
    }
}