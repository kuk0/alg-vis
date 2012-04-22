package algvis.pairingheap;

import algvis.core.DataStructure;

public class PairHeapMeld extends PairHeapAlg{
	int i, j;
	
	public PairHeapMeld(DataStructure H, int i, int j) {
		super(H);
		this.i = i;
		this.j = j;
		setHeader("melding");
	}
	
	@Override
	public void run() {
		if (i == j) {
			return;
		}
		if (H.root[i] == null) {
			H.root[i] = H.root[j];
			H.root[j] = null;
			if (H.root[i] != null) {
				H.root[i].highlightTree();
			}
			H.reposition();
			// heap #1 is empty; done;
			return;
		}

		if (H.root[j] == null) {
			// heap #2 is empty; done;
			return;
		}
		
		H.root[0] = H.root[j];
		if (j != 0) {
			H.root[j] = null;
		}
		H.active = i;
		H.root[0].highlightTree();
		H.root[i].highlightTree();
		H.root[0].mark();
		H.root[i].mark();
		if (H.root[i].key < H.root[0].key){
			if(H.minHeap){
				addStep("pairlinkmin", H.root[i].key, H.root[0].key);
			} else {
				addStep("pairlinkmax", H.root[i].key, H.root[0].key);
			}
		} else {
			if(H.minHeap){
				addStep("pairlinkmin", H.root[0].key, H.root[i].key);
			} else {
				addStep("pairlinkmax", H.root[0].key, H.root[i].key);
			}
		}
		mysuspend();
		H.root[0].unmark();
		H.root[i].unmark();
		link(i,0);
		H.root[0] = null;
		H.reposition();
		addNote("done");
	}

}
