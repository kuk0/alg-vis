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
		setHeader("find", s.substring(0, s.length() - 1));
	}

	@Override
	public void run() {
		if (s.compareTo("$") == 0) {
			addNote("badword");
			return;
		}

		TrieNode w = T.getRoot();
		addNote("triefindnote");
		addStep("trierootstart");
		w.mark();
		mysuspend();
		w.unmark();

		while (s.compareTo("$") != 0) {
			TrieNode wwd = (TrieNode) w.getChild();
			while (wwd != null) {
				wwd.setColor(NodeColor.FIND);
				wwd = (TrieNode) wwd.getRight();
			}
			wwd = (TrieNode) w.getChild();

			String ch = s.substring(0, 1);
			TrieNode ww = w.getChildWithCH(ch);
			if (ww == null) {
				while (wwd != null) {
					wwd.setColor(NodeColor.NORMAL);
					wwd = (TrieNode) wwd.getRight();
				}
				addStep("triefindending1", ch);
				mysuspend();
				return;
			}
			addStep("triefindmovedown", ch);
			mysuspend();
			while (wwd != null) {
				wwd.setColor(NodeColor.NORMAL);
				wwd = (TrieNode) wwd.getRight();
			}
			w.setColor(NodeColor.NORMAL);
			w = ww;
			w.setColor(NodeColor.CACHED);
			s = s.substring(1);
		}
		TrieNode ww = (TrieNode) w.getChildWithCH("$");
		if (ww == null) {
			addStep("triefindending2");
		} else {
			addStep("triefindsucc");
		}
		mysuspend();
		w.setColor(NodeColor.NORMAL);
		
		addStep("triedeletenote2");
		mysuspend();
		
		
		addStep("done");
	}
}
