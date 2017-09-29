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
package algvis.ds.dictionaries.scapegoattree;

import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.view.REL;

public class GBFind extends GBAlg {
    public GBFind(GBTree T, int x) {
        super(T, x);
    }

    @Override
    public void runAlgorithm() {
        setHeader("find", K);
        v = new GBNode(T, K, ZDepth.ACTIONNODE);
        v.setColor(NodeColor.FIND);
        addToScene(v);
        if (T.getRoot() == null) {
            v.goToRoot();
            addStep(T.getBoundingBoxDef(), 200, REL.TOP, "empty");
            pause();
            v.goDown();
            v.setColor(NodeColor.NOTFOUND);
            addStep(T.getBoundingBoxDef(), 200, REL.TOP, "notfound");
            removeFromScene(v);
        } else {
            BSTNode w = T.getRoot();
            v.goTo(w);
            addStep(v, REL.TOP, "bstfindstart");
            pause();
            while (true) {
                if (w.getKey() == K) {
                    v.goTo(w);
                    if (((GBNode) w).isDeleted()) {
                        addStep(w, REL.BOTTOM, "gbfinddeleted");
                        v.setColor(NodeColor.NOTFOUND);
                        v.goDown();
                    } else {
                        addStep(w, REL.BOTTOM, "found");
                        v.setColor(NodeColor.FOUND);
                        pause();
                        addStep(w, REL.BOTTOM, "done");
                    }
                    break;
                } else if (w.getKey() < K) {
                    if (w.getRight() == null) {
                        v.pointInDir(45);
                    } else {
                        v.pointAbove(w.getRight());
                    }
                    addStep(v, REL.LEFT, "bstfindright", "" + K, w.getKeyS());
                    pause();
                    v.noArrow();
                    w.setColor(NodeColor.DARKER);
                    if (w.getLeft() != null) {
                        w.getLeft().subtreeColor(NodeColor.DARKER);
                    }
                    if (w.getRight() != null) {
                        w = w.getRight();
                        v.goAbove(w);
                    } else { // not found
                        addStep(w, REL.BOTTOMLEFT, "notfound");
                        v.setColor(NodeColor.NOTFOUND);
                        v.goRight();
                        break;
                    }
                } else {
                    if (w.getLeft() == null) {
                        v.pointInDir(135);
                    } else {
                        v.pointAbove(w.getLeft());
                    }
                    addStep(v, REL.RIGHT, "bstfindleft", "" + K, w.getKeyS());
                    pause();
                    v.noArrow();
                    w.setColor(NodeColor.DARKER);
                    if (w.getRight() != null) {
                        w.getRight().subtreeColor(NodeColor.DARKER);
                    }
                    if (w.getLeft() != null) {
                        w = w.getLeft();
                        v.goAbove(w);
                    } else { // notfound
                        addStep(w, REL.BOTTOMLEFT, "notfound");
                        v.setColor(NodeColor.NOTFOUND);
                        v.goLeft();
                        break;
                    }
                }
                pause();
            }
            pause();
            if (T.getRoot() != null) {
                T.getRoot().subtreeColor(NodeColor.NORMAL);
            }
            removeFromScene(v);
        }
    }
}
