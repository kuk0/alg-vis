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
			v.setKey(v.getKey() - delta);
			if (v.getKey() < 1)
				v.setKey(1);
		} else {
			v.setKey(v.getKey() + delta);
			if (v.getKey() > InputField.MAX)
				v.setKey(InputField.MAX);
		}
		bubbleup(v);
	}
}
