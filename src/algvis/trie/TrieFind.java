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
package algvis.trie;

import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class TrieFind extends Algorithm {
	Trie T;
	String s;

	public TrieFind(Trie T, String s) {
		super(T);
		this.T = T;
		this.s = s;
		setHeader("triefind", s.substring(0, s.length() - 1));
	}

	public void beforeReturn() {
		T.hw = null;
		T.clearExtraColor();
		addStep("done");
	}

	@Override
	public void run() {
		if (s.compareTo("$") == 0) {
			addNote("badword");
		}

		TrieNode v = T.getRoot();
		addNote("triefindnote");
		addStep("trierootstart");
		v.mark();
		mysuspend();
		v.unmark();
		T.hw = new TrieWordNode(T, s);
		T.hw.setColor(NodeColor.CACHED);
		T.hw.goNextTo(v);

		while (s.compareTo("$") != 0) {
			TrieNode wd = (TrieNode) v.getChild();
			while (wd != null) {
				wd.setColor(NodeColor.FIND);
				wd = (TrieNode) wd.getRight();
			}
			wd = (TrieNode) v.getChild();

			char ch = s.charAt(0);
			T.hw.setAndGoNextTo(s, v);
			TrieNode w = v.getChildWithCH(ch);
			if (w == null) {
				while (wd != null) {
					wd.setColor(NodeColor.NORMAL);
					wd = (TrieNode) wd.getRight();
				}
				addStep("triefindending1", ""+ch);
				mysuspend();
				beforeReturn();
				return;
			}
			addStep("triefindmovedown", ""+ch);
			mysuspend();
			while (wd != null) {
				wd.setColor(NodeColor.NORMAL);
				wd = (TrieNode) wd.getRight();
			}
			v = w;
			v.setColor(NodeColor.CACHED);
			s = s.substring(1);
		}
		T.hw.setAndGoNextTo(s, v);
		TrieNode w = v.getChildWithCH('$');
		if (w == null) {
			addStep("triefindending2");
		} else {
			addStep("triefindsucc");
		}
		mysuspend();

		beforeReturn();
	}
}
