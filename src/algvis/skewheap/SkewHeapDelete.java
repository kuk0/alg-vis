package algvis.skewheap;

public class SkewHeapDelete extends SkewHeapAlg {
	int i;

	public SkewHeapDelete(SkewHeap H, int i) {
		super(H);
		this.i = i;
		setHeader("deletion");
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

		mysuspend();

		SkewHeapNode tmp = H.root[i];
		H.root[i] = tmp.getLeft();
		H.root[0] = tmp.getRight();
		tmp = null;

		if (H.root[i] == null) {
			H.root[i] = H.root[0];
			H.root[0] = null;
			if (H.root[i] != null) {
				H.root[i].highlightTree();
				H.root[i].repos(H.root[i].x, H.root[i].y
						- (H.yspan + 2 * H.radius));
			}
			// heap #1 is empty; done;
			return;
		}
		H.root[i].setParent(null);

		if (H.root[0] == null) {
			H.root[i].repos(H.root[i].x, H.root[i].y - (H.yspan + 2 * H.radius));
			// heap #2 is empty; done;
			return;
		}
		H.root[0].setParent(null);

		H.root[i].repos(H.root[i].x, H.root[i].y - (H.yspan + 2 * H.radius));
		H.root[0].repos(H.root[0].x, H.root[0].y - (H.yspan + 2 * H.radius));

		mysuspend();
		meld(i);

	}

}
