package algvis.leftistheap;

public class LeftHeapMeld extends LeftHeapAlg {
	int i, j;

	public LeftHeapMeld(LeftHeap H, int i, int j) {
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
		if (j != 0){
			H.root[j] = null;
		}
		H.active = i;
		H.root[0].highlightTree();
		H.reposition();

		mysuspend();
		meld(i);
	}

}
