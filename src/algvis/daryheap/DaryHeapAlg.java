package algvis.daryheap;

import algvis.core.Algorithm;
import algvis.core.Node;

public class DaryHeapAlg extends Algorithm {
	DaryHeap H;
	DaryHeapNode v;

	public DaryHeapAlg(DaryHeap H) {
		super(H);
		this.H = H;
	}

	public void bubbleup(DaryHeapNode v) {
		if (H.minHeap) {
			addStep("minheapbubbleup");
		} else {
			addStep("maxheapbubbleup");
		}
		DaryHeapNode w = v.getParent();
		while (w != null && v.prec(w)) {
			H.v = new DaryHeapNode(v);
			H.v.mark();
			H.v2 = new DaryHeapNode(w);
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

		addStep("done");
	}

	public void bubbledown(DaryHeapNode v) {
		DaryHeapNode w;

		while (true) {
			w = null;
			if (v.isLeaf()) {
				break;
			}

			w = v.findMaxSon();
			if (v.prec(w)) {
				break;
			}
			H.v = new DaryHeapNode(v);
			H.v.mark();
			H.v2 = new DaryHeapNode(w);
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
		}

		addStep("done");
	}
}

