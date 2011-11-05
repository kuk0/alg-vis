package algvis.treenode;

import algvis.core.DataStructure;
import algvis.core.View;
import algvis.core.VisPanel;

public class TreeDS extends DataStructure {
	public static String dsName = "bst";
	public static String adtName = "dictionary";

	public TreeNode root = new TreeNode(this, 1);
	int N = 1;
	
	public TreeDS(VisPanel M) {
		super(M);
	}

	@Override
	public String stats() {
		return "Stats are not set";
	}

	@Override
	public void insert(int x) {
		N++;
		//start(new TreeAppend(this, x, N));
		this.root.append(x, N);
		System.out.print("Skap1");
		this.root.reposition();
		System.out.print("Skap2");
	}

	@Override
	public void clear() {
		root = null;
	}

	@Override
	public void draw(View V) {
		if (root != null) {
			root.moveTree();
			root.drawTree(V);
		}
	}

	public void reposition() {
		x1 = x2 = y1 = y2 = 0;
		root.reposition();
		M.S.V.setBounds(x1-50, y1-50, x2+50, y2+50);
	}
}
