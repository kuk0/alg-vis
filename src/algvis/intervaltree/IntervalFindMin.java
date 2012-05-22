package algvis.intervaltree;

import algvis.core.Node;
import algvis.intervaltree.IntervalNode.focusType;

public class IntervalFindMin extends IntervalAlg{
	int i,j;
	IntervalNode maxi;
	static final int ninf = -2147483647;
	static final int pinf = 2147483647;

	public IntervalFindMin(IntervalTree T, int i, int j) {
		super(T);
		this.T = T;
		maxi = new IntervalNode(T, ninf);
		if (i>j){
			int tmp = j;
			j = i;
			i = tmp;
		}
		if(i < 1 || i > T.numLeafs){
			i = 1;			
		}
		if (j > T.numLeafs || j < 1){
			j = T.numLeafs;
		}
		this.i = i;
		this.j = j;
		T.markColor(T.root,i,j);
		setHeader("findmax", i, j);
		System.out.println(i + " " + j);
		System.out.println(T.root.b + " " + T.root.e);
	}
	
	
	public void run(){
		// kazdy vrchol ma zapamatany interval, ktory reprezentuje (je to b-e+1=2^k

		if (T.root != null){
			//addNote("intervalfind"); //vysvetlenie - bfs, porovnavanie intervalov - 3 pripady
			find(T.root,i,j);
			mysuspend();
			addStep("maximum", maxi.key);
			T.unfocus(T.root);
			maxi.mark();
			T.markColor(T.root, i, j);
			mysuspend();
			T.unfocus(T.root);
			addNote("done");
		} else {
			//addStep(); //strom je prazdny/zly interval
		}
	}
	
	public void find(IntervalNode w, int b, int e){
		
		w.mark();
		//w.markSubtree = true;
		
		if((w.b > e) || (w.e < b)){
			if (w.key != Node.NOKEY){
				addStep("intervalout", i, j, w.key, w.b, w.e); //mimo intervalu
			} else {
				addStep("intervalempty", w.b, w.e); //prazdny vrchol
			}
			w.focused = focusType.TOUT;
			mysuspend();
			w.unmark();
			w.focused = focusType.FALSE;
			return;
		}
		
		if ((w.b >= b) && (w.e <= e)){
			if(w.key > maxi.key){
				maxi = w;
			}
			addStep("intervalin", i, j, w.key, w.b, w.e); //dnu intervalu
			w.focused = focusType.TIN;
			mysuspend();
			//w.unmark();
			//w.unfocus();
			return;
		}
		
		if((w.b <= b) || (w.e >= e)){
			addStep("intervalpart", i, j, w.key, w.b, w.e); // neprazdny prienik
			w.focused = focusType.TOUT;
			mysuspend();
			w.focused = focusType.TWAIT;
			find(w.getLeft(), b, e);
			find(w.getRight(), b, e);
		}
		w.unmark();
		w.focused = focusType.FALSE;
	}	
}
