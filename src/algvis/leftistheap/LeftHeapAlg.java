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
		/*treba spravit, aby sa posuvali na spravne miesta, 
		 * aby sa vobec posuvali, 
		 * aby vobec bolo vidno vrchol, ktory sa prave vklada 
		 */
		
		LeftHeapNode v = H.root[0];
		v.rank = 1;

		//v je vrchol, ktory vkladame
		while (H.root[0] != null){	
			H.root[0] = (LeftHeapNode) v.right;
			v.right = null;
			
			v.mark();					
			H.reposition();  //<<-----
			BSTNode w = H.root[i];
			int K = v.key;
			v.goAbove(w); //v.goAboveRoot(); 
			setText("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key < K) {
					if (w.right == null) {
						v.pointInDir(45);
					} else {
						v.pointAbove(w.right);
					}
					setText("leftinsertright", K, w.key);
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
					setText("leftinsertup", K, w.key);
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
		H.v = null;  //??
		if (H.root[0] !=null ){
			v = H.root[0];	
			v.rank = 1;
		}
						
		}
		
		H.reposition();
		setText("done");
	}

}
