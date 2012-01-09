package algvis.heap;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Node;

public class HeapAlg extends Algorithm {
	Heap H;
	BSTNode v;

	public HeapAlg(Heap H) {
		super(H.M);
		this.H = H;
	}
	
	public void bubbleup(BSTNode v) {
		if (H.minHeap) {
			setText("minheapbubbleup");
		} else {
			setText("maxheapbubbleup");
		}
		BSTNode w = v.parent;
		while (w != null && ((HeapNode) v).prec(w)) {
			H.v = new HeapNode((HeapNode) v);
			H.v2 = new HeapNode((HeapNode) w);
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
			w = w.parent;
		}
		setText("done");
	}
}
