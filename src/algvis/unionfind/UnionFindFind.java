package algvis.unionfind;

import algvis.core.Algorithm;
import algvis.core.TreeNode;

public class UnionFindFind extends Algorithm {
	public static final int SIMPLE = 1, COMPRESSION = 2, ZIGZAG = 3;
	public int state = SIMPLE;
	UnionFind U;
	
	public UnionFindFind(UnionFind U) {
		super(U.M);
		this.U = U;
		setHeader(null);
	}

	public int setState(int state) {
		switch (state) {
		case SIMPLE:
			this.state = SIMPLE;
			return 1;
		case COMPRESSION:
			this.state = COMPRESSION;
			return 1;
		case ZIGZAG:
			this.state = ZIGZAG;
			return 1;
		default:
			return 0;
		}
	}

	public TreeNode Find(TreeNode T) {
		switch (state) {
		case SIMPLE:
			this.state = SIMPLE;
			return null;
		case COMPRESSION:
			this.state = COMPRESSION;
			return null;
		case ZIGZAG:
			this.state = ZIGZAG;
			return null;
		default:
			return null;
		}
	}
	
	public TreeNode FindSimple(TreeNode T) {
		return null;
	}

	public TreeNode FindWithCompression(TreeNode T) {
		return null;
	}

	public TreeNode FindZigZag(TreeNode T) {
		return null;
	}
}
