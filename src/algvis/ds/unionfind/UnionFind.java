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
package algvis.ds.unionfind;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Hashtable;

import algvis.core.DataStructure;
import algvis.core.MyRandom;
import algvis.core.history.HashtableStoreSupport;
import algvis.core.visual.ZDepth;
import algvis.ds.unionfind.UnionFindFind.FindHeuristic;
import algvis.ds.unionfind.UnionFindUnion.UnionHeuristic;
import algvis.ui.VisPanel;
import algvis.ui.view.Alignment;
import algvis.ui.view.ClickListener;
import algvis.ui.view.View;

public class UnionFind extends DataStructure implements ClickListener {
    public static String dsName = "ufi";

    public int count = 0;
    private ArrayList<UnionFindNode> sets = new ArrayList<>();
    private ArrayList<UnionFindNode> vertices = new ArrayList<>();

    public FindHeuristic pathCompression = FindHeuristic.NONE;
    public UnionHeuristic unionState = UnionHeuristic.NONE;

    public UnionFindNode firstSelected = null;
    public UnionFindNode secondSelected = null;

    @Override
    public String getName() {
        return "ufi";
    }

    public UnionFind(VisPanel M) {
        super(M);
        M.screen.V.align = Alignment.LEFT;
        M.screen.V.setDS(this);
        count = 0;
    }

    /** adds to sets and vertices */
    void add(UnionFindNode n) {
        count++;
        sets.add(n);
        vertices.add(n);
    }

    public void removeFromSets(UnionFindNode n) {
        sets.remove(n);
    }

    @Override
    public String stats() {
        return "";
    }

    @Override
    public void insert(int x) {
    }

    public void makeSet(int N) {
        for (int i = 0; i < N; i++) {
            add(new UnionFindNode(this, count, ZDepth.NODE));
        }
        reposition();
    }

    public void find(UnionFindNode u) {
        start(new UnionFindFind(this, u));
    }

    public void union(UnionFindNode u, UnionFindNode v) {
        start(new UnionFindUnion(this, u, v));
    }

    @Override
    public void random(int n) {
        for (int i = 0; i < n; ++i) {
            union(at(MyRandom.Int(count)), at(MyRandom.Int(count)));
        }
    }

    @Override
    public void clear() {
        count = 0;
        sets = new ArrayList<>();
        vertices = new ArrayList<>();
        makeSet(10);
        setStats();
    }

    @Override
    public void draw(View V) {
        if (sets != null) {
            for (final UnionFindNode set : sets) {
                set.drawTree(V);
            }
        }
    }

    @Override
    public void move() {
        if (sets != null) {
            for (final UnionFindNode set : sets) {
                set.moveTree();
            }
        }
    }

    @Override
    public Rectangle2D getBoundingBox() {
        Rectangle2D retVal = null;
        if (sets != null) {
            for (final UnionFindNode set : sets) {
                final Rectangle2D sBB = set.getBoundingBox();
                if (retVal == null) {
                    retVal = sBB;
                } else if (sBB != null) {
                    retVal = retVal.createUnion(sBB);
                }
            }
        }
        return retVal;
    }

    @Override
    protected void endAnimation() {
        if (sets != null) {
            for (final UnionFindNode set : sets) {
                set.endAnimation();
            }
        }
    }

    @Override
    protected boolean isAnimationDone() {
        if (sets != null) {
            for (final UnionFindNode set : sets) {
                if (!set.isAnimationDone()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void reposition() {
        if (sets != null) {
            int ey2 = -9999999;
            int ey1 = 9999999;
            for (final UnionFindNode set : sets) {
                y1 = y2 = 0;
                set.reposition();
                if (y1 < ey1) {
                    ey1 = y1;
                }
                if (y2 > ey2) {
                    ey2 = y2;
                }
            }
            y1 = ey1;
            y2 = ey2;

            x1 = x2 = 0;
            int shift = -sets.get(0).leftw;
            x1 = shift;
            for (final UnionFindNode set : sets) {
                shift += set.leftw;
                set.shift(shift, 0);
                shift += set.rightw;
            }
            x2 = shift;
            panel.screen.V.setBounds(x1, y1, x2, y2);
        }
    }

    public UnionFindNode at(int elementAt) {
        return vertices.get(elementAt);
    }

    boolean isSelected(UnionFindNode u) {
        return (u == firstSelected) || (u == secondSelected);
    }

    @Override
    public void mouseClicked(int x, int y) {
        UnionFindNode u = null;
        int i = 0;
        final int j = sets.size();
        do {
            u = (UnionFindNode) sets.get(i).find(x, y);
            i++;
        } while ((u == null) && (i < j));
        if (u != null) {
            if (isSelected(u)) {
                u.unmark();
                if (u == secondSelected) {
                    secondSelected = null;
                } else if (u == firstSelected) {
                    firstSelected = secondSelected;
                    secondSelected = null;
                }
            } else {
                u.mark();
                if (firstSelected == null) {
                    firstSelected = u;
                } else if (secondSelected == null) {
                    secondSelected = u;
                } else {
                    firstSelected.unmark();
                    firstSelected = secondSelected;
                    secondSelected = u;
                }
            }
        }
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "count", count);
        HashtableStoreSupport.store(state, hash + "sets", sets.clone());
        for (final UnionFindNode node : sets) {
            node.storeState(state);
        }
        HashtableStoreSupport.store(state, hash + "vertices", vertices.clone());
        for (final UnionFindNode node : vertices) {
            node.storeState(state);
        }
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object count = state.get(hash + "count");
        if (count != null) {
            this.count = (Integer) HashtableStoreSupport.restore(count);
        }

        final Object sets = state.get(hash + "sets");
        if (sets != null) {
            this.sets = (ArrayList<UnionFindNode>) HashtableStoreSupport
                .restore(sets);
        }
        for (final UnionFindNode node : this.sets) {
            node.restoreState(state);
        }

        final Object vertices = state.get(hash + "vertices");
        if (vertices != null) {
            this.vertices = (ArrayList<UnionFindNode>) HashtableStoreSupport
                .restore(vertices);
        }
        for (final UnionFindNode node : this.vertices) {
            node.restoreState(state);
        }
    }
}
