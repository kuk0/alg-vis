package algvis.intervaltree;

import algvis.core.Algorithm;
import algvis.core.Node;

public class IntervalAlg extends Algorithm{
	IntervalTree T;
	IntervalNode v;
	
	public IntervalAlg(IntervalTree T) {
		super(T);
		this.T = T;
	}
	
	public void adjustValues(IntervalNode w){
		while (w != null){
			w.mark();
			if ((w.getRight() != null) && (w.getLeft() != null)){
				if (T.minTree){
					if (w.getRight().key == Node.NOKEY) {
						w.key = w.getLeft().key;
						addStep("intervalkeyempty", w.getLeft().key);
					} else {
						w.key = Math.min(w.getRight().key, w.getLeft().key);
						addStep("intervalmin", w.getRight().key, w.getLeft().key);
					}
				} else {
					w.key = Math.max(w.getRight().key, w.getLeft().key);
					if (w.getRight().key != Node.NOKEY) {
						addStep("intervalmax", w.getRight().key, w.getLeft().key);
					} else {
						addStep("intervalkeyempty", w.getLeft().key);
					}
				}
				w.setInterval(w.getLeft().b, w.getRight().e);
				System.out.println(w.key + " " + w.b + " " + w.e);
				mysuspend();
				//System.out.println(w.b + " " + w.e);
			}
			w.unmark();
			w = w.getParent();
		}
	}

}
