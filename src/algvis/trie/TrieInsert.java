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
		setHeader("trieinsert", s.substring(0, s.length() - 1));
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
		T.hw = new TrieHelpWord(s);
		T.hw.setC(NodeColor.INSERT);
		T.hw.goNextTo(v);

		while (s.compareTo("$") != 0) {
			String ch = s.substring(0, 1);
			T.hw.setAndGoNT(s, v);
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
		T.hw.setAndGoNT(s, v);
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
		T.hw.setAndGoNT(s, v);
		beforeReturn();
	}

}
