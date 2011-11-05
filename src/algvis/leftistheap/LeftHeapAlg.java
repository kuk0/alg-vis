package algvis.leftistheap;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.VisPanel;

public class LeftHeapAlg extends Algorithm{
	LeftHeap H;
	BSTNode v;

	
	public LeftHeapAlg(VisPanel M) {
		super(M);
	}
	
	
	public LeftHeapAlg(LeftHeap H) {
		super(H.M);
		this.H = H;
	}
	

}
