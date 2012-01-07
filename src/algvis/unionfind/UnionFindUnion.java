package algvis.unionfind;

import algvis.core.Algorithm;
import algvis.core.VisPanel;

public class UnionFindUnion extends Algorithm {
	public static final int SIMPLE = 1, BYRANK = 2;
	public int state = SIMPLE;
	UnionFind U;
	
	public UnionFindUnion(UnionFind U) {
		super(U.M);
		this.U = U;
		setHeader(null);
	}

	public int setState(int state) {
		switch (state) {
		case SIMPLE:
			this.state = SIMPLE;
			return 1;
		case BYRANK:
			this.state = BYRANK;
			return 1;
		default:
			return 0;
		}
	}
	
	public int union(int element1, int element2) {
		switch (state) {
		case SIMPLE:
			return unionSimple(element1, element2);
		case BYRANK:
			return unionByRank(element1, element2);
		default:
			return -1;
		}
	}
	
	private int unionSimple(int element1, int element2) {
		return -1;
	}

	private int unionByRank(int element1, int element2) {
		return -1;
	}
}
