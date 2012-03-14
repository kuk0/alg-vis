package algvis.leftistheap;

import algvis.core.Algorithm;
import algvis.core.Node;

public class LeftHeapAlg extends Algorithm {
	LeftHeap H;
	LeftHeapNode v;

	public LeftHeapAlg(LeftHeap H) {
		super(H);
		this.H = H;
	}

	public void meld(int i) {
		LeftHeapNode w = H.root[i];
		H.root[0].mark();
		w.mark();
		addStep("leftmeldstart");
		mysuspend();
		while (true) {
			H.root[0].mark();
			w.mark();
			if (w.prec(H.root[0])) {
				if (!H.minHeap) {
					addStep("leftmeldrightg", w.key, H.root[0].key);
				} else {
					addStep("leftmeldrightl", w.key, H.root[0].key);
				}
				mysuspend();
			} else {
				if (!H.minHeap) {
					addStep("leftmeldswapl", w.key, H.root[0].key);
				} else {
					addStep("leftmeldswapg", w.key, H.root[0].key);
				}
				w.setDoubleArrow(H.root[0]);
				mysuspend();
				w.noDoubleArrow();
				LeftHeapNode tmp1 = w.getParent();
				LeftHeapNode tmp2 = H.root[0];

				H.root[0] = w;
				if (w.getParent() != null) {
					H.root[0].setParent(null);
					tmp1.setRight(tmp2);
					tmp2.setParent(tmp1);
					w = tmp2;
				} else {
					H.root[i] = tmp2;
					w = H.root[i];
				}
				H.reposition();
			}

			if (w.getParent() != null) {
				w.getParent().dashedrightl = false;
			}
			H.root[0].repos(H.root[0].tox, H.root[0].toy + LeftHeap.minsepy);// + 2* LeftHeapNode.radius);
			H.root[0].unmark();
			w.unmark();

			if (w.getRight() == null) {
				addStep("leftmeldnoson", H.root[0].key, w.key);
				mysuspend();
				w.linkRight(H.root[0]);
				H.root[0] = null;
				H.reposition();
				break;
			}

			w.dashedrightl = true;
			w = w.getRight();
			mysuspend();
		}
		addNote("leftrankupdate");
		mysuspend();

		LeftHeapNode tmp = w;

		while (tmp != null) {
			if ((tmp.getLeft() != null) && (tmp.getRight() != null)) {
				tmp.rank = Math.min(tmp.getLeft().rank, tmp.getRight().rank) + 1;
			} else {
				tmp.rank = 1;
			}

			tmp = tmp.getParent();
		}
		addNote("leftrankstart");
		mysuspend();

		tmp = w;
		while (tmp != null) {
			int l;
			if (tmp.getLeft() == null) {
				l = -47;
			} else {
				tmp.getLeft().mark();
				l = tmp.getLeft().rank;
			}
			int r;
			if (tmp.getRight() == null) {
				r = -47;
			} else {
				tmp.getRight().mark();
				r = tmp.getRight().rank;
			}
			if (l < r) {
				tmp.swapChildren();
			}
			mysuspend();

			H.reposition();

			if (tmp.getLeft() != null) {
				tmp.getLeft().unmark();
			}
			if (tmp.getRight() != null) {
				tmp.getRight().unmark();
			}
			tmp = tmp.getParent();
		}

		H.reposition();
		addNote("done");
	}
	
	
	public void bubbleup(LeftHeapNode v) {
		if (H.minHeap) {
			addStep("minheapbubbleup");
		} else {
			addStep("maxheapbubbleup");
		}
		v.mark();
		mysuspend();
		v.unmark();		
		LeftHeapNode w = v.getParent();
		while (w != null && v.prec(w)) {
			H.v = new LeftHeapNode(v);
			H.v.rank = -1;
			H.v.mark();			
			H.v2 = new LeftHeapNode(w);
			H.v2.rank = -1;
			v.key = Node.NOKEY;
			w.key = Node.NOKEY;
			H.v.goTo(w);
			H.v2.goTo(v);
			mysuspend();
			v.key = H.v2.key;
			w.key = H.v.key;
			v.setColor(H.v2.getColor());
			w.setColor(H.v.getColor());
			H.v.unmark();			
			H.v = null;
			H.v2 = null;
			v = w;
			w = w.getParent();
		}
		addNote("done");
	}

}
