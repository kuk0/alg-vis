package algvis.leftistheap;

import algvis.core.InputField;

public class LeftHeapDecrKey extends LeftHeapAlg{
	int delta;
	
	public LeftHeapDecrKey(LeftHeap H, LeftHeapNode v, int delta) {
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
