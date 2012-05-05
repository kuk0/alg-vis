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
		mysuspend();
		v.unmark();
		SuffixTreeNode starting = v;
		starting.setColor(NodeColor.FOUND);
		int startingJ = 0;
		// T.hw = new TrieWordNode(T, s);
		// T.hw.setColor(NodeColor.INSERT);
		// T.hw.goNextTo(v);

		SuffixTreeNode setUpSuffixLinkOnThis = null;
		int length = T.text.length();
		for (int i = 1; i <= length; i++) {
			if (setUpSuffixLinkOnThis != null) {
				setUpSuffixLinkOnThis.setSuffixLink(T.getRoot());
				setUpSuffixLinkOnThis = null;
			}
			
			String ch = T.text.substring(i - 1, i);
			// TODO addstep: extend first-cases
			mysuspend();
			// in real implementation this is done in O(1) both time & space
			Vector<SuffixTreeNode> newRuleOneBuffer = new Vector<SuffixTreeNode>();
			for (SuffixTreeNode u : ruleOneBuffer) {
				u.setColor(NodeColor.NORMAL);
				SuffixTreeNode w = new SuffixTreeNode(T, ch, u.x, u.y, true);
				u.addChild(w);
				w.setColor(NodeColor.CACHED);
				newRuleOneBuffer.add(w);
			}
			ruleOneBuffer = newRuleOneBuffer;
			// TODO addstep: walk the path
			T.reposition();
			mysuspend();

			SuffixTreeNode current = starting;
			boolean pathEnded = false;
			while (!pathEnded) {
				System.out.println("path not ended");
				String cachedUpWalk = new String();
				Vector<SuffixTreeNode> upWalk = new Vector<SuffixTreeNode>();
				SuffixTreeNode caching = current;
				while (caching.isPacked()) {
					System.out.println("going up");
					upWalk.add(caching);
					cachedUpWalk = caching.ch + cachedUpWalk;
					caching = (SuffixTreeNode) caching.getParent();
				}
				current.unmark();
				starting.setColor(NodeColor.FOUND);
				current = caching;
				current.mark();
				// TODO if upWalk != null
				// addstep: cached string + walk the suffix link
				for (SuffixTreeNode u : upWalk) {
					u.setColor(NodeColor.INSERT);
				}
				T.reposition();
				mysuspend();
				current.unmark();
				if (!current.isRoot()) {
					current = current.getSuffixLink();
				} else {
					cachedUpWalk = T.text.substring(startingJ, i - 1);
				}
				current.mark();
				for (SuffixTreeNode u : upWalk) {
					u.setColor(NodeColor.NORMAL);
				}
				// TODO addstep: find the suffix
				T.reposition();
				mysuspend();
				Vector<SuffixTreeNode> downWalk = new Vector<SuffixTreeNode>();
				caching = current;
				while (!cachedUpWalk.equals("")) {
					System.out.println("going down");
					// TODO addstep: down one edge
					for (SuffixTreeNode u : downWalk) {
						u.setColor(NodeColor.INSERT);
					}
					T.reposition();
					mysuspend();
					// in real implementation this is O(1) both time and space
					do {
						caching = (SuffixTreeNode) current
								.getChildWithCH(cachedUpWalk.substring(0, 1));
						cachedUpWalk = cachedUpWalk.substring(1);
						downWalk.add(caching);
					} while ((!cachedUpWalk.equals("")) && (caching.isPacked()));
				}
				for (SuffixTreeNode u : downWalk) {
					u.setColor(NodeColor.NORMAL);
				}
				current = caching;
				current.mark();
				if (current.getChildWithCH(ch) != null) {
					// rule 3!
					pathEnded = true;
					// TODO addstep: phase 3 is "show stopper"
					mysuspend();
					current.unmark();
				} else {
					// rule 2
					if (setUpSuffixLinkOnThis != null) {
						setUpSuffixLinkOnThis.setSuffixLink(current);
					}
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
				}
			}

		}

		/*
		 * TODO After ukkonen's algorithm we need to transform the tree from
		 * implicit to explicit form.
		 */

		// TODO addstep: change to explicit
		mysuspend();
		for (SuffixTreeNode u : ruleOneBuffer) {
			u.setPacked(true);
		}

		beforeReturn();

	}
}
