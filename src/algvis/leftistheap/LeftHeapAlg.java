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
		/*
		 * Bude to konecny cyklus a budeme prechadzat postupne vsetkymi vrcholmi root[i] haldy
		 * Vsetko to staci spravit raz.
		 * Kazdym prechodom cyklu sa o jeden level nizsie posunie jedna halda.
		 */
		
		/*
		 * Treba upravit komentare, 
		 * zistit, kedy sa ma posuvat root[0] dole,
		 * upravit miesta mysuspend(),
		 * nechat zmiznut hrany,
		 * na konci root[0] = null.
		 */
		
		BSTNode w = H.root[i];
		while (w != null){	//asi by bolo lepsie kym H.root[0] != null
			H.root[0].mark();
			w.mark();
			H.reposition();  //<<-----
			int K = H.root[0].key;
			setText("bstinsertstart");
			//mysuspend();
				if (w.key < K) {	//posuvame sa dole a nic nemenime
					setText("leftinsertright", K, w.key);
					H.root[0].repos(H.root[0].x, H.root[0].y + H.yspan + 2 * H.radius);
					mysuspend();
					w.unmark();
					H.root[0].unmark();
					if (w.right != null) { //ukoncime kolo cyklu
						w = w.right;
						H.root[0].repos(H.root[0].x, H.root[0].y + H.yspan + 2 * H.radius);
					} else {	//povieme, ze nie je co riesit a napojime v vpravo na w
						w.linkright(H.root[0]);
						H.root[0] = null;
						break;
					}
				} else {	//vymenime w a root[0], napojime root[0] ako praveho syna. treba upravit rank a povymienat synov??
					//text bla bla;
					mysuspend();
					
					H.root[0].repos(H.root[0].x, H.root[0].y + H.yspan + 2 * H.radius);
					w.unmark();
					H.root[0].unmark();

					BSTNode tmp1;
					BSTNode tmp2;
					tmp1 = w.parent;
					tmp2 = (BSTNode) H.root[0];
					
					if (w.parent != null){
						//w.parent.right = null;
						H.root[0] = (LeftHeapNode) w;
						H.root[0].parent = null;
						tmp1.right = tmp2;
						tmp2.parent = tmp1;
						w = tmp2;
						
					}else{
						tmp1 = H.root[0];
						H.root[0] = (LeftHeapNode) w;
						H.root[i] = (LeftHeapNode) tmp1;
						w = H.root[i];
					}
					H.reposition();
					mysuspend();
					setText("leftinsertup", K, w.key);
					if (w.right == null){
						w.linkright(H.root[0]);
						H.root[0] = null;
						break;
					}
					
					w = w.right;
					
					mysuspend();
				
				//H.v.goAbove(w);
				//mysuspend();
				}
		}
		
			
			//dorobit iba premiestnenie vrcholu
			H.reposition();
				
			//uprava rankov
			setText("leftrankupdate");
			mysuspend();		
			
			//najdeme si najpravejsieho synacika, kedze w sme si nechali ako nulllove musime ho znovu najst,
			//neskor asi cely while cyklus prerobim na inu podmienku (root[0] != null) 
			LeftHeapNode ther = H.root[i];
			while (ther.right != null){
				ther = (LeftHeapNode) ther.right;
			}
			
			LeftHeapNode tmp = ther;

			while (tmp != null){
				if ((tmp.left != null) && (tmp.right != null)) {
					tmp.rank = Math.min(((LeftHeapNode) tmp.left).rank, ((LeftHeapNode) tmp.right).rank) + 1;
				}else{
					tmp.rank = 1;
				}

				if (tmp.parent != null){
					tmp = ((LeftHeapNode) tmp.parent);
				}else{
					break;
			    }
			
			
			}
			setText("done");
			mysuspend();
			//vymienanie s bratmi podla ranku			
			tmp = (LeftHeapNode) ther;
			
			while (tmp != null){								
				if ((tmp.left != null) && (tmp.right != null)){
					tmp.left.mark();
					tmp.right.mark();
					if (((LeftHeapNode) tmp.left).rank < ((LeftHeapNode) tmp.right).rank) {
						setText("leftranksonch");
						mysuspend();
						BSTNode tmp3 = tmp.left;
						tmp.left = tmp.right;
						tmp.right = tmp3;					
					}else{
						setText("leftranksonok");
						mysuspend();
					}				
					tmp.left.unmark();
					tmp.right.unmark();
					H.reposition();		//dorobit iba premiestnenie bratov
				}
				if (( tmp.left == null) && (tmp.right != null)){
					tmp.right.mark();
					setText("leftranksonch");
					mysuspend();
					tmp.left = tmp.right;
					tmp.right = null;
					tmp.left.unmark();
					H.reposition();		//dorobit iba premiestnenie bratov
				}
				tmp = ((LeftHeapNode) tmp.parent);
			}

		H.reposition();
		mysuspend();
		setText("done");
		H.reposition();
		setText("done");
	}

}
