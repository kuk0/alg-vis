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
package algvis.ds.dictionaries.splaytree;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.visual.ShadePair;
import algvis.core.visual.ShadeTriple;
import algvis.core.visual.ZDepth;
import algvis.ui.view.REL;

abstract class SplayAlg extends Algorithm {
    final SplayTree T;
    protected final int K;
    protected final String KS;

    SplayAlg(SplayTree T, int x) {
        super(T.panel, null);
        this.T = T;
        K = x;
        KS = "" + K;
    }

    SplayNode find(int K) {
        SplayNode w = (SplayNode) T.getRoot();
        final SplayNode s = new SplayNode(T, this.K, ZDepth.ACTIONNODE);
        s.setColor(NodeColor.FIND);
        addToScene(s);
        s.goTo(w);
        addNote("splay-start", K);
        pause();
        while (true) {
            if (w.getKey() == K) {
                addNote("splay-found");
                break;
            } else if (w.getKey() < K) { // right
                if (w.getRight() == null) {
                    addNote("splay-lower", K, w.getKey());
                    break;
                }
                addStep(w, REL.TOP, "bstfindright", KS, w.getKeyS());
                w = w.getRight();
            } else { // left
                if (w.getLeft() == null) {
                    addNote("splay-higher", K, w.getKey());
                    break;
                }
                addStep(w, REL.TOP, "bstfindleft", KS, w.getKeyS());
                w = w.getLeft();
            }
            s.goTo(w);
            pause();
        }
        w.setColor(NodeColor.FIND);
        removeFromScene(s);
        pause();
        return w;
    }

    void splay(SplayNode w) {
        while (!w.isRoot()) {
            if (w.getParent().isRoot()) {
                final ShadePair shade = new ShadePair(w, w.getParent());
                addToScene(shade);
                addNote("splay-root");
                w.setArc(w.getParent());
                pause();
                w.noArc();
                T.rotate(w);
                pause();
                removeFromScene(shade);
            } else {
                final ShadeTriple shade = new ShadeTriple(w, w.getParent(), w
                    .getParent().getParent());
                addToScene(shade);
                if (w.isLeft() == w.getParent().isLeft()) {
                    if (w.isLeft()) {
                        addNote("splay-zig-zig-left", w.getKey(), w.getParent()
                            .getKey());
                    } else {
                        addNote("splay-zig-zig-right", w.getKey(), w
                            .getParent().getKey());
                    }
                    addStep(w, REL.BOTTOM, "rotate", w.getParent().getKeyS());
                    w.getParent().setArc(w.getParent().getParent());
                    pause();
                    w.getParent().noArc();
                    T.rotate(w.getParent());
                    w.setArc(w.getParent());
                    addStep(w, REL.BOTTOM, "rotate", w.getKeyS());
                    pause();
                    w.noArc();
                    T.rotate(w);
                    pause();
                } else {
                    if (!w.isLeft()) {
                        addNote("splay-zig-zag-left", w.getKey(), w.getParent()
                            .getKey());
                    } else {
                        addNote("splay-zig-zag-right", w.getKey(), w
                            .getParent().getKey());
                    }
                    w.setArc(w.getParent());
                    addStep(w, REL.BOTTOM, "rotate", w.getKeyS());
                    pause();
                    w.noArc();
                    T.rotate(w);
                    w.setArc(w.getParent());
                    addStep(w, REL.BOTTOM, "rotate", w.getKeyS());
                    pause();
                    w.noArc();
                    T.rotate(w);
                    pause();
                }
                removeFromScene(shade);
            }
        }
        T.setRoot(w);
    }
}
