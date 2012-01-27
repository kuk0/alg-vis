package algvis.btree;

import algvis.core.VisPanel;

public class a23Tree extends BTree {
	public static String dsName = "23tree";

	@Override
	public String getName() {
		return "23tree";
	}

	public a23Tree(VisPanel M) {
		super(M);
		order = 3;
	}
}
