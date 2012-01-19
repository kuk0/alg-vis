package algvis.unionfind;

import algvis.core.Colors;

public class UnionFindUnion extends UnionFindFind {
	public enum UnionHeuristic {
		NONE, BYRANK
	}

	public UnionHeuristic unionState = UnionHeuristic.BYRANK;
	UnionFind UF;
	UnionFindNode u = null;
	UnionFindNode v = null;

	public UnionFindUnion(UnionFind UF, int element1, int element2) {
		super(UF);
		this.UF = UF;
		this.unionState = UF.unionState;
		this.u = this.UF.at(element1);
		this.v = this.UF.at(element2);
		setHeader("ufunion");
	}

	public void setState(UnionHeuristic state) {
		this.unionState = state;
	}

	public void run() {
		switch (unionState) {
		case NONE:
			unionSimple(u, v);
			break;
		case BYRANK:
			unionByRank(u, v);
			break;
		default:
			break;
		}
	}

	private void unionSimple(UnionFindNode V, UnionFindNode W) {
		UnionFindNode R1 = find(V);
		UnionFindNode R2 = find(W);
		if (R1 == R2) {
			addStep("ufsameset");
			mysuspend();
		} else {
			addStep("ufunionsimple");
			mysuspend();
			UF.sets.remove(R2);
			R1.addChild(R2);
		}

		R1.unmark();
		R1.bgcolor = Colors.NORMAL;
		R2.unmark();
		R2.bgcolor = Colors.NORMAL;

		UF.reposition();
		addNote("done");
		UF.M.screen.V.resetView(); // only for testing, but still there should
									// be some correction.
	}

	private void unionByRank(UnionFindNode V, UnionFindNode W) {
		UnionFindNode R1 = find(V);
		UnionFindNode R2 = find(W);
		if (R1 == R2) {
			addStep("ufsameset");
			mysuspend();
		} else {
			addStep("ufunionbyrank", R1.rank, R2.rank);
			if (R1.rank > R2.rank) {
				addStep("ufunionfirstsecond");
				mysuspend();
				UF.sets.remove(R2);
				R1.addChild(R2);
			} else if (R1.rank < R2.rank) {
				addStep("ufunionsecondfirst");
				mysuspend();
				UF.sets.remove(R1);
				R2.addChild(R1);
			} else {
				addStep("ufunionsamerank");
				mysuspend();
				UF.sets.remove(R2);
				R1.addChild(R2);
				R1.rank++;
			}
		}

		R1.unmark();
		R1.bgcolor = Colors.NORMAL;
		R2.unmark();
		R2.bgcolor = Colors.NORMAL;

		UF.reposition();
		addNote("done");
		UF.M.screen.V.resetView(); // only for testing, but still there should
									// be some correction.
	}
}
