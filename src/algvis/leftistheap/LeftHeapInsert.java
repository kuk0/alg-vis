package algvis.leftistheap;

import algvis.core.VisPanel;

public class LeftHeapInsert extends LeftHeapAlg{
	int K;
	int i; //halda cislo i  
	//LeftHeap H;
	LeftHeapNode v;

	public LeftHeapInsert(VisPanel M) {
		super(M);
	}
	
	public LeftHeapInsert(LeftHeap H, int i, int x) {
		//super(H.M);
		super(H);
		this.i = i;
		H.v = v = new LeftHeapNode(H, K = x);
		//H.root[0] = new LeftHeapNode(H, K = x);
		//this.H = H;
		setHeader("insertion");
	}

	@Override
	public void run() {
		//treba doplnit spravny text	
			H.root[0] = v;
			H.reposition();

			/* toto je trochu surove, ale v reposition nemoze byt root[0].repos(sumx, root[0].toy);
			 * lebo potom pri meldovani stale root[0] vracia spat nahor.
			 */
			H.root[0].goTo(H.root[0].tox, 0);

			//mysuspend();
			H.v = null;
			v = null;
			

			if (H.root[i] == null) {
				H.root[i] = H.root[0];
				H.root[0] = null;
				if (H.root[i] != null) {
					setText("newroot");
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
