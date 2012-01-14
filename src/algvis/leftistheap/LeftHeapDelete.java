package algvis.leftistheap;

import algvis.core.VisPanel;


public class LeftHeapDelete extends LeftHeapAlg{
	//LeftHeap H;
	int i;

	public LeftHeapDelete(VisPanel M) {
		super(M);
	}
	
	public LeftHeapDelete(LeftHeap H, int i) {
		super(H);
		//this.H = H;
		this.i = i;		
		setHeader("deletion");		
	}
	
	@Override
	public void run() {
		if(H.root[i] == null){
			return;
		}

		//if () {
			setText("maximum", H.root[i].key);
		/*
		 * }else{
			setText("minimum", H.root[i].key);
			}
		*/
		mysuspend();
		
		LeftHeapNode tmp = H.root[i];
		//H.root[i] = null;
		H.root[i] = (LeftHeapNode) tmp.left;
		H.root[i].parent = null;
		H.root[0] = (LeftHeapNode) tmp.right;
		H.root[0].parent = null;
		//H.reposition();
		tmp = null;
		
		if (H.root[i] == null) {		
			H.root[i] = H.root[0];
			H.root[0] = null;
			if (H.root[i] != null) {
				H.root[i].highlightTree();
				H.root[i].repos(H.root[i].x, H.root[i].y - (H.yspan + 2*H.radius));
			}
			//H.reposition();
			// heap #1 is empty; done;
			return;
		}
				
		if (H.root[0] == null) {
			H.root[i].repos(H.root[i].x, H.root[i].y - (H.yspan + 2*H.radius));
			// heap #2 is empty; done;
			return;
		}
		
		H.root[i].repos(H.root[i].x, H.root[i].y - (H.yspan + 2*H.radius));
		H.root[0].repos(H.root[0].x, H.root[0].y - (H.yspan + 2*H.radius));
		
		
		//H.active = i;
		//H.root[0].highlightTree();

		mysuspend();
		//H.reposition();
		meld(i);

		//H.root[0] = null;

		
		
		
	}

}
