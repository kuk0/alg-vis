package algvis.intervaltree;

public class IntervalInsert extends IntervalAlg{
	int K;

	public IntervalInsert(IntervalTree T, int x) {
		super(T);
		this.T = T;
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
		}
		T.reposition();
		mysuspend();
		
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
		
		mysuspend();
		
		/*
		uprav strom na min/max z intervalu;
		*/
		//w = w.getParent();
		//toto prerobit na iba prechod od prave pridaneho vrcholu po koren
		while (w != null){
			if ((w.getRight() != null) && (w.getLeft() != null)){
				
				if (T.minTree){
					if (w.getRight().key < 1) {
						w.key = w.getLeft().key;
					} else {
						w.key = Math.min(w.getRight().key, w.getLeft().key);
					}
				} else {
					w.key = Math.max(w.getRight().key, w.getLeft().key);
				}
				//if (w.getRight().key < 1){
					//w.setInterval(w.getLeft().b, T.numLeafs);
				//} else {
					w.setInterval(w.getLeft().b, w.getRight().e);
				//}
				System.out.println(w.key + " " + w.b + " " + w.e);
				mysuspend();
				//System.out.println(w.b + " " + w.e);
			}
			w = w.getParent();
		}
		//uprav(w);
		System.out.println("hotovo");
		mysuspend();
	}
	
	public void uprav(IntervalNode w){
		if (w.getLeft() != null){
			uprav(w.getLeft());
		}
		
		if (w.getRight() != null){
			uprav(w.getRight());
		}

		/*
		if ((w.getRight() != null) && (w.getLeft() != null)){
			w.key = w.getRight().key + w.getLeft().key;
		}
		*/

		if ((w.getRight() != null) && (w.getLeft() != null)){
			if (T.minTree){
				w.key = Math.min(w.getRight().key, w.getLeft().key);
			} else {
				w.key = Math.max(w.getRight().key, w.getLeft().key);
			}
		}
		
	}
}
