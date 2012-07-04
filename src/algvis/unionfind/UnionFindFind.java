/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.unionfind;

import java.util.Stack;

import algvis.core.Algorithm;
import algvis.core.NodeColor;

public class UnionFindFind extends Algorithm {
	public enum FindHeuristic {
		NONE, COMPRESSION, HALVING, SPLITTING
	}

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

	@Override
	public void run() {
		setHeader("uffind");
		UnionFindNode v = find(u);
		v.setColor(NodeColor.NORMAL);
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

		u.setColor(NodeColor.FIND);
		u.mark();
		addStep("uffindstart", u.getKey());
		mysuspend();

		// u is a representative
		if (u.getParent() == null) {
			u.setColor(NodeColor.FOUND);
			addStep("ufalreadyroot");
			mysuspend();
			u.unmark();
			return u;
		}

		v = u;

		// looking for root
		while (v.getParent() != null) {
			S.add(v);
			v.setColor(NodeColor.FIND);
			addStep("ufup");
			mysuspend();
			v = v.getParent();
		}

		// root found
		result = v;
		v.setColor(NodeColor.FOUND);
		addStep("ufrootfound", result.getKey());
		mysuspend();

		// traveling back
		while (!S.empty()) {
			v = S.pop();
			v.setColor(NodeColor.NORMAL);
		}

		// u.bgcolor = Colors.NORMAL;
		u.unmark();

		return result;
	}

	public UnionFindNode findWithCompression(UnionFindNode u) {
		Stack<UnionFindNode> S = new Stack<UnionFindNode>();
		UnionFindNode result = null;
		UnionFindNode v = null;

		u.setColor(NodeColor.FIND);
		u.mark();
		addStep("uffindstart", u.getKey());
		mysuspend();

		// u is a representative
		if (u.getParent() == null) {
			u.setColor(NodeColor.FOUND);
			addStep("ufalreadyroot");
			mysuspend();
			u.unmark();
			return u;
		}

		v = u;

		// looking for root
		while (v.getParent() != null) {
			S.add(v);
			//v.setColor(NodeColor.FIND);
			v.setGrey(true);
			addStep("ufup");
			mysuspend();
			v = v.getParent();
		}

		// root found
		result = v;
		v.setColor(NodeColor.FOUND);
		addStep("ufrootfound", result.getKey());
		addStep("ufdownstart");
		mysuspend();

		// don't compress a path of a son of a root
		if (!S.empty()) {
			addStep("ufdownson");
			mysuspend();
			v = S.pop();
			v.setColor(NodeColor.NORMAL);
		}

		while (!S.empty()) {
			addStep("ufdown");
			v = S.pop();
			//v.pointTo(result);
			mysuspend();
			//v.noArrow();
			v.setColor(NodeColor.NORMAL);
			v.getParent().deleteChild(v);
			UF.reposition();
			// mysuspend();
			result.addChild(v);
			UF.reposition();
			// mysuspend();
		}

		// u.bgcolor = Colors.NORMAL;
		mysuspend();
		u.unmark();
		result.setGrey(false);
		return result;
	}

	public UnionFindNode findHalving(UnionFindNode u) {
		UnionFindNode result = null;
		UnionFindNode v = null;

		u.setColor(NodeColor.FIND);

		// grey path
		UnionFindNode t = u;
		while (t.getParent() != null) {
			t.setGrey(true);
			t = t.getParent();
		}
		
		u.mark();
		addStep("uffindstart", u.getKey());
		mysuspend();

		// u is a representative
		if (u.getParent() == null) {
			u.setColor(NodeColor.FOUND);
			addStep("ufalreadyroot");
			mysuspend();
			u.unmark();
			return u;
		}

		v = u;
		UnionFindNode grandchild = null;
		UnionFindNode child = null;

		// looking for a root
		if (v.getParent() != null) {
			grandchild = v;
			v.setColor(NodeColor.INSERT);
		}

		if (v.getParent() != null) {
			addStep("ufup");
			mysuspend();
			v.setColor(NodeColor.FIND);
			v = v.getParent();
			child = v;
			v.setColor(NodeColor.INSERT);
		}

		boolean odd = true;
		if (v.getParent() != null)
			do {
				addStep("ufup");
				mysuspend();
				v.setColor(NodeColor.FIND);
				v.setGrey(true);
				v = v.getParent();
				v.setColor(NodeColor.INSERT);
				if (odd) {
					odd = false;
					grandchild.setColor(NodeColor.CACHED);
					addStep("ufupspecial");
					mysuspend();
					grandchild.setColor(NodeColor.NORMAL);
					grandchild.getParent().deleteChild(grandchild);
					v.addChild(grandchild);
					UF.reposition();
				} else {
					odd = true;
				}
				grandchild.setColor(NodeColor.NORMAL);
				grandchild = child;
				child = v;
			} while (v.getParent() != null);

		// root found
		if (grandchild != null)
			grandchild.setColor(NodeColor.NORMAL);
		if (child != null)
			child.setColor(NodeColor.NORMAL);
		v.setColor(NodeColor.FOUND);
		result = v;
		addStep("ufrootfound", result.getKey());
		mysuspend();

		u.unmark();
		result.setGrey(false);
		return result;
	}

	public UnionFindNode findSplitting(UnionFindNode u) {
		UnionFindNode result = null;
		UnionFindNode v = null;

		u.setColor(NodeColor.FIND);
		u.mark();
		addStep("uffindstart", u.getKey());

		// grey path
		UnionFindNode t = u;
		while (t.getParent() != null) {
			t.setGrey(true);
			t = t.getParent();
		}
		
		mysuspend();

		// u is a representative
		if (u.getParent() == null) {
			u.setColor(NodeColor.FOUND);
			addStep("ufalreadyroot");
			mysuspend();
			u.unmark();
			return u;
		}

		v = u;
		UnionFindNode grandchild = null;
		UnionFindNode child = null;

		// looking for root
		if (v.getParent() != null) {
			grandchild = v;
			v.setColor(NodeColor.INSERT);
		}

		if (v.getParent() != null) {
			addStep("ufup");
			mysuspend();
			v.setColor(NodeColor.FIND);
			v = v.getParent();
			child = v;
			v.setColor(NodeColor.INSERT);
		}

		if (v.getParent() != null)
			do {
				addStep("ufup");
				mysuspend();
				v.setColor(NodeColor.FIND);
				v = v.getParent();
				v.setColor(NodeColor.INSERT);
				grandchild.setColor(NodeColor.CACHED);
				addStep("ufupspecial");
				mysuspend();
				grandchild.setColor(NodeColor.NORMAL);
				grandchild.getParent().deleteChild(grandchild);
				v.addChild(grandchild);
				UF.reposition();
				grandchild.setColor(NodeColor.NORMAL);
				grandchild = child;
				child = v;
			} while (v.getParent() != null);

		// root found
		if (grandchild != null)
			grandchild.setColor(NodeColor.NORMAL);
		if (child != null)
			child.setColor(NodeColor.NORMAL);
		v.setColor(NodeColor.FOUND);
		result = v;
		addStep("ufrootfound", result.getKey());
		mysuspend();

		u.unmark();
		result.setGrey(false);
		return result;
	}
}
