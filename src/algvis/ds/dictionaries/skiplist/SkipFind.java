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

import algvis.core.NodeColor;
import algvis.core.visual.ZDepth;
import algvis.ui.view.REL;

public class SkipFind extends SkipAlg {
    public SkipFind(SkipList L, int x) {
        super(L, x);
    }

    @Override
    public void runAlgorithm() {
        setHeader("find", K);
        p = new SkipNode[L.height];
        v = new SkipNode(L, K, ZDepth.ACTIONNODE);
        v.setColor(NodeColor.FIND);
        addToScene(v);
        
        addStep("skipfindstart");
        addStep(L.getRoot(),REL.TOP, "skipfindstart");
       
        final SkipNode w = find();
        if (w.getKey() == v.getKey()) {
            addNote("found");
            addStep(w, REL.BOTTOM, "found", "" + K);
            v.setColor(NodeColor.FOUND);
            pause();
            addNote("done");
        } else {
            addNote("notfound");
            addStep(w, REL.BOTTOM, "notfound", "" + K);
            pause();
            v.setColor(NodeColor.NOTFOUND);
            v.goDown();
        }
        removeFromScene(v);
    }
}
