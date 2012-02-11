package algvis.redblacktree;

import algvis.bst.BST;
import algvis.core.Layout;
import algvis.core.Node;
import algvis.core.View;
import algvis.core.VisPanel;

public class RB extends BST {
	public static String dsName = "redblack";
	RBNode NULL;
	public boolean mode24 = false;

	@Override
	public String getName() {
		return "redblack";
	}

	public RB(VisPanel M) {
		super(M);
		NULL = new RBNode(this, Node.NULL);
		NULL.setParent(NULL);
		NULL.setRight(NULL);
		NULL.setLeft(NULL);	
		NULL.setRed(false);
		NULL.size = NULL.height = NULL.sumh = 0;
		NULL.state = Node.INVISIBLE;
	}

	@Override
	public void insert(int x) {
		start(new RBInsert(this, x));
	}

	@Override
	public void find(int x) {
		start(new RBFind(this, x));
	}

	@Override
	public void delete(int x) {
		start(new RBDelete(this, x));
	}

	@Override
	public void draw(View V) {
		if (root != null) {
			root.moveTree();
			((RBNode) root).drawRBTree(V);
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
	}

	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}
}
