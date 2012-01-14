package algvis.unionfind;

public class UnionFindUnion extends UnionFindFind {
	public enum unionType {
		SIMPLE, BYRANK
	}

	public unionType unionState = unionType.BYRANK;
	UnionFind U;
	UnionFindNode V = null;
	UnionFindNode W = null;

	public UnionFindUnion(UnionFind U, int element1, int element2) {
		super(U);
		this.U = U;
		this.unionState = U.unionState;
		this.V = this.U.at(element1);
		this.W = this.U.at(element2);
		setHeader("ufunion");
	}

	public void setState(unionType state) {
		this.unionState = state;
	}

	public void run() {
		switch (unionState) {
		case SIMPLE:
			unionSimple(V, W);
			break;
		case BYRANK:
			unionByRank(V, W);
			break;
		default:
			break;
		}
	}

	private void unionSimple(UnionFindNode V, UnionFindNode W) {
		UnionFindNode R1 = find(V);
		UnionFindNode R2 = find(W);
		if (R1 == R2) {
			setText("ufsameset");
			mysuspend();
		} else {
			setText("ufunionsimple");
			mysuspend();
			R1.addChild(R2);
		}
		if (R2 != R1) {
			U.sets.remove(R2);
		}
		U.reposition();
		setText("done");
	}

	private void unionByRank(UnionFindNode V, UnionFindNode W) {
		UnionFindNode R1 = find(V);
		UnionFindNode R2 = find(W);
		if (R1 == R2) {
			setText("ufsameset");
			mysuspend();
		} else {
			setText("ufunionbyrank", R1.rank, R2.rank);
			if (R1.rank > R2.rank) {
				setText("ufunionfirstsecond");
				mysuspend();
				R1.addChild(R2);
				U.sets.remove(R2);
			} else if (R1.rank < R2.rank) {
				setText("ufunionsecondfirst");
				mysuspend();
				R2.addChild(R1);
				U.sets.remove(R1);
			} else {
				setText("ufunionsamerank");
				mysuspend();
				R1.addChild(R2);
				R1.rank++;
				U.sets.remove(R2);
			}
		}
		U.reposition();
		setText("done");
		U.M.screen.V.resetView(); // only for testing, but still there should be
									// some correction.
	}
}
