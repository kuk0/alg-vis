package algvis.trie;

import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class TrieDelete extends Algorithm {
	Trie T;
	String s;
	
	public TrieDelete(Trie T, String s) {
		super(T);
		this.T = T;
		this.s = s;
		setHeader("delete", s.substring(0, s.length() - 1));
	}
	
	@Override
	public void run() {
		if (s.compareTo("$") == 0) {
			addNote("badword");
			return;
		}

		TrieNode v = T.getRoot();
		addNote("triedeletenote1");
		addStep("trierootstart");
		v.mark();
		mysuspend();
		v.unmark();

		while (s.compareTo("$") != 0) {
			TrieNode vd = (TrieNode) v.getChild();
			while (vd != null) {
				vd.setColor(NodeColor.FIND);
				vd = (TrieNode) vd.getRight();
			}
			vd = (TrieNode) v.getChild();

			String ch = s.substring(0, 1);
			TrieNode ww = v.getChildWithCH(ch);
			if (ww == null) {
				while (vd != null) {
					vd.setColor(NodeColor.NORMAL);
					vd = (TrieNode) vd.getRight();
				}
				addStep("triefindending1", ch);
				mysuspend();
				addStep("triedeletefindunsu");
				mysuspend();
				addStep("done");
				return;
			}
			addStep("triefindmovedown", ch);
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
		TrieNode w = (TrieNode) v.getChildWithCH("$");
		if (w == null) {
			addStep("triefindending2");
			mysuspend();
			v.setColor(NodeColor.NORMAL);
			addStep("triedeletefindunsu");
			mysuspend();
			addStep("done");
			return;
		} else {
			addStep("triefindsucc");
		}
		mysuspend();
		v.setColor(NodeColor.NORMAL);
		/*
		TrieNode v = (TrieNode) getParent();
		if ((key == ordinaryNode) && (getChild() == null) && (v != null)) {
			v.deleteChild(this);
			v.deleteDeadBranch();
		}
		*/
		addNote("triedeletenote2");
		v.deleteChild(w);
		T.reposition();
		if (v.getChild() != null) {
			addStep("triedeletewodb");
			mysuspend();
			addStep("done");
			return;
		}
		
		int countOfSons;
		w = v;
		do {
			w.greyPair = true;
			w = (TrieNode) w.getParent();
			countOfSons = 0;
			TrieNode ww = (TrieNode) w.getChild();
			while (ww != null) {
				countOfSons++;
				ww = (TrieNode) ww.getRight();
			}
		} while ((w != null) && (countOfSons == 1));
		mysuspend();
		w = v;
		do {
			w = (TrieNode) v.getParent();
			w.deleteChild(v);
			v = w;
		} while ((v != null) && (v.getChild() == null));
		T.reposition();
		addStep("done");
	}

}
