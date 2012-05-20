package algvis.intervaltree;

public class IntervalFindMin extends IntervalAlg{
	int i,j;
	IntervalNode maxi;
	static final int ninf = -2147483647;

	public IntervalFindMin(IntervalTree T, int i, int j) {
		super(T);
		this.T = T;
		setHeader("findmax", i, j);
		this.i = i;
		this.j = j;
		System.out.println(i + " " + j);
		System.out.println(T.root.b + " " + T.root.e);
		maxi = new IntervalNode(T, ninf);
		if (i>j){
			int tmp = j;
			j = i;
			i = tmp;
		}
	}
	
	
	public void run(){
		// kazdy vrchol ma zapamatany interval, ktory reprezentuje (je to b=1 po e=2^k)
		if (j > T.numLeafs){
			j = T.numLeafs;
		}

		if (T.root != null){
			find(T.root,i,j);;
			maxi.mark();
			addStep("maximum", maxi.key);
		} else {
			//addStep(); //strom je prazdny/zly interval
		}
	}
	
	public void find(IntervalNode w, int b, int e){
		w.mark();
		if((w.b > e) || (w.e < b)){
			addStep("intervalout", i, j); //mimo intervalu
			mysuspend();
			w.unmark();
			return;
		}
		
		if ((w.b >= b) && (w.e <= e)){
			if(w.key > maxi.key){
				maxi = w;
			}
			addStep("intervalin", i, j); //dnu intervalu
			mysuspend();
			w.unmark();
			return;
		}
		
		if((w.b <= b) || (w.e >= e)){
			addStep("intervalpart", i, j); // neprazdny prienik
			mysuspend();
			find(w.getLeft(), b, e);	
			find(w.getRight(), b, e);
		}
		w.unmark();
	}	
}
