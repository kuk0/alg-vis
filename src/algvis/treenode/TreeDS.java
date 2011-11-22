package algvis.treenode;

import java.util.Random;

import algvis.core.DataStructure;
import algvis.core.View;
import algvis.core.VisPanel;

public class TreeDS extends DataStructure {
	public static String dsName = "treeex";
	public static String adtName = "treeexample";

	public TreeNode root = new TreeNode(this, 1, 0, 0);
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
		if ((x > N) || (x < 1)) {
			Random g = new Random(System.currentTimeMillis());
			x = g.nextInt(N) + 1;
		}
		if (x == (N + 1)) {
			x = N;
		}
		++N;
		start(new TreeAppend(this, x, N));
	}

	@Override
	public void clear() {
		root = null;
	}

	@Override
	public void draw(View V) {
		root.moveTree();
		root.drawTree(V);
	}

	public void reposition() {
		x1 = x2 = y1 = y2 = 0;
		root.reposition();
		M.S.V.setBounds(x1 -= 50, y1 -= 50, x2 += 50, y2 += 50);
		// System.out.print(x1 + " " + y1 + " " + x2 + " " + y2 + "\n");
	}
}
