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

public class TrieInsert extends Algorithm {
	Trie T;
	String s;

	public TrieInsert(Trie T, String s) {
		super(T);
		this.T = T;
		this.s = s;
		if (s.compareTo(Trie.EPSILON) == 0) {
			setHeader("trieinsert", Trie.EPSILON);
		} else {
			setHeader("trieinsert", s.substring(0, s.length() - 1));
		}
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
			return;
		}

		TrieNode v = T.getRoot();
		v.mark();
		addNote("trieinsertnote");
		addStep("trierootstart");
		mysuspend();
		v.unmark();
		T.hw = new TrieHelpWord(T, s);
		T.hw.setC(NodeColor.INSERT);
		T.hw.goNextTo(v);
		
		if (s.compareTo(Trie.EPSILON) == 0) {
			TrieNode w = v.getChildWithCH(Trie.EPSILON);
			if (w != null) {
				addStep("triewe");
			} else {
				addStep("triewoe");
				addStep("trieappende");
				w = v.addChild(Trie.EPSILON);
			}
			w.setColor(NodeColor.CACHED);
			T.reposition();
			mysuspend();
			v = w;
			v.setColor(NodeColor.INSERT);
			T.reposition();
			
			beforeReturn();
			return;
		}

		while (s.compareTo("$") != 0) {
			String ch = s.substring(0, 1);
			T.hw.setAndGoNextTo(s, v);
			TrieNode w = v.getChildWithCH(ch);
			if (w != null) {
				addStep("trieinsertwch", ch);
			} else {
				addStep("trieinsertwoch", ch);
				w = v.addChild(ch);
			}
			w.setColor(NodeColor.CACHED);
			T.reposition();
			mysuspend();
			v = w;
			v.setColor(NodeColor.INSERT);
			T.reposition();
			s = s.substring(1);
		}
		T.hw.setAndGoNextTo(s, v);
		TrieNode w = v.getChildWithCH("$");
		if (w == null) {
			addStep("trieinserteow");
		} else {
			addStep("trieinsertneow");
		}
		mysuspend();
		v.setColor(NodeColor.NORMAL);
		v = v.addChild(s);
		T.reposition();
		T.hw.setAndGoNextTo(s, v);
		beforeReturn();
	}
}
