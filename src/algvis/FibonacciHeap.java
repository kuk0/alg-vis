package algvis;

public class FibonacciHeap extends LazyBinomialHeap {
	public FibonacciHeap(VisPanel M) {
		super(M);
	}

	@Override
	public void increaseKey(Node v, int delta) {
		start(new FibHeapIncrKey(this, (BinHeapNode)v, delta, active));
	}
}
