package algvis.ds.dictionaries.bst;

import algvis.core.Algorithm;

public abstract class BSTAlg extends Algorithm {
    protected BST T;
    protected int K;

    public BSTAlg(BST T, int K) {
        super(T.panel);
        this.T = T;
        this.K = K;
    }

}
