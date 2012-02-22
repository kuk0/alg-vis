package algvis.trie;

import algvis.core.Algorithm;

public class TrieInsert extends Algorithm {
	Trie T;
	String S;
	
	public TrieInsert(Trie T, String S) {
		super(T);
		this.T = T;
		this.S = S;
		setHeader("EMPTYSTR");
	}
	
	@Override
	public void run() {
		T.root.addWord("mississippi");
		T.root.addWord("vrta");
		T.root.addWord("v");
		T.root.addWord("missouri");
		T.root.deleteWord("v");
		T.root.addWord("aky");
		T.root.addWord("je");
		T.root.addWord("v");
		T.root.addWord("tom");
		T.root.addWord("rozdiel");
		T.root.addWord("robot");
		T.root.addWord("nerobi");
		T.root.addWord("nemravne");
		T.root.addWord("veci");
		T.root.addWord("vrtacky");
		T.root.addWord("obcas");
		T.root.addWord("ano");
		T.root.addWord("vrtaky");
		T.root.addWord("vrtava");
		T.root.deleteWord("ano");
		T.root.addWord("nie");
		T.root.deleteWord("v");
		T.root.reposition();
		
	}

}
