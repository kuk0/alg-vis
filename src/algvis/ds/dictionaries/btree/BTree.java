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
package algvis.ds.dictionaries.btree;

import java.util.Hashtable;

import algvis.core.Algorithm;
import algvis.core.Dictionary;
import algvis.core.history.HashtableStoreSupport;
import algvis.internationalization.Languages;
import algvis.ui.VisPanel;
import algvis.ui.view.View;

public class BTree extends Dictionary {
    public static String dsName = "btree";
    int order = 5;
    final int xspan = 5;
    final int yspan = 15;

    @Override
    public String getName() {
        return "btree";
    }

    public BTree(VisPanel M) {
        super(M);
    }

    @Override
    public void insert(int x) {
        start(new BInsert(this, x));
    }

    @Override
    public void find(int x) {
        start(new BFind(this, x));
    }

    @Override
    public void delete(int x) {
        start(new BDelete(this, x));
    }

    @Override
    public void clear() {
        root = null;
        setStats();
    }

    @Override
    public BNode getRoot() {
        return (BNode) root;
    }

    @Override
    public String stats() {
        if (root == null) {
            return "#" + Languages.getString("nodes") + ": 0;   #"
                + Languages.getString("keys") + ": 0 = 0% "
                + Languages.getString("full") + ";   "
                + Languages.getString("height") + ": 0";
        } else {
            getRoot().calcTree();
            return "#" + Languages.getString("nodes") + ": " + getRoot().nnodes
                + ";   " + "#" + Languages.getString("keys") + ": "
                + getRoot().nkeys + " = " + (100 * getRoot().nkeys)
                / (getRoot().nnodes * (order - 1)) + "% "
                + Languages.getString("full") + ";   "
                + Languages.getString("height") + ": " + getRoot().height;
        }
    }

    @Override
    public void draw(View V) {
        if (root != null) {
            getRoot().drawTree(V);
        }
    }

    @Override
    protected void move() {
        if (root != null) {
            getRoot().moveTree();
        }
    }

    public void reposition() {
        if (root != null) {
            getRoot()._reposition();
            panel.screen.V.setBounds(x1, y1, x2, y2);
        }
    }

    public void setOrder(final Integer order) {
        if (root != null || this.order != order) {
            start(new Algorithm(panel) {
                @Override
                public void runAlgorithm() {
                    BTree.this.order = order;
                    clear();
                }
            });
        }
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "order", order);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object order = state.get(hash + "order");
        if (order != null) {
            this.order = (Integer) HashtableStoreSupport.restore(order);
            final BTreeButtons buttons = (BTreeButtons) panel.buttons;
            buttons.OS.removeChangeListener(buttons);
            buttons.OS.setValue(this.order);
            buttons.OS.addChangeListener(buttons);
        }
    }
}
