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
package algvis.ds.suffixtree;

import java.util.Vector;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.core.TreeNode;
import algvis.ds.trie.TrieWordNode;
import algvis.ui.view.REL;

public class SuffixTreeFind extends Algorithm {
    private final SuffixTree T;
    private final String s;
    private TrieWordNode hw;

    public SuffixTreeFind(SuffixTree T, String s) {
        super(T.panel);
        this.T = T;
        this.s = s.substring(0, s.length() - 1);
    }

    void beforeReturn() {
        removeFromScene(hw);
        T.clearExtraColor();
        addNote("done");
    }

    @Override
    public void runAlgorithm() {
        setHeader("triefind", s);
        if (s.length() == 0) {
            // addNote("badword");
        }

        SuffixTreeNode v = T.getRoot();
        // addNote("triefindnote");
        addStep(v, REL.TOP, "trierootstart");
        v.mark();
        pause();
        v.unmark();
        hw = new TrieWordNode(T, s);
        addToScene(hw);
        hw.setColor(NodeColor.CACHED);
        hw.goNextTo(v);

        int i = 0;
        while (i < s.length()) {
            SuffixTreeNode wd = v.getChild();
            while (wd != null) {
                wd.setColor(NodeColor.FIND);
                wd = wd.getRight();
            }
            wd = v.getChild();

            final char ch = s.charAt(i);
            hw.setAndGoNextTo(s.substring(i), v);
            final SuffixTreeNode w = v.getChildWithCH(ch);
            if (w == null) {
                while (wd != null) {
                    wd.setColor(NodeColor.NORMAL);
                    wd = wd.getRight();
                }
                addStep(v, REL.TOP, "triefindending1", "" + ch);
                pause();
                beforeReturn();
                return;
            }
            addStep(v, REL.TOP, "triefindmovedown", "" + ch);
            pause();
            v = w;
            while (wd != null) {
                wd.setColor(NodeColor.NORMAL);
                wd = wd.getRight();
            }
            v.setColor(NodeColor.CACHED);
            ++i;
        }
        hw.setAndGoNextTo("", v);
        final Vector<TreeNode> leaves = v.getLeaves();
        final Vector<Integer> pos = new Vector<>();
        for (final TreeNode w : leaves) {
            final int p = w.getKey();
            w.mark();
            T.str.mark(p);
            T.str.setColor(NodeColor.FOUND.bgColor, p - 1, p - 1 + s.length());
            pos.add(p);
        }
        addStep(v, REL.TOP, "suffixtree-found", s, "" + leaves.size());

        pause();
        for (final TreeNode w : leaves) {
            w.unmark();
            T.str.unmark(w.getKey());
        }
        T.str.setColor(NodeColor.NORMAL.bgColor, 0, 99999);
        beforeReturn();
    }
}