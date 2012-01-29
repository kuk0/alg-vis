package algvis.skewheap;

public class SkewHeapDecrKey extends SkewHeapAlg{
int delta;
	
	public SkewHeapDecrKey(SkewHeap H, SkewHeapNode v, int delta) {
		super(H);
		this.v = v;
		this.delta = delta;
		if (H.minHeap) {
			setHeader("decreasekey");
		} else {
			setHeader("increasekey");
		}
	}
	
	@Override
	public void run() {
		
	}
}
