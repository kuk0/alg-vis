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

import java.util.Hashtable;

import algvis.core.Dictionary;
import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.internationalization.Languages;
import algvis.ui.VisPanel;
import algvis.ui.view.Alignment;
import algvis.ui.view.ClickListener;
import algvis.ui.view.View;

public class SkipList extends Dictionary implements ClickListener {
    public static String dsName = "skiplist";
    SkipNode sent;
    int height = 1, n = 0, e = 0;

    @Override
    public String getName() {
        return "skiplist";
    }

    public SkipList(VisPanel M) {
        super(M);
        M.screen.V.setDS(this);
        M.screen.V.align = Alignment.LEFT;
        root = new SkipNode(this, -Node.INF, zDepth);
        getRoot().linkright(sent = new SkipNode(this, Node.INF, zDepth));
        reposition();
    }

    @Override
    public void insert(int x) {
        start(new SkipInsert(this, x));
    }

    @Override
    public void find(int x) {
        start(new SkipFind(this, x));
    }

    @Override
    public void delete(int x) {
        start(new SkipDelete(this, x));
    }

    @Override
    public void clear() {
        if (n != 0) {
            height = 1;
            n = e = 0;
            root = new SkipNode(this, -Node.INF, zDepth);
            getRoot().linkright(sent = new SkipNode(this, Node.INF, zDepth));
            setStats();
            reposition();
            // panel.screen.V.resetView(); TODO toto bolo v BST.clear()
        }
    }

    @Override
    public String stats() {
        if (getRoot() == null) {
            return Languages.getString("size") + ": 0;   "
                + Languages.getString("height") + ": 0;   #"
                + Languages.getString("excess") + ": 0";
        } else {
            return Languages.getString("size") + ": " + n + ";   "
                + Languages.getString("height") + ": " + height + ";   #"
                + Languages.getString("excess") + ": " + e;
        }
    }

    @Override
    public void draw(View V) {
        if (getRoot() != null) {
            getRoot().drawSkipList(V);
        }
    }

    @Override
    protected void move() {
        if (getRoot() != null) {
            getRoot().moveSkipList();
        }
    }

    public void reposition() {
        x1 = 0;
        y1 = 0;
        getRoot()._reposition();
        panel.screen.V.setBounds(x1, y1, x2, y2);
    }

    @Override
    public void mouseClicked(int x, int y) {
        chosen = getRoot() != null ? getRoot().find(x, y) : null;
        if (chosen != null) {
            panel.buttons.I.setText("" + chosen.getKey());
        }
    }

    @Override
    public SkipNode getRoot() {
        return (SkipNode) super.getRoot();
    }

    public SkipNode setRoot(SkipNode root) {
        super.setRoot(root);
        return root;
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        super.storeState(state);
        HashtableStoreSupport.store(state, hash + "height", height);
        HashtableStoreSupport.store(state, hash + "n", n);
        HashtableStoreSupport.store(state, hash + "e", e);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        super.restoreState(state);
        final Object height = state.get(hash + "height");
        if (height != null) {
            this.height = (Integer) HashtableStoreSupport.restore(height);
        }
        final Object n = state.get(hash + "n");
        if (n != null) {
            this.n = (Integer) HashtableStoreSupport.restore(n);
        }
        final Object e = state.get(hash + "e");
        if (e != null) {
            this.e = (Integer) HashtableStoreSupport.restore(e);
        }
    }
}
