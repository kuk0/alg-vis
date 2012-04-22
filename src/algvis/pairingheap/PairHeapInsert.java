package algvis.pairingheap;

public class PairHeapInsert extends PairHeapAlg{
	int K;
	int i;
	PairHeapNode w;
	
	public PairHeapInsert(PairingHeap H, int i, int x) {
		super(H);
		this.i = i;
		H.root[0] = new PairHeapNode(H, K = x);
		setHeader("insertion");
	}
	
	
	@Override
	public void run() {

		H.reposition();

		if (H.root[i] == null) {
			H.root[i] = H.root[0];
			if (H.root[i] != null) {
				addStep("newroot");
				H.root[i].highlightTree();
			}			
		} else {
			H.root[i].highlightTree();
			H.root[i].mark();
			//kedze je <cislo> viac/menej ako <cislo> tak to prilinkujeme k tomu
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
			H.root[i].unmark();
			link(i,0);
			H.reposition();
		}
		H.root[0] = null;
		H.reposition();
		addNote("done");
	}
}
