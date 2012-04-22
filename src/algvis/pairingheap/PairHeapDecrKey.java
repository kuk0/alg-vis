package algvis.pairingheap;

import algvis.core.InputField;

public class PairHeapDecrKey extends PairHeapAlg{
	int delta;
	
	public PairHeapDecrKey(PairingHeap D, PairHeapNode v, int delta) {
		super(D);
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
		if (!v.isRoot()){
			if (H.minHeap){
				addStep("pairdecr"); //zvysili sme hodnotu, dieta odtrhneme a prilinkujeme
			} else {
				addStep("pairincr"); 
			}
			H.root[0] = v;
			H.root[0].mark();
			mysuspend();
			v.getParent().deleteChild(v);
			
			H.root[H.active].mark();
			H.reposition();
			mysuspend();
			H.root[0].unmark();
			H.root[H.active].unmark();
			link(H.active, 0);
			H.root[0] = null;
			H.reposition();
		}
		
		addNote("done");
		
	}

}
