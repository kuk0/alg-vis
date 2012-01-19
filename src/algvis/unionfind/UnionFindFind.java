package algvis.unionfind;

import java.util.Stack;

import algvis.core.Algorithm;
import algvis.core.Colors;

public class UnionFindFind extends Algorithm {
	public enum FindHeuristic {
		NONE, COMPRESSION, HALVING, SPLITTING
	};
	UnionFindNode u = null;

	public FindHeuristic findState = FindHeuristic.NONE;
	UnionFind UF;

	public UnionFindFind(UnionFind UF) {
		super(UF);
		this.UF = UF;
		setState(UF.pathCompression);
	}

	public UnionFindFind(UnionFind UF, int element1) {
		super(UF);
		this.UF = UF;
		setState(UF.pathCompression);
		u = UF.at(element1);
	}
	
	public void run() {
		setHeader("uffind");
		UnionFindNode v = find(u);
		v.bgcolor = Colors.NORMAL;
		addNote("done");
	}

	public void setState(FindHeuristic state) {
		this.findState = state;
	}

	public UnionFindNode find(UnionFindNode u) {
		switch (findState) {
		case NONE:
			return findSimple(u);
		case COMPRESSION:
			return findWithCompression(u);
		case HALVING:
			return findHalving(u);
		default:
			return null;
		}
	}

	public UnionFindNode findSimple(UnionFindNode u) {
		Stack<UnionFindNode> S = new Stack<UnionFindNode>();
		UnionFindNode result = null;
		UnionFindNode v = null;

		u.bgcolor = Colors.FIND;
		u.mark();
		addStep("uffindstart", u.key);
		mysuspend();

		// u is a representative
		if (u.parent == null) {
			u.bgcolor = Colors.FOUND;
			addStep("ufalreadyroot");
			mysuspend();
			u.unmark();
			return u;
		}

		v = (UnionFindNode) u;

		// looking for root
		while (v.parent != null) {
			S.add(v);
			v.bgcolor = Colors.FIND;
			addStep("ufup");
			mysuspend();
			v = (UnionFindNode) v.parent;
		}

		// root found
		result = v;
		v.bgcolor = Colors.FOUND;
		addStep("ufrootfound", result.key);
		mysuspend();

		// traveling back
		while (!S.empty()) {
			v = S.pop();
			v.bgcolor = Colors.NORMAL;
		}

		// u.bgcolor = Colors.NORMAL;
		u.unmark();

		return result;
	}

	public UnionFindNode findWithCompression(UnionFindNode u) {
		Stack<UnionFindNode> S = new Stack<UnionFindNode>();
		UnionFindNode result = null;
		UnionFindNode v = null;

		u.bgcolor = Colors.FIND;
		u.mark();
		addStep("uffindstart", u.key);
		mysuspend();

		// u is a representative
		if (u.parent == null) {
			u.bgcolor = Colors.FOUND;
			addStep("ufalreadyroot");
			mysuspend();
			u.unmark();
			return u;
		}

		v = (UnionFindNode) u;

		// looking for root
		while (v.parent != null) {
			S.add(v);
			v.bgcolor = Colors.FIND;
			addStep("ufup");
			mysuspend();
			v = (UnionFindNode) v.parent;
		}

		// root found
		result = v;
		v.bgcolor = Colors.FOUND;
		addStep("ufrootfound", result.key);
		addStep("ufdownstart");
		mysuspend();

		// don't compress a path of a son of a root
		if (!S.empty()) {
			v = S.pop();
			v.bgcolor = Colors.NORMAL;
		}

		while (!S.empty()) {
			addStep("ufdown");
			mysuspend();
			v = S.pop();
			v.bgcolor = Colors.NORMAL;
			v.parent.deleteChild(v);
			UF.reposition();
			// mysuspend();
			result.addChild(v);
			UF.reposition();
			// mysuspend();
		}

		// u.bgcolor = Colors.NORMAL;
		u.unmark();
		return result;
	}

	public UnionFindNode findHalving(UnionFindNode u) {
		return null;
	}
}
