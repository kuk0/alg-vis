package algvis.unionfind;

import algvis.core.DataStructure;
import algvis.core.TreeNode;

public class UnionFindNode extends TreeNode {
	public int rank = 0;

	public UnionFindNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public UnionFindNode(DataStructure D, int key) {
		super(D, key);
	}

}
