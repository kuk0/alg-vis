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

public class TrieDelete extends Algorithm {
	private Trie T;
	private String s;

	public TrieDelete(Trie T, String s) {
		super(T);
		this.T = T;
		this.s = s;
		setHeader("triedelete", s.substring(0, s.length() - 1));
	}

	void beforeReturn() {
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
		addNote("triedeletenote1");
		addStep("trierootstart");
		v.mark();
		mysuspend();
		v.unmark();
		T.hw = new TrieWordNode(T, s);
		T.hw.setColor(NodeColor.CACHED);
		T.hw.goNextTo(v);

		while (s.compareTo("$") != 0) {
			TrieNode vd = (TrieNode) v.getChild();
			while (vd != null) {
				vd.setColor(NodeColor.FIND);
				vd = (TrieNode) vd.getRight();
			}
			vd = (TrieNode) v.getChild();

			char ch = s.charAt(0);
			T.hw.setAndGoNextTo(s, v);
			TrieNode ww = v.getChildWithCH(ch);
			if (ww == null) {
				while (vd != null) {
					vd.setColor(NodeColor.NORMAL);
					vd = (TrieNode) vd.getRight();
				}
				addStep("triefindending1", ""+ch);
				mysuspend();
				addStep("triedeletefindunsu");
				mysuspend();
				beforeReturn();
				return;
			}
			addStep("triefindmovedown", ""+ch);
			mysuspend();
			while (vd != null) {
				vd.setColor(NodeColor.NORMAL);
				vd = (TrieNode) vd.getRight();
			}
			v.setColor(NodeColor.NORMAL);
			v = ww;
			v.setColor(NodeColor.CACHED);
			s = s.substring(1);
		}
		T.hw.setAndGoNextTo(s, v);
		TrieNode w = v.getChildWithCH('$');
		if (w == null) {
			addStep("triefindending2");
			mysuspend();
			v.setColor(NodeColor.NORMAL);
			addStep("triedeletefindunsu");
			beforeReturn();
			return;
		} else {
			addStep("triefindsucc");
		}
		mysuspend();
		T.hw = null;
		T.clearExtraColor();
		addNote("triedeletenote2");
		v.deleteChild(w);
		T.reposition();
		if (v.getChild() != null) {
			addStep("triedeletewodb");
			mysuspend();
			beforeReturn();
			return;
		}

		int countOfSons;
		w = v;
		do {
			w.setColor(NodeColor.DELETE);
			w = w.getParent();
			countOfSons = 0;
			TrieNode ww = (TrieNode) w.getChild();
			while (ww != null) {
				countOfSons++;
				ww = (TrieNode) ww.getRight();
			}
		} while ((w.getParent() != null) && (countOfSons == 1));
		mysuspend();
		w = v;
		do {
			addStep("triedeletedbdb");
			mysuspend();
			w = v.getParent();
			w.deleteChild(v);
			T.reposition();
			v = w;
		} while ((v.getParent() != null) && (v.getChild() == null));
		addStep("triedeletedbend");
		addStep("done");
	}

}
