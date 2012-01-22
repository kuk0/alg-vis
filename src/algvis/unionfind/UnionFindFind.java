package algvis.unionfind;

import java.util.Stack;

import algvis.core.Algorithm;
import algvis.core.Colors;

public class UnionFindFind extends Algorithm {
	public enum FindHeuristic {
		NONE, COMPRESSION, HALVING, SPLITTING
	};

	UnionFindNode u = null;

	public FindHeuristic findState;
	UnionFind UF;

	public UnionFindFind(UnionFind UF) {
		super(UF);
		this.UF = UF;
		setState(UF.pathCompression);
	}

	public UnionFindFind(UnionFind UF, UnionFindNode u) {
		super(UF);
		this.UF = UF;
		setState(UF.pathCompression);
		this.u = u;
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
		case SPLITTING:
			return findSplitting(u);
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
			addStep("ufdownson");
			mysuspend();
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
		UnionFindNode grandchild = null;
		UnionFindNode child = null;

		// looking for a root
		if (v.parent != null) {
			grandchild = v;
			v.bgcolor = Colors.INSERT;
		}

		if (v.parent != null) {
			addStep("ufup");
			mysuspend();
			v.bgcolor = Colors.FIND;
			v = (UnionFindNode) v.parent;
			child = v;
			v.bgcolor = Colors.INSERT;
		}

		boolean odd = true;
		if (v.parent != null)
			do {
			addStep("ufup");
			mysuspend();
			v.bgcolor = Colors.FIND;
			v = (UnionFindNode) v.parent;
			v.bgcolor = Colors.INSERT;
			if (odd) {
				odd = false;
				grandchild.bgcolor = Colors.CACHED;
				addStep("ufupspecial");
				mysuspend();
				grandchild.bgcolor = Colors.NORMAL;
				grandchild.parent.deleteChild(grandchild);
				v.addChild(grandchild);
				UF.reposition();
			} else {
				odd = true;
			}
			grandchild.bgcolor = Colors.NORMAL;
			grandchild = child;
			child = v;
		} while (v.parent != null);

		// root found
		if (grandchild != null)
			grandchild.bgcolor = Colors.NORMAL;
		if (child != null)
			child.bgcolor = Colors.NORMAL;
		v.bgcolor = Colors.FOUND;
		result = v;
		addStep("ufrootfound", result.key);
		mysuspend();

		u.unmark();
		return result;
	}

	public UnionFindNode findSplitting(UnionFindNode u) {
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
		UnionFindNode grandchild = null;
		UnionFindNode child = null;

		// looking for root
		if (v.parent != null) {
			grandchild = v;
			v.bgcolor = Colors.INSERT;
		}

		if (v.parent != null) {
			addStep("ufup");
			mysuspend();
			v.bgcolor = Colors.FIND;
			v = (UnionFindNode) v.parent;
			child = v;
			v.bgcolor = Colors.INSERT;
		}

		if (v.parent != null)
			do {
				addStep("ufup");
				mysuspend();
				v.bgcolor = Colors.FIND;
				v = (UnionFindNode) v.parent;
				v.bgcolor = Colors.INSERT;
				grandchild.bgcolor = Colors.CACHED;
				addStep("ufupspecial");
				mysuspend();
				grandchild.bgcolor = Colors.NORMAL;
				grandchild.parent.deleteChild(grandchild);
				v.addChild(grandchild);
				UF.reposition();
				grandchild.bgcolor = Colors.NORMAL;
				grandchild = child;
				child = v;
			} while (v.parent != null);

		// root found
		if (grandchild != null)
			grandchild.bgcolor = Colors.NORMAL;
		if (child != null)
			child.bgcolor = Colors.NORMAL;
		v.bgcolor = Colors.FOUND;
		result = v;
		addStep("ufrootfound", result.key);
		mysuspend();

		u.unmark();
		return result;
	}
}
