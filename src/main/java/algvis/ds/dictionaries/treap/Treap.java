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
package algvis.ds.dictionaries.treap;

import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTFind;
import algvis.ui.VisPanel;
import algvis.ui.view.Layout;

public class Treap extends BST {
    public static String dsName = "treap";

    // TreapNode root = null, v = null;

    @Override
    public String getName() {
        return "treap";
    }

    public Treap(VisPanel M) {
        super(M);
    }

    @Override
    public void insert(int x) {
        start(new TreapInsert(this, x));
    }

    @Override
    public void find(int x) {
        start(new BSTFind(this, x));
    }

    @Override
    public void delete(int x) {
        start(new TreapDelete(this, x));
    }

    @Override
    public Layout getLayout() {
        return Layout.COMPACT;
    }
}
