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
package algvis.suffixtree;

import java.util.Vector;

import algvis.core.Algorithm;
import algvis.core.NodeColor;
import algvis.trie.TrieWordNode;

public class SuffixTreeInsert extends Algorithm {
	SuffixTree T;
	String s;

	public SuffixTreeInsert(SuffixTree T, String s) {
		super(T);
		this.T = T;
		this.s = s;
		setHeader("trieinsert", s.substring(0, s.length() - 1));
	}

	public void beforeReturn() {
		T.hw = null;
		// T.clearExtraColor();
		addStep("done");
	}

	@Override
	public void run() {
		if (s.compareTo("$") == 0) {
			addNote("badword");
		}

		Vector<SuffixTreeNode> ruleOneBuffer = new Vector<SuffixTreeNode>();

		T.setRoot(new SuffixTreeNode(T, false));
		T.text = s;
		SuffixTreeNode v = T.getRoot();
		v.mark();
		// addNote("sxinsertnote");
		addStep("sxbstart");
		mysuspend();
		v.unmark();
		SuffixTreeNode starting = v;
		int startingJ = 0;

		SuffixTreeNode setUpSuffixLinkOnThis = T.getRoot();
		int length = T.text.length();
		for (int i = 1; i <= length; i++) {
			addStep("sxbphase", i);
			T.reposition();
			mysuspend();
			String ch = T.text.substring(i - 1, i);
			if (!ch.equals("$"))
				addStep("sxbfirstrule", ch);
			else
				addStep("sxbfirstrule", "\\$");
			T.reposition();
			mysuspend();
			// in real implementation this is done in O(1) both time & space
			Vector<SuffixTreeNode> newRuleOneBuffer = new Vector<SuffixTreeNode>();
			for (SuffixTreeNode u : ruleOneBuffer) {
				u.setColor(NodeColor.NORMAL);
				SuffixTreeNode w = new SuffixTreeNode(T, ch, u.x, u.y, true);
				u.addChild(w);
				w.setColor(NodeColor.CACHED);
				newRuleOneBuffer.add(w);
				starting = w;
			}
			ruleOneBuffer = newRuleOneBuffer;
			starting.setColor(NodeColor.FOUND);
			if (!ch.equals("$"))
				addStep("sxbcontinue", ch);
			else
				addStep("sxbcontinue", "\\$");
			T.reposition();
			mysuspend();
			Vector<SuffixTreeNode> upWalk = new Vector<SuffixTreeNode>();
			String cachedUpWalk = new String();
			setUpSuffixLinkOnThis = T.getRoot();
			SuffixTreeNode current = starting;
			boolean pathEnded = false;
			while (!pathEnded) {
				addStep("sxbupwalk");
				T.reposition();
				mysuspend();
				SuffixTreeNode caching = current;
				if ((caching == starting) && (current != T.getRoot()))
					caching = (SuffixTreeNode) caching.getParent();
				while (caching.isPacked()) {
					upWalk.add(caching);
					cachedUpWalk = caching.ch + cachedUpWalk;
					caching = (SuffixTreeNode) caching.getParent();
				}
				current.unmark();
				starting.setColor(NodeColor.FOUND);
				current = caching;
				current.mark();
				// if upWalk != null
				for (SuffixTreeNode u : upWalk) {
					u.setColor(NodeColor.INSERT);
				}
				current.unmark();
				if (!current.isRoot()) {
					addStep("sxbslink");
					T.reposition();
					mysuspend();
					current = current.getSuffixLink();
				}
				if (current.isRoot()) {
					cachedUpWalk = T.text.substring(startingJ, i - 1);
					addStep("sxbfind", cachedUpWalk);
					T.reposition();
					mysuspend();
				}
				current.mark();
				for (SuffixTreeNode u : upWalk) {
					u.setColor(NodeColor.NORMAL);
				}
				starting.setColor(NodeColor.FOUND);
				if (!ch.equals("$"))
					addStep("sxbdownwalk", ch);
				else
					addStep("sxbdownwalk", "\\$");
				T.hw = new TrieWordNode(T, cachedUpWalk, current.x, current.y,
						NodeColor.INSERT);
				T.hw.goNextTo(current);
				T.reposition();
				mysuspend();
				Vector<SuffixTreeNode> downWalk = new Vector<SuffixTreeNode>();
				caching = current;
				while (!cachedUpWalk.equals("")) {
					if (!caching.isPacked()) {
						current.unmark();
						current = caching;
						current.mark();
					}
					for (SuffixTreeNode u : downWalk) {
						u.setColor(NodeColor.INSERT);
					}
					T.hw.setAndGoNextTo(cachedUpWalk, current);
					addStep("sxbdownwalkedge");
					T.reposition();
					mysuspend();
					// in real implementation this is O(1) both time and space
					SuffixTreeNode u = null;
					do {
						caching = (SuffixTreeNode) caching
								.getChildWithCH(cachedUpWalk.substring(0, 1));
						cachedUpWalk = cachedUpWalk.substring(1);
						downWalk.add(caching);
					} while ((!cachedUpWalk.equals("")) && (u != null)
							&& (caching.isPacked()));
				}
				for (SuffixTreeNode u : downWalk) {
					u.setColor(NodeColor.NORMAL);
				}
				upWalk.clear();
				T.hw.setAndGoNextTo(cachedUpWalk, current);
				T.reposition();
				current = caching;
				current.mark();
				if (current.getChildWithCH(ch) != null) {
					// rule 3!
					pathEnded = true;
					if (setUpSuffixLinkOnThis != T.getRoot()) {
						setUpSuffixLinkOnThis.setSuffixLink(current);
					}
					addStep("sxbthirdrule");
					T.reposition();
					mysuspend();
					current.unmark();
				} else {
					// rule 2
					if (setUpSuffixLinkOnThis != T.getRoot()) {
						setUpSuffixLinkOnThis.setSuffixLink(current);
					}
					if (!ch.equals("$"))
						addStep("sxbsecondrule", ch);
					else
						addStep("sxbsecondrule", "\\$");
					T.reposition();
					mysuspend();
					if (current != T.getRoot()) {
						setUpSuffixLinkOnThis = current;
					} else {
						pathEnded = true;
					}
					current.setPacked(false);
					SuffixTreeNode u = new SuffixTreeNode(T, ch, current.x,
							current.y, true);
					current.addChild(u);
					ruleOneBuffer.add(u);
					starting.setColor(NodeColor.CACHED);
					starting = u;
					startingJ++;
					starting.setColor(NodeColor.FOUND);
					addStep("sxbaftersecondrule", ch);
					T.reposition();
					mysuspend();
					current.unmark();
					if ((current.getSuffixLink() == null)
							&& (!current.isRoot())) {
						upWalk.add(current);
						cachedUpWalk = current.ch + cachedUpWalk;
						current = (SuffixTreeNode) current.getParent();
					}
				}
				T.hw = null;
			}

		}

		/*
		 * After Ukkonen's algorithm we need to mark leaves.
		 */

		addStep("sxbexplicit");
		T.reposition();
		mysuspend();
		for (SuffixTreeNode u : ruleOneBuffer) {
			u.setPacked(false);
		}

		T.reposition();
		beforeReturn();

	}
}
