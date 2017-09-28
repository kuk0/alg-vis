package algvis.ds.dictionaries.aatree;

import algvis.core.Algorithm;
import algvis.ui.view.REL;

abstract public class AAAlg extends Algorithm {
    protected final AA T;
    protected final int K;

    public AAAlg(AA T, int x) {
        super(T.panel);
        this.T = T;
        K = x;
    }

	public void skew(AANode w, String txt) {
        if (w != null && w.leftPseudoNode()) {
            addStep(w, REL.TOP, txt);
            w.getLeft().setArc(w);
            pause();
            w.getLeft().noArc();
            T.rotate(w.getLeft());
            T.reposition();
        }
	}
	
	public void split(AANode w, String txt) {
		AANode r = w.getRight();
        addStep(w, REL.TOP, txt);
        r.setArc();
        pause();
        r.noArc();
        T.rotate(r);
        r.setLevel(r.getLevel() + 1);
        T.reposition();
	}
}
