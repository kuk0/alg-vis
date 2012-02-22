package algvis.trie;

import algvis.core.DataStructure;
import algvis.core.View;
import algvis.core.VisPanel;

public class Trie extends DataStructure {
	public static String adtName = "triea";
	public static String dsName = "triei";

	TrieNode root = null;

	public Trie(VisPanel M) {
		super(M);
		root = new TrieNode(this);
		root.reposition();
	}

	@Override
	public String getName() {
		return "triei";
	}

	@Override
	public String stats() {
		return "";
	}

	@Override
	public void insert(int x) {
		//
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	public TrieNode getRoot() {
		return root;
	}

	public void setRoot(TrieNode root) {
		this.root = root;
	}

	@Override
	public void draw(View V) {
		TrieNode v = getRoot();
		if (v != null) {
			v.moveTree();
			v.drawTree(V);
		}
	}

	public void insert(String s) {
		start(new TrieInsert(this, s));
	}

	public void find(String s) {
		start(new TrieFind(this, s));
	}

	public void delete(String s) {
		start(new TrieDelete(this, s));
	}

}
