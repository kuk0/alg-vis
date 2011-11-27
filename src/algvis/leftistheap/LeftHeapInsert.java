package algvis.leftistheap;

import algvis.bst.BSTNode;
import algvis.core.Colors;
import algvis.core.VisPanel;

public class LeftHeapInsert extends LeftHeapAlg{
	int K;
	int i; //halda cislo i  
	LeftHeap H;
	LeftHeapNode v;

	public LeftHeapInsert(VisPanel M) {
		super(M);
	}
	
	public LeftHeapInsert(LeftHeap H, int i, int x) {
		super(H.M);
		this.i = i;
		H.v = v = new LeftHeapNode(H, K = x);
		this.H = H;
		setHeader("insertion"); 
	}

	@Override
	public void run() {
		//treba doplnit spravny text	
		if (H.root[i] == null) {
			H.root[i] = v;			
			v.goToRoot();
			setText("newroot");
		} else {
			BSTNode w = H.root[i];
			v.goAboveRoot();
			setText("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key < K) {
					if (w.right == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(w.right);
					}
					setText("bstinsertright", K, w.key);
					mysuspend();
					v.noArrow();
					if (w.right != null) {
						w = w.right;
					} else {
						w.linkright(v);
						break;
					}					
				} else {
					if (w.left == null) {
						v.pointInDir(135);
					} else {
						v.pointAbove(w.left);
					}
					setText("bstinsertleft", K, w.key);
					mysuspend();
					v.noArrow();
					
					if (w.parent != null){
					  ((LeftHeapNode) w).linkup(v);
					  break;
					} else {
						H.root[i] = v;
						H.root[i].right = ((LeftHeapNode) w);
						w.parent = H.root[i];
						break;
						
					}
				}
				v.goAbove(w);
				mysuspend();
			}
			
			//dorobit iba premiestnenie vrcholu
			H.reposition();
			
			//uprava rankov,, maju sa upravovat az po koren alebo iba do vtedy, kym sa zmeni rank?
			setText("leftrankupdate");
			mysuspend();
			w = v;
			while (v.parent != null){				
				v = ((LeftHeapNode) v.parent);
				v.mark();
				if (v.left != null) {
					v.rank = Math.min(((LeftHeapNode) v.left).rank, ((LeftHeapNode) v.right).rank) + 1;
				}
				//mysuspend();
				v.unmark();
			}
			
			setText("done");
			mysuspend();
			//vymienanie s bratmi podla ranku			
			while (w != null){								
				if ((w.left != null) && (w.right != null)){
					w.left.mark();
					w.right.mark();
					if (((LeftHeapNode) w.left).rank < ((LeftHeapNode) w.right).rank) {
						setText("leftranksonch");
						mysuspend();
						BSTNode tmp = w.left;
						w.left = w.right;
						w.right = tmp;					
					}else{
						setText("leftranksonok");
						mysuspend();
					}				
					w.left.unmark();
					w.right.unmark();
					H.reposition();		//dorobit iba premiestnenie bratov
				}
				if (( w.left == null) && (w.right != null)){
					w.right.mark();
					setText("leftranksonch");
					mysuspend();
					w.left = w.right;
					w.right = null;					
					w.left.unmark();
					H.reposition();		//dorobit iba premiestnenie bratov
				}																
				w = ((LeftHeapNode) w.parent);								
			}
		}
		
		H.reposition();
		mysuspend();
		setText("done");
		v.bgColor(Colors.NORMAL);
		H.v = null; 

	}	
	
}
