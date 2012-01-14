package algvis.unionfind;

import java.util.Stack;

import algvis.core.Algorithm;

public class UnionFindFind extends Algorithm {
	public enum findType {
		SIMPLE, COMPRESSION, ZIGZAG
	};

	public findType findState = findType.SIMPLE;
	UnionFind U;

	public UnionFindFind(UnionFind U) {
		super(U.M);
		this.U = U;
		setState(this.U.findState);
	}

	public void setState(findType state) {
		this.findState = state;
	}

	public UnionFindNode find(UnionFindNode T) {
		switch (findState) {
		case SIMPLE:
			return findSimple(T);
		case COMPRESSION:
			return findWithCompression(T);
		case ZIGZAG:
			return findZigZag(T);
		default:
			return null;
		}
	}

	public UnionFindNode findSimple(UnionFindNode T) {
		setText("uffindstart", T.key);
		mysuspend();
		Stack<UnionFindNode> S = new Stack<UnionFindNode>();
		UnionFindNode result = T;
		while (T.parent != null) {
			S.add(T);
			T.mark();
			setText("ufup");
			mysuspend();
			T = (UnionFindNode) T.parent;
		}
		result = T;
		setText("ufrootfound", result.key);
		mysuspend();
		while (!S.empty()) {
			T = S.pop();
			T.unmark();
		}
		return result;
	}

	public UnionFindNode findWithCompression(UnionFindNode T) {
		setText("uffindstart", T.key);
		mysuspend();
		Stack<UnionFindNode> S = new Stack<UnionFindNode>();
		UnionFindNode result = T;
		while (T.parent != null) {
			S.add(T);
			T.mark();
			setText("ufup");
			mysuspend();
			T = (UnionFindNode) T.parent;
		}
		result = T;
		setText("ufrootfound", result.key);
		setText("ufdownstart");
		mysuspend();
		while (!S.empty()) {
			setText("ufdown");
			mysuspend();
			T = S.pop();
			T.unmark();
			T.parent.deleteChild(T);
			result.addChild(T);
			// mysuspend();
			U.reposition();
		}
		return result;
	}

	public UnionFindNode findZigZag(UnionFindNode T) {
		return null;
	}
}
