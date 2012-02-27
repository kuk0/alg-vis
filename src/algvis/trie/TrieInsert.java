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
		setHeader("insert", s.substring(0, s.length() - 1));
	}

	@Override
	public void run() {
		if (s.compareTo("$") == 0) {
			addNote("badword");
			return;
		}

		TrieNode w = T.getRoot();
		addNote("trieinsertnote");
		addStep("trierootstart");
		w.mark();
		mysuspend();
		w.unmark();

		while (s.compareTo("$") != 0) {
			String ch = s.substring(0, 1);
			TrieNode ww = w.getChildWithCH(ch);
			if (ww != null) {
				addStep("trieinsertwch", ch);
			} else {
				addStep("trieinsertwoch", ch);
				ww = w.addChild(ch);
			}
			ww.setColor(NodeColor.CACHED);
			T.reposition();
			mysuspend();
			w.setColor(NodeColor.NORMAL);
			w = ww;
			ww.setColor(NodeColor.INSERT);
			T.reposition();
			s = s.substring(1);
		}
		TrieNode ww = w.getChildWithCH("$");
		if (ww == null) {
		addStep("trieinserteow");
		} else {
			addStep("trieinsertneow");
		}
		mysuspend();
		w.setColor(NodeColor.NORMAL);
		w = w.addChild(s);
		w.setColor(NodeColor.INSERT);
		T.reposition();
		addStep("done");
		w.setColor(NodeColor.NORMAL);
	}

}
