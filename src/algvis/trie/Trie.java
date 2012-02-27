package algvis.trie;

import algvis.core.DataStructure;
import algvis.core.View;
import algvis.core.VisPanel;

public class Trie extends DataStructure {
	public static String adtName = "triea";
	public static String dsName = "triei";

	private TrieNode root = null;
	private TrieNode v = null;

	public Trie(VisPanel M) {
		super(M);
		clear();
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
		root = new TrieNode(this);
		root.reposition();
		v = null;
	}

	public TrieNode getV() {
		return v;
	}

	public TrieNode setV(TrieNode v) {
		this.v = v;
		return v;
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
		v = getV();
		if (v != null) {
			v.move();
			v.drawTrieCH(V);
		}
	}

	@Override
	public void random(int n) {
		boolean p = M.pause;
		M.pause = false;
		insert("mississippi$");
		insert("vrta$");
		insert("v$");
		insert("missouri$");
		//delete("v$");
		insert("aky$");
		insert("je$");
		insert("v$");
		insert("tom$");
		insert("rozdiel$");
		insert("robot$");
		insert("nerobi$");
		insert("nemravne$");
		insert("veci$");
		insert("vrtacky$");
		insert("obcas$");
		insert("ano$");
		insert("vrtaky$");
		insert("vrtava$");
		//delete("ano$");
		insert("nie$");
		//delete("v$");
		insert("robotika$");
		//delete("robotika$");
		insert("ma$");
		insert("mama$");
		insert("emu$");
		insert("she$");
		insert("sells$");
		insert("sea$");
		insert("shore$");
		insert("the$"); 
		insert("by$");
		insert("sheer$");
		reposition();
		M.pause = p;
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

	public void reposition() {
		getRoot().reposition();
	}

}
