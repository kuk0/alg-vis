package algvis.unionfind;

import algvis.core.Colors;

public class UnionFindUnion extends UnionFindFind {
	public enum UnionHeuristic {
		NONE, BYRANK
	}

	public UnionHeuristic unionState = UnionHeuristic.NONE;
	UnionFind UF;
	UnionFindNode u = null;
	UnionFindNode v = null;

	public UnionFindUnion(UnionFind UF, UnionFindNode u, UnionFindNode v) {
		super(UF);
		this.UF = UF;
		this.unionState = UF.unionState;
		this.u = u;
		this.v = v;
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
		UnionFindNode r1 = find(V);
		UnionFindNode r2 = find(W);
		if (r1 == r2) {
			addStep("ufsameset");
			mysuspend();
		} else {
			addStep("ufunionsimple");
			mysuspend();
			UF.sets.remove(r2);
			r1.addChild(r2);
		}

		r1.unmark();
		r1.bgcolor = Colors.NORMAL;
		r2.unmark();
		r2.bgcolor = Colors.NORMAL;

		UF.reposition();
		addNote("done");
		//UF.M.screen.V.resetView(); // only for testing, but still there should
									// be some correction.
	}

	private void unionByRank(UnionFindNode V, UnionFindNode W) {
		UnionFindNode r1 = find(V);
		UnionFindNode r2 = find(W);
		if (r1 == r2) {
			addStep("ufsameset");
			mysuspend();
		} else {
			addStep("ufunionbyrank", r1.rank, r2.rank);
			if (r1.rank > r2.rank) {
				addStep("ufunionfirstsecond");
				mysuspend();
				UF.sets.remove(r2);
				r1.addChild(r2);
			} else if (r1.rank < r2.rank) {
				addStep("ufunionsecondfirst");
				mysuspend();
				UF.sets.remove(r1);
				r2.addChild(r1);
			} else {
				addStep("ufunionsamerank");
				mysuspend();
				UF.sets.remove(r2);
				r1.addChild(r2);
				r1.rank++;
			}
		}

		r1.unmark();
		r1.bgcolor = Colors.NORMAL;
		r2.unmark();
		r2.bgcolor = Colors.NORMAL;

		UF.reposition();
		addNote("done");
		//UF.M.screen.V.resetView(); // only for testing, but still there should
									// be some correction.
	}
}
