package algvis.intervaltree;

import algvis.core.Algorithm;
import algvis.core.Node;
import algvis.intervaltree.IntervalTrees.mimasuType;

class IntervalAlg extends Algorithm{
	IntervalTree T;
	IntervalNode v;
	
	IntervalAlg(IntervalTree T) {
		super(panel, d);
		this.T = T;
	}
	
	void adjustValues(IntervalNode w){
		while (w != null){
			w.mark();
			if ((w.getRight() != null) && (w.getLeft() != null)){
				if (T.minTree == mimasuType.MIN){
					if (w.getRight().getKey() == Node.NOKEY) {
						w.setKey(w.getLeft().getKey());
						addStep("intervalkeyempty", w.getLeft().getKey());
					} else {
						w.setKey(Math.min(w.getRight().getKey(), w.getLeft().getKey()));
						addStep("intervalmin", w.getRight().getKey(), w.getLeft().getKey());
					}
				} else if (T.minTree == mimasuType.MAX){
					w.setKey(Math.max(w.getRight().getKey(), w.getLeft().getKey()));
					if (w.getRight().getKey() != Node.NOKEY) {
						addStep("intervalmax", w.getRight().getKey(), w.getLeft().getKey());
					} else {
						addStep("intervalkeyempty", w.getLeft().getKey());
					}
				}else if (T.minTree == mimasuType.SUM){
					if (w.getRight().getKey() != Node.NOKEY) {
						w.setKey(w.getRight().getKey() + w.getLeft().getKey());
						addStep("intervalsum", w.getRight().getKey(), w.getLeft().getKey());
					} else {
						w.setKey(w.getLeft().getKey());
						addStep("intervalkeyempty", w.getLeft().getKey());
					}
				}
				w.setInterval(w.getLeft().b, w.getRight().e);
				//System.out.println(w.getKey() + " " + w.b + " " + w.e);
				pause();
				//System.out.println(w.b + " " + w.e);
			}
			w.unmark();
			w = w.getParent();
		}
	}

}
