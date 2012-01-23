package algvis.heap;

import algvis.core.Algorithm;
import algvis.core.Node;

public class HeapAlg extends Algorithm {
	Heap H;
	HeapNode v;

	public HeapAlg(Heap H) {
		super(H);
		this.H = H;
	}

	public void bubbleup(HeapNode v) {
		if (H.minHeap) {
			addStep("minheapbubbleup");
		} else {
			addStep("maxheapbubbleup");
		}
		HeapNode w = v.getParent();
		while (w != null && v.prec(w)) {
			H.v = new HeapNode(v);
			H.v2 = new HeapNode(w);
			v.key = Node.NOKEY;
			w.key = Node.NOKEY;
			H.v.goTo(w);
			H.v2.goTo(v);
			mysuspend();
			v.key = H.v2.key;
			w.key = H.v.key;
			v.bgcolor = H.v2.bgcolor;
			w.bgcolor = H.v.bgcolor;
			H.v = null;
			H.v2 = null;
			v = w;
			w = w.getParent();
		}
		addStep("done");
	}
}
