package algvis.btree;

import algvis.core.VisPanel;

public class a234Tree extends BTree {
	public static String dsName = "234tree";
	
	public String getName() {
		return "234tree";
	}
	
	public a234Tree(VisPanel M) {
		super(M);
		order = 4;
	}
}
