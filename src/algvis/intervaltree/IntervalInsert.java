package algvis.intervaltree;

public class IntervalInsert extends IntervalAlg{
	int K;

	public IntervalInsert(IntervalTree T, int x) {
		super(T);
		this.T = T;
		if (x > 333){
			x = x/3;
		}
		v = new IntervalNode(T, K = x);
		v.setInterval(T.numLeafs+1, T.numLeafs+1);
		//v.setColor(NodeColor.INSERT);
		setHeader("insert", K);
	}
	
	@Override
	public void run() {
		
		T.reposition();

		if (T.root == null) {
			T.root = v;
			v = null;
			if (T.root != null) {
				T.numLeafs++;
				addStep("newroot");
			}
			T.reposition();
			// heap #1 is empty; done;
			return;
		}

		int tmp = T.numLeafs;
		while (tmp % 2 == 0){
			tmp /= 2;
		}
		
		if (tmp == 1){
			T.extend();
			addStep("intervalextend");
			mysuspend();
		}
		T.reposition();
		//mysuspend();
		
		/*
		T.root.linkRight(v);
		T.reposition();
		*/
		/*
		pridaj na T.numLeafs + 1 novy prvok;
		*/
		
		IntervalNode w; 
		//T.numLeafs++;
		int n = T.numLeafs, k = 1 << 10;
		if (n == 0) {
			T.root = w = v;
			v.goToRoot();
			mysuspend();
		} else {
			while ((k & n) == 0) {
				k >>= 1;
			}
			//k >>= 1;
			w = T.root;
			while (k > 1) {
				w = ((n & k) == 0) ? w.getLeft() : w.getRight();
				k >>= 1;
			}
			if ((k & n) == 0) {
				w.linkLeft(v);
			} else {
				w.linkRight(v);
			}
			T.reposition();
		}
		
		T.numLeafs++;
		addNote("intervalinsert");
		v.mark();
		mysuspend();
		v.unmark();
		/*
		uprav strom na min/max z intervalu;
		*/
		//w = w.getParent();
		//toto prerobit na iba prechod od prave pridaneho vrcholu po koren
		adjustValues(w);
		addNote("done");
	}
}
