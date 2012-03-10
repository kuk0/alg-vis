package algvis.daryheap;

import algvis.core.Node;


public class DaryHeapDelete extends DaryHeapAlg {
	public DaryHeapDelete(DaryHeap H) {
		super(H);
		setHeader("deletion");
	}

	@Override
	public void run() {
		if (H.root == null) {
			addStep("heapempty");
			H.last = null;
			return;
		}
		if (H.root.numChildren == 0) {
			H.v = H.root;
			H.root = null;
			//--H.n;
			H.v.goDown();
			mysuspend();
			return;
		}
		
		H.v = new DaryHeapNode(H.last);
		H.v2 = new DaryHeapNode(H.root);
		H.last.key = Node.NOKEY;
		H.root.key = Node.NOKEY;
		H.v.goToRoot();
		H.v2.goTo(H.last);
		H.v2.mark();
		mysuspend();
		H.last.key = H.v2.key;
		H.root.key = H.v.key;
		H.last.setColor(H.v2.getColor());
		H.root.setColor(H.v.getColor());
		H.v = null;
		H.v2 = null;
		
		H.v = H.last;
		H.last = H.last.prevneighbour();
		H.v.goDown();
		H.v.getParent().c[H.v.nson - 1] = null;
		H.v.getParent().numChildren--;
		H.root.mark();
		H.reposition();
		
		mysuspend();
		H.v = null;
		if (H.minHeap) {
			addStep("mindheapbubbledown");
		} else {
			addStep("maxdheapbubbledown");
		}
		// mysuspend();
		//bubble bubble bubble
		v = H.root;
		H.root.unmark();
		bubbledown(v);
	}
}

