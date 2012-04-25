package algvis.skewheap;

import algvis.core.Algorithm;
import algvis.core.Node;

public class SkewHeapAlg extends Algorithm {
	SkewHeap H;
	SkewHeapNode v;

	public SkewHeapAlg(SkewHeap H) {
		super(H);
		this.H = H;
	}
	
	public void meld(int i) {
		SkewHeapNode w = H.root[i];
		H.root[0].mark();
		w.mark();
		addStep("leftmeldstart");
		mysuspend();
		while (true) {
			H.root[0].mark();
			w.mark();
			if (w.prec(H.root[0])) {
				if (!H.minHeap) {
					addStep("leftmeldrightg", w.getKey(), H.root[0].getKey());
				} else {
					addStep("leftmeldrightl", w.getKey(), H.root[0].getKey());
				}
				mysuspend();
			} else {
				if (!H.minHeap) {
					addStep("leftmeldswapl", w.getKey(), H.root[0].getKey());
				} else {
					addStep("leftmeldswapg", w.getKey(), H.root[0].getKey());
				}
				w.setDoubleArrow(H.root[0]);
				mysuspend();
				w.noDoubleArrow();
				SkewHeapNode tmp1 = w.getParent();
				SkewHeapNode tmp2 = H.root[0];

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

			H.root[0].repos(H.root[0].tox, H.root[0].toy + SkewHeap.minsepy);// + 2* SkewHeapNode.radius);
			H.root[0].unmark();
			w.unmark();

			if (w.getRight() == null) {
				addStep("leftmeldnoson", H.root[0].getKey(), w.getKey());
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

		//povymiename synov, ale nie na zaklade ranku, ale vsetkych okrem synov posledneho vrcholu z pravej cesty 
		addNote("skewheapswap");
		mysuspend();

		SkewHeapNode tmp = w;
		//najdeme predposledny vrchol v pravej ceste 
		while (tmp.getRight() != null){			
			tmp = tmp.getRight();
		}
		tmp = tmp.getParent();
		
		while (tmp != null) {
			if (tmp.getLeft() != null) {
				tmp.getLeft().mark();
			}
			if (tmp.getRight() != null) {
				tmp.getRight().mark();
			}
			
			tmp.swapChildren();
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
	
	public void bubbleup(SkewHeapNode v) {
		if (H.minHeap) {
			addStep("minheapbubbleup");
		} else {
			addStep("maxheapbubbleup");
		}
		v.mark();
		mysuspend();
		v.unmark();		
		SkewHeapNode w = v.getParent();
		while (w != null && v.prec(w)) {
			H.v = new SkewHeapNode(v);
			H.v.mark();
			H.v2 = new SkewHeapNode(w);
			v.setKey(Node.NOKEY);
			w.setKey(Node.NOKEY);
			H.v.goTo(w);
			H.v2.goTo(v);
			mysuspend();
			v.setKey(H.v2.getKey());
			w.setKey(H.v.getKey());
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
