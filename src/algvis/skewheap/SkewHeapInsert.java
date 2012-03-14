package algvis.skewheap;

public class SkewHeapInsert extends SkewHeapAlg {
	int K;
	int i;

	public SkewHeapInsert(SkewHeap H, int i, int x) {
		super(H);
		this.i = i;
		H.root[0] = new SkewHeapNode(H, K = x);
		setHeader("insertion");
	}
	
	@Override
	public void run() {

		H.reposition();

		if (H.root[i] == null) {
			H.root[i] = H.root[0];
			H.root[0] = null;
			if (H.root[i] != null) {
				addStep("newroot");
				H.root[i].highlightTree();
			}
			H.reposition();
			// heap #1 is empty; done;
			return;
		}

		if (H.root[0] == null) {
			// heap #2 is empty; done;
			return;
		}

		H.active = i;
		H.root[0].highlightTree();
		H.reposition();

		mysuspend();
		meld(i);
	}
}
