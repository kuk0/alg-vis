package algvis.fibonacciheap;

import algvis.binomialheap.BinHeapNode;
import algvis.core.Node;
import algvis.core.VisPanel;
import algvis.lazybinomialheap.LazyBinomialHeap;

public class FibonacciHeap extends LazyBinomialHeap {
	public FibonacciHeap(VisPanel M) {
		super(M);
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		start(new FibHeapDecrKey(this, (BinHeapNode)v, delta, active));
	}
}
