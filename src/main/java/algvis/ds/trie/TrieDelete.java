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
package algvis.ds.trie;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.ui.view.REL;

public class TrieDelete extends Algorithm {
    private final Trie T;
    private String s;
    private TrieWordNode hw;

    public TrieDelete(Trie T, String s) {
        super(T.panel);
        this.T = T;
        this.s = s;
    }

    void beforeReturn() {
        removeFromScene(hw);
        T.clearExtraColor();
        addNote("done");
    }

    @Override
    public void runAlgorithm() {
        setHeader("triedelete", s.substring(0, s.length() - 1));
        if (s.compareTo("$") == 0) {
            addNote("badword");
        }

        TrieNode v = T.getRoot();
        addNote("triedeletenote1");
        addStep(v, REL.TOP, "trierootstart");
        v.mark();
        pause();
        v.unmark();
        hw = new TrieWordNode(T, s);
        hw.setColor(NodeColor.CACHED);
        addToScene(hw);
        hw.goNextTo(v);

        while (s.compareTo("$") != 0) {
            TrieNode vd = v.getChild();
            while (vd != null) {
                vd.setColor(NodeColor.FIND);
                vd = vd.getRight();
            }
            vd = v.getChild();

            final char ch = s.charAt(0);
            hw.setAndGoNextTo(s, v);
            final TrieNode ww = v.getChildWithCH(ch);
            if (ww == null) {
                while (vd != null) {
                    vd.setColor(NodeColor.NORMAL);
                    vd = vd.getRight();
                }
                addStep(v, REL.TOP, "triefindending1", "" + ch);
                pause();
                addStep(v, REL.TOP, "triedeletefindunsu");
                pause();
                beforeReturn();
                return;
            }
            addStep(v, REL.TOP, "triefindmovedown", "" + ch);
            pause();
            while (vd != null) {
                vd.setColor(NodeColor.NORMAL);
                vd = vd.getRight();
            }
            v.setColor(NodeColor.NORMAL);
            v = ww;
            v.setColor(NodeColor.CACHED);
            s = s.substring(1);
        }
        hw.setAndGoNextTo(s, v);
        TrieNode w = v.getChildWithCH('$');
        if (w == null) {
            addStep(v, REL.TOP, "triefindending2");
            pause();
            v.setColor(NodeColor.NORMAL);
            addStep(v, REL.TOP, "triedeletefindunsu");
            beforeReturn();
            return;
        } else {
            addStep(v, REL.TOP, "triefindsucc");
        }
        pause();
        removeFromScene(hw);
        T.clearExtraColor();
        addNote("triedeletenote2");
        v.deleteChild(w);
        T.reposition();
        if (v.getChild() != null) {
            addStep(v, REL.TOP, "triedeletewodb");
            pause();
            beforeReturn();
            return;
        }

        int countOfSons;
        w = v;
        do {
            w.setColor(NodeColor.DELETE);
            w = w.getParent();
            countOfSons = 0;
            TrieNode ww = w.getChild();
            while (ww != null) {
                countOfSons++;
                ww = ww.getRight();
            }
        } while ((w.getParent() != null) && (countOfSons == 1));
        pause();
        w = v;
        do {
            addStep(v, REL.TOP, "triedeletedbdb");
            pause();
            w = v.getParent();
            w.deleteChild(v);
            T.reposition();
            v = w;
        } while ((v.getParent() != null) && (v.getChild() == null));
        addStep(v, REL.TOP, "triedeletedbend");
        addNote("done");
    }
}
