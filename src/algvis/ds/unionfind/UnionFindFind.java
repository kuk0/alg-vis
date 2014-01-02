/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ds.unionfind;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.visual.Edge;
import algvis.ui.view.REL;

import java.util.Stack;

public class UnionFindFind extends Algorithm {
    public enum FindHeuristic {
        NONE, COMPRESSION, HALVING, SPLITTING
    }

    private UnionFindNode u = null;

    private final FindHeuristic findState;
    private final UnionFind UF;

    UnionFindFind(UnionFind UF) {
        super(UF.panel, null);
        this.UF = UF;
        this.findState = UF.pathCompression;
    }

    public UnionFindFind(UnionFind UF, UnionFindNode u) {
        this(UF);
        this.u = u;
    }

    @Override
    public void runAlgorithm() throws InterruptedException {
        setHeader("uffind");
        final UnionFindNode v = find(u);
        v.setColor(NodeColor.NORMAL);
        addNote("done");
    }

    UnionFindNode find(UnionFindNode u) throws InterruptedException {
        switch (findState) {
        case NONE:
            return findSimple(u);
        case COMPRESSION:
            return findWithCompression(u);
        case HALVING:
            return findHalvingOrSplitting(u, true);
        case SPLITTING:
            return findHalvingOrSplitting(u, false);
        default:
            return null;
        }
    }

    private Stack<UnionFindNode> findRoot(UnionFindNode u)
        throws InterruptedException {
        final Stack<UnionFindNode> S = new Stack<UnionFindNode>();
        UnionFindNode v = null;

        u.setColor(NodeColor.FIND);
        u.mark();
        addStep(u, REL.BOTTOM, "uf-find-start", u.getKey());
        pause();

        // looking for root
        v = u;
        while (!v.isRoot()) {
            S.add(v);
            v.setColor(NodeColor.FIND);
            v.setGrey(true);
            addStep(v, REL.RIGHT, "uf-go-up");
            pause();
            v = v.getParent();
        }

        // root found
        S.add(v);
        v.setColor(NodeColor.FOUND);
        addStep(v, REL.TOP, "uf-found-root", u.getKey());
        pause();
        return S;
    }

    UnionFindNode findSimple(UnionFindNode u) throws InterruptedException {
        Stack<UnionFindNode> S = findRoot(u);
        final UnionFindNode result = S.pop();
        // traveling back; set color back to normal
        while (!S.empty()) {
            S.pop().setColor(NodeColor.NORMAL);
        }
        u.unmark();
        result.setGrey(false);
        return result;
    }

    UnionFindNode findWithCompression(UnionFindNode u)
        throws InterruptedException {
        Stack<UnionFindNode> S = findRoot(u);
        final UnionFindNode result = S.pop();

        // the son of root doesn't move
        if (!S.empty()) {
            S.pop().setColor(NodeColor.NORMAL);
        }

        //TODO: on the way down, we link nodes directly to the root
        while (!S.empty()) {
            UnionFindNode v = S.pop();
            addStep(v, REL.BOTTOM, "uf-link", v.getKey(), result.getKey());
            // v.pointTo(result);
            pause();
            // v.noArrow();
            v.setColor(NodeColor.NORMAL);
            v.getParent().deleteChild(v);
            UF.reposition();
            // pause();
            result.addChild(v);
            UF.reposition();
            // pause();
        }

        pause();
        u.unmark();
        result.setGrey(false);
        return result;
    }

    private void greyPathToRoot(UnionFindNode u) throws InterruptedException {
        u.setColor(NodeColor.FIND);
        u.mark();
        addStep("uf-find-start", u.getKey());

        // grey path
        UnionFindNode t = u;
        while (t.getParent() != null) {
            t.setGrey(true);
            t = t.getParent();
        }
        pause();
    }

    UnionFindNode findHalvingOrSplitting(UnionFindNode u, boolean halving)
        throws InterruptedException {
        greyPathToRoot(u);
        UnionFindNode v = u, p, pp;

        v.mark();
        // looking for a root
        while (!v.isRoot() && !v.getParent().isRoot()) {
            p = v.getParent();
            pp = p.getParent();
            addToSceneUntilNext(new Edge(v, pp));
            addStep(v, REL.BOTTOM, "uf-link", v.getKey(), pp.getKey());
            pause();
            p.deleteChild(v);
            pp.addChild(v);
            UF.reposition();
            pause();
            if (halving) {
                addStep(v, REL.RIGHT, "uf-go-up");
            } else {
                addStep(v, REL.BOTTOM, "uf-go-old-parent");
            }
            pause();
            v.unmark();
            v = halving ? pp : p;
            v.mark();
        }
        if (!v.isRoot()) {
            addStep(v, REL.RIGHT, "uf-go-up");
            pause();
            v.unmark();
            v = v.getParent();
        }
        v.unmark();
        v.setColor(NodeColor.FOUND);
        addStep(v, REL.TOP, "uf-found-root", u.getKey());
        pause();
        addStep(v, REL.TOP, halving ? "uf-path-halved" : "uf-path-split",
            u.getKey());
        pause();

        u.unmark();
        v.setGrey(false);
        return v;
    }
}
