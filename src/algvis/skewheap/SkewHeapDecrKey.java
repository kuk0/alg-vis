package algvis.skewheap;

import algvis.core.InputField;

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
		if (H.minHeap) {
			v.key -= delta;
			if (v.key < 1)
				v.key = 1;
		} else {
			v.key += delta;
			if (v.key > InputField.MAX)
				v.key = InputField.MAX;
		}
		bubbleup(v);
	}
}
