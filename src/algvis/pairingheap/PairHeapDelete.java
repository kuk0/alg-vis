package algvis.pairingheap;

import algvis.core.DataStructure;

public class PairHeapDelete extends PairHeapAlg{

	int i;
	public Pairing pairState;
	
	public PairHeapDelete(DataStructure H, int i) {
		super(H);
		this.i = i;
		setHeader("deletion");
		this.pairState = this.H.pairState;
	}
	
	public void setState(Pairing state) {
		this.pairState = state;
	}
	
	@Override
	public void run() {
		if (H.root[i] == null) {
			return;
		}

		if (!H.minHeap) {
			addStep("maximum", H.root[i].key);
		} else {
			addStep("minimum", H.root[i].key);
		}

		H.v = new PairHeapNode(H.root[i]);
		H.v.mark();
		
		mysuspend();
		
		//spravime neviditelneho roota (vymazane minimum) a posunieme to o minsepy hore.
		
		H.v.goDown();

		H.root[i].state = -1; //<<----- potom odkomentovat
		H.root[i].shift(0, - PairingHeap.minsepy);
		
		switch (pairState) {
		case NAIVE:
			pairNaive(i);
			break;
		case LRRL:
			pairLRRL(i);
			break;
		/*
		case FB:
			break;
		case BF:
			break;
		case MULTI:
			break;
		case LAZYM:
			break;
		*/
		default:
			break;
		}
	}
}
