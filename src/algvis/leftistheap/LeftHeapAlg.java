package algvis.leftistheap;

import algvis.bst.BSTNode;
import algvis.core.Algorithm;
import algvis.core.Colors;
import algvis.core.VisPanel;

public class LeftHeapAlg extends Algorithm{
	LeftHeap H;
	//LeftHeapNodeNode v;

	
	public LeftHeapAlg(VisPanel M) {
		super(M);
	}
	
	
	public LeftHeapAlg(LeftHeap H) {
		super(H.M);
		this.H = H;
	}
	
	public void meld(int i) {
		//root[0] primelduvavame k root[i]
		//i = 1		
		//misung medyi v a w
		
		LeftHeapNode v = H.root[0];
		H.root[0] = (LeftHeapNode) v.right;
		v.right = null;
		
		while (v != null){			
			v.mark();
		
			int K = H.root[0].key;
			
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
		
		
		H.reposition();
		mysuspend();
		setText("done");
		v.bgColor(Colors.NORMAL);
		H.v = null;
		
		v = (LeftHeapNode) v.right;
		v.parent = null;
		H.root[0] = (LeftHeapNode) v.right;
		v.right = null;
						
		}
		
		
		
		
		
		
		
	/*		while (true) {
			if (H.root[0] != null && v.size > H.root[0].size) {
				// pripojime vlavo
				BinHeapNode u = H.root[0];
				if (H.root[0].right == H.root[0]) {
					H.root[0] = null;
				} else {
					H.root[0] = H.root[0].right;
				}
				u.unlink();
				u.highlightTree(u);
				v.linkLeft(u);
				v.unmark();
				v = H.root[i] = u;
				v.mark();
			} else if (H.root[0] != null && v.size <= H.root[0].size
					&& (v.right == H.root[i] || H.root[0].size < v.right.size)) {
				// pripojime vpravo
				BinHeapNode u = H.root[0];
				if (H.root[0].right == H.root[0]) {
					H.root[0] = null;
				} else {
					H.root[0] = H.root[0].right;
				}
				u.unlink();
				u.highlightTree(u);
				v.linkRight(u);
			} else if (v.left.size == v.size && v.left != v
					&& (v.right == H.root[i] || v.size < v.right.size)) {
				// spojime 2 rovnakej velkosti
				BinHeapNode u = v.left;
				if (u.prec(v)) { // napojime v pod u
					v.unlink();
					u.linkChild(v);
					v.unmark();
					v = u;
					v.mark();
				} else { // napojime u pod v
					if (H.root[i] == u) {
						H.root[i] = v;
					}
					u.unlink();
					v.linkChild(u);
				}
			} else if (v.right != H.root[i]) {
				// posunieme sa
				v.unmark();
				v = v.right;
				v.mark();
			} else {
				// koncime
				v.unmark();
				break;
			}
			H.reposition();
			mysuspend();
		} */
	}

}
