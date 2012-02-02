package algvis.unionfind;

import algvis.core.DataStructure;
import algvis.core.TreeNode;
import algvis.core.View;

public class UnionFindNode extends TreeNode {
	public int rank = 0;
	public boolean greyPair = false;

	public UnionFindNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public UnionFindNode(DataStructure D, int key) {
		super(D, key);
	}

	public void unsetGrey() {
		TreeNode w = child;
		while (w != null) {
			((UnionFindNode)w).unsetGrey();
			w = w.right;
		}
		greyPair = false;
	}
	
	public void drawGrey(View v) {
		TreeNode w = child;
		while (w != null) {
			((UnionFindNode)w).drawGrey(v);
			w = w.right;
		}
		if (greyPair && parent != null) {
			v.drawWideLine(x, y, parent.x, parent.y, 10.0f);			
		}
	}
	
	@Override
	public void drawTree(View v) {
		drawGrey(v);
		super.drawTree(v);		
	}
}
