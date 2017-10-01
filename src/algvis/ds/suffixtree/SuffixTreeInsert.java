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
import algvis.core.StringElem;
import algvis.ds.trie.TrieWordNode;
import algvis.ui.view.REL;

public class SuffixTreeInsert extends Algorithm {
    private final SuffixTree T;
    private final String s;
    private TrieWordNode hw;

    public SuffixTreeInsert(SuffixTree T, String s) {
        super(T.panel);
        this.T = T;
        this.s = s;
        T.clear();
    }

    void beforeReturn() {
        removeFromScene(hw);
        // T.clearExtraColor();
        addNote("done");
    }

    @Override
    public void runAlgorithm() {
        T.text = s;
        T.str = new StringElem(T.text, 0, SuffixTree.textpos);

        setHeader("trieinsert", s.substring(0, s.length() - 1));
        if (s.compareTo("$") == 0) {
            addNote("badword");
        }

        Vector<SuffixTreeNode> ruleOneBuffer = new Vector<SuffixTreeNode>();

        T.getRoot().ch = ':';
        T.text = s;
        SuffixTreeNode starting = T.getRoot();
        int startingJ = 0;

        SuffixTreeNode setUpSuffixLinkOnThis = T.getRoot();
        final int length = T.text.length();
        for (int i = 0; i < length; i++) {
            addStep(T, 200, REL.TOP, "sxbphase", "" + (i + 1));
            T.str.setColor(NodeColor.NORMAL.bgColor, i, i + 1);
            T.reposition();
            pause();
            final char ch = T.text.charAt(i);
            if (ch != '$') {
                addStep(T, 200, REL.TOP, "sxbfirstrule", "" + ch);
            } else {
                addStep(T, 200, REL.TOP, "sxbfirstrule", "\\$");
            }
            T.reposition();
            pause();
            // in real implementation this is done in O(1) both time & space
            final Vector<SuffixTreeNode> newRuleOneBuffer = new Vector<SuffixTreeNode>();
            for (final SuffixTreeNode u : ruleOneBuffer) {
                u.setColor(NodeColor.NORMAL);
                final SuffixTreeNode w = new SuffixTreeNode(T, ch, u.x, u.y,
                    true);
                u.addChild(w);
                w.setColor(NodeColor.CACHED);
                newRuleOneBuffer.add(w);
                starting = w;
                w.setKey(newRuleOneBuffer.size());
            }
            ruleOneBuffer = newRuleOneBuffer;
            starting.setColor(NodeColor.FOUND);
            if (ch != '$') {
                addStep(T, 200, REL.TOP, "sxbcontinue", "" + ch);
            } else {
                addStep(T, 200, REL.TOP, "sxbcontinue", "\\$");
            }
            T.reposition();
            pause();
            final Vector<SuffixTreeNode> upWalk = new Vector<SuffixTreeNode>();
            String cachedUpWalk = "";
            setUpSuffixLinkOnThis = T.getRoot();
            SuffixTreeNode current = starting;
            boolean pathEnded = false;
            while (!pathEnded) {
                addStep(current, REL.BOTTOM, "sxbupwalk");
                T.reposition();
                pause();
                SuffixTreeNode caching = current;
                if ((caching == starting) && (current != T.getRoot())) {
                    caching = caching.getParent();
                }
                while (caching.isPacked()) {
                    upWalk.add(caching);
                    cachedUpWalk = caching.ch + cachedUpWalk;
                    caching = caching.getParent();
                }
                current.unmark();
                starting.setColor(NodeColor.FOUND);
                current = caching;
                current.mark();
                // if upWalk != null
                for (final SuffixTreeNode u : upWalk) {
                    u.setColor(NodeColor.INSERT);
                }
                current.unmark();
                if (!current.isRoot()) {
                    addStep(current, REL.BOTTOM, "sxbslink");
                    T.reposition();
                    pause();
                    current = current.getSuffixLink();
                }
                if (current.isRoot()) {
                    cachedUpWalk = T.text.substring(startingJ, i);
                    addStep(current, REL.BOTTOM, "sxbfind", cachedUpWalk);
                    T.reposition();
                    pause();
                }
                current.mark();
                for (final SuffixTreeNode u : upWalk) {
                    u.setColor(NodeColor.NORMAL);
                }
                starting.setColor(NodeColor.FOUND);
                if (ch != '$') {
                    addStep(current, REL.BOTTOM, "sxbdownwalk", "" + ch);
                } else {
                    addStep(current, REL.BOTTOM, "sxbdownwalk", "\\$");
                }
                hw = new TrieWordNode(T, cachedUpWalk, current.x, current.y,
                    NodeColor.INSERT);
                addToScene(hw);
                hw.goNextTo(current);
                T.reposition();
                pause();
                final Vector<SuffixTreeNode> downWalk = new Vector<SuffixTreeNode>();
                caching = current;
                while (!cachedUpWalk.equals("")) {
                    if (!caching.isPacked()) {
                        current.unmark();
                        current = caching;
                        current.mark();
                    }
                    for (final SuffixTreeNode u : downWalk) {
                        u.setColor(NodeColor.INSERT);
                    }
                    hw.setAndGoNextTo(cachedUpWalk, current);
                    // T.reposition();
                    // pause();
                    // in real implementation this is O(1) both time and space
                    do {
                        caching = caching
                            .getChildWithCH(cachedUpWalk.charAt(0));
                        cachedUpWalk = cachedUpWalk.substring(1);
                        downWalk.add(caching);
                    } while (!cachedUpWalk.equals("") && caching.isPacked());
                }
                for (final SuffixTreeNode u : downWalk) {
                    u.setColor(NodeColor.NORMAL);
                }
                upWalk.clear();
                hw.setAndGoNextTo(cachedUpWalk, current);
                T.reposition();
                current = caching;
                current.mark();
                if (current.getChildWithCH(ch) != null) {
                    // rule 3!
                    pathEnded = true;
                    if (setUpSuffixLinkOnThis != T.getRoot()) {
                        setUpSuffixLinkOnThis.setSuffixLink(current);
                    }
                    addStep(current, REL.BOTTOM, "sxbthirdrule");
                    T.reposition();
                    pause();
                    current.unmark();
                } else {
                    // rule 2
                    if (setUpSuffixLinkOnThis != T.getRoot()) {
                        setUpSuffixLinkOnThis.setSuffixLink(current);
                    }
                    if (ch != '$') {
                        addStep(current, REL.BOTTOM, "sxbsecondrule", "" + ch);
                    } else {
                        addStep(current, REL.BOTTOM, "sxbsecondrule", "\\$");
                    }
                    T.reposition();
                    pause();
                    if (current != T.getRoot()) {
                        setUpSuffixLinkOnThis = current;
                    } else {
                        pathEnded = true;
                    }
                    current.setPacked(false);
                    final SuffixTreeNode u = new SuffixTreeNode(T, ch,
                        current.x, current.y, true);
                    current.addChild(u);
                    u.setParent(current);
                    ruleOneBuffer.add(u);
                    u.setKey(ruleOneBuffer.size());
                    starting.setColor(NodeColor.CACHED);
                    starting = u;
                    startingJ++;
                    starting.setColor(NodeColor.FOUND);
                    addStep(current, REL.BOTTOM, "sxbaftersecondrule", "" + ch);
                    T.reposition();
                    pause();
                    current.unmark();
                    if ((current.getSuffixLink() == null)
                        && (!current.isRoot())) {
                        upWalk.add(current);
                        cachedUpWalk = current.ch + cachedUpWalk;
                        current = current.getParent();
                    }
                }
                removeFromScene(hw);
            }

        }

        /*
         * After Ukkonen's algorithm we need to mark leaves.
         */

        addStep(T, 200, REL.TOP, "sxbexplicit");
        T.reposition();
        pause();
        for (final SuffixTreeNode u : ruleOneBuffer) {
            u.setPacked(false);
            u.setColor(NodeColor.NORMAL);
        }

        T.reposition();
        beforeReturn();
    }
}
