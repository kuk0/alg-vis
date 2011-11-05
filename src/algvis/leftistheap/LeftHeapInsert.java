package algvis.leftistheap;

import algvis.bst.BSTNode;
import algvis.core.VisPanel;

public class LeftHeapInsert extends LeftHeapAlg{

	public LeftHeapInsert(VisPanel M) {
		super(M);
	}
	
	public LeftHeapInsert(LeftHeap H, int x) {
		super(H.M);
		H.v = v = new BSTNode(H, x);
		setHeader("insertion"); //??

	}
	@Override
	public void run() {
		if (H.n == 1000) {
			setText("heapfull");
			H.v = null;
			return;
		}
		BSTNode w;

		// link
		++H.n;
		int n = H.n, k = 1 << 10;
		if (n == 1) {
			H.root = w = v;
			v.goToRoot();
			mysuspend();
		} 
		H.v = null;


		// mysuspend();

		setText("done");
	}	
	

}
