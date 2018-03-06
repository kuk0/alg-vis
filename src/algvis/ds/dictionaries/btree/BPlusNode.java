package algvis.ds.dictionaries.btree;

import algvis.core.DataStructure;
import algvis.core.Node;

public class BPlusNode extends BNode {

    public BPlusNode(BNode v) {
        super(v);
        c = new BPlusNode[((BPlusTree) D).order + 5];
    }

    public BPlusNode(BPlusNode v) {
        super(v);
        c = new BPlusNode[((BPlusTree) D).order + 5];
    }

    public BPlusNode(DataStructure D, int keys, int x, int y) {
        super(D, keys, x, y);
        c = new BPlusNode[((BPlusTree) D).order + 5];
    }

    public BPlusNode(DataStructure D, int keys) {
        super(D, keys);
        c = new BPlusNode[((BPlusTree) D).order + 5];
    }

    public BPlusNode(BPlusNode u, BPlusNode v, BPlusNode w) {
        super(u, v, w);
    }

    @Override
    public BPlusNode newNode(DataStructure D, int key, int x, int y) {
        return new BPlusNode(D, key, x, y);
    }

    public BPlusNode newNode(BPlusNode u, BPlusNode v, BPlusNode w) {
        return new BPlusNode(u, v, w);
    }

    public BPlusNode(BPlusNode u, BPlusNode v) {
        this(u.D, Node.NOKEY, (u.x + v.x) / 2, (u.y + v.y) / 2);
        int n1 = u.numKeys, n2 = v.numKeys;
        numKeys = n1 + n2;
        for (int i = 0; i < n1; ++i) {
            keys[i] = u.keys[i];
        }
        for (int i = 0; i < n2; ++i) {
            keys[n1 + i] = v.keys[i];
        }
        n1 = u.numChildren;
        n2 = v.numChildren;
        numChildren = n1 + n2;
        for (int i = 0; i < n1; ++i) {
            c[i] = u.c[i];
        }
        for (int i = 0; i < n2; ++i) {
            c[n1 + i] = v.c[i];
        }
        for (int i = 0; i < numChildren; ++i) {
            c[i].parent = this;
        }
        width = _width();
    }

    @Override
    public BPlusNode split() {
        return _split(isLeaf()); // if the node is a leaf, 
        // copy the middle element upwards; otherwise move it
    }

    private BPlusNode _split(boolean keep) {
        final int K = keep ? 0 : 1;
        final int k = numKeys, ku = numKeys / 2;
        final BPlusNode u = newNode(D, keys[0], tox, toy),
            v = newNode(D, keys[ku], tox, toy),
            w = newNode(D, keys[k - 1], tox, toy);
        for (int i = 1; i < ku; ++i) {
            u.addLeaf(keys[i]);
        }
        for (int i = ku + K; i < k - 1; ++i) {
            w.addLeaf(keys[i]);
        }
        if (isLeaf()) {
            u.numChildren = w.numChildren = 0;
        } else {
            u.numChildren = (numChildren + 1) / 2;
            w.numChildren = numChildren / 2;
            for (int i = 0; i < u.numChildren; ++i) {
                u.c[i] = c[i];
                u.c[i].parent = u;
            }
            for (int i = 0; i < w.numChildren; ++i) {
                w.c[i] = c[u.numChildren + i];
                w.c[i].parent = w;
            }
        }
        u.parent = w.parent = v;
        v.numChildren = 2;
        v.parent = parent;
        v.c[0] = u;
        v.c[1] = w;
        u.width = u._width();
        w.width = w._width();
        u.x = u.tox = tox - u.width / 2 - Node.RADIUS;
        w.x = w.tox = tox + w.width / 2 + Node.RADIUS;
        return v;
    }

    @Override
    public BPlusNode del(int k) {
        return (BPlusNode) super.del(k);
    }

    @Override
    public BPlusNode delMax() {
        return (BPlusNode) super.delMax();
    }

    @Override
    public BPlusNode delMin() {
        return (BPlusNode) super.delMin();
    }

}
