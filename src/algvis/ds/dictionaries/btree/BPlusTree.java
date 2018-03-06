package algvis.ds.dictionaries.btree;

import algvis.ui.VisPanel;

public class BPlusTree extends BTree {
    public static String dsName = "bplustree";

    public BPlusTree(VisPanel M) {
        super(M);
    }

    @Override
    public String getName() {
        return "bplustree";
    }

    @Override
    public void insert(int x) {
        start(new BPlusInsert(this, x));
    }

    @Override
    public void find(int x) {
        start(new BPlusFind(this, x));
    }

    @Override
    public void delete(int x) {
        start(new BPlusDelete(this, x));
    }

    @Override
    public BPlusNode getRoot() {
        return (BPlusNode) root;
    }

}
