package algvis.daryheap;


public class DaryHeapInsert extends DaryHeapAlg{
	DaryHeap T; //prepisat na H
	DaryHeapNode v;
	int K;

	public DaryHeapInsert(DaryHeap T, int x) {
		super(T);
		T.v = v = new DaryHeapNode(T, K = x);
		this.T = T;
		setHeader("insertion");
	}
	
	@Override
	public void run() {
		System.out.print("last je " + ((DaryHeap) T).last.key + "\n");		
		if ( (H.root != null) && (H.root.nnodes == 1000) ) {
			addStep("heapfull");
			H.v = null;
			return;
		}		
		DaryHeapNode w;

		//int n = H.root.nnodes - 1;
		if (H.root == null) {
			H.root = w = v;
			v.goToRoot();
			H.last = H.root;
			mysuspend();
		} else { //najdeme miesto pre v
			w = H.last.nextneighbour();
			w.linknewson(v);
			H.reposition();			
			mysuspend();
		}
		H.v = null;
		
		++H.root.nnodes;
		// mysuspend();
		bubbleup(v);
	}

}
