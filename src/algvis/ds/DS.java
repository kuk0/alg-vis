package algvis.ds;

import java.lang.reflect.Constructor;

import algvis.core.DataStructure;
import algvis.ds.dictionaries.aatree.AA;
import algvis.ds.dictionaries.aatree.AAPanel;
import algvis.ds.dictionaries.avltree.AVL;
import algvis.ds.dictionaries.avltree.AVLPanel;
import algvis.ds.dictionaries.bst.BST;
import algvis.ds.dictionaries.bst.BSTPanel;
import algvis.ds.dictionaries.btree.BPanel;
import algvis.ds.dictionaries.btree.BTree;
import algvis.ds.dictionaries.btree.a234Panel;
import algvis.ds.dictionaries.btree.a234Tree;
import algvis.ds.dictionaries.btree.a23Panel;
import algvis.ds.dictionaries.btree.a23Tree;
import algvis.ds.dictionaries.redblacktree.RB;
import algvis.ds.dictionaries.redblacktree.RBPanel;
import algvis.ds.dictionaries.scapegoattree.GBPanel;
import algvis.ds.dictionaries.scapegoattree.GBTree;
import algvis.ds.dictionaries.skiplist.SkipList;
import algvis.ds.dictionaries.skiplist.SkipListPanel;
import algvis.ds.dictionaries.splaytree.SplayPanel;
import algvis.ds.dictionaries.splaytree.SplayTree;
import algvis.ds.dictionaries.treap.Treap;
import algvis.ds.dictionaries.treap.TreapPanel;
import algvis.ds.dynamicarray.DynamicArray;
import algvis.ds.dynamicarray.DynamicArrayPanel;
import algvis.ds.intervaltree.IntervalPanel;
import algvis.ds.intervaltree.IntervalTree;
import algvis.ds.priorityqueues.binomialheap.BinHeapPanel;
import algvis.ds.priorityqueues.binomialheap.BinomialHeap;
import algvis.ds.priorityqueues.daryheap.DaryHeap;
import algvis.ds.priorityqueues.daryheap.DaryHeapPanel;
import algvis.ds.priorityqueues.fibonacciheap.FibHeapPanel;
import algvis.ds.priorityqueues.fibonacciheap.FibonacciHeap;
import algvis.ds.priorityqueues.heap.Heap;
import algvis.ds.priorityqueues.heap.HeapPanel;
import algvis.ds.priorityqueues.lazybinomialheap.LazyBinHeapPanel;
import algvis.ds.priorityqueues.lazybinomialheap.LazyBinomialHeap;
import algvis.ds.priorityqueues.leftistheap.LeftHeap;
import algvis.ds.priorityqueues.leftistheap.LeftHeapPanel;
import algvis.ds.priorityqueues.pairingheap.PairHeapPanel;
import algvis.ds.priorityqueues.pairingheap.PairingHeap;
import algvis.ds.priorityqueues.skewheap.SkewHeap;
import algvis.ds.priorityqueues.skewheap.SkewHeapPanel;
import algvis.ds.rotations.RotPanel;
import algvis.ds.rotations.Rotations;
import algvis.ds.suffixtree.SuffixTree;
import algvis.ds.suffixtree.SuffixTreePanel;
import algvis.ds.trie.Trie;
import algvis.ds.trie.TriePanel;
import algvis.ds.unionfind.UnionFind;
import algvis.ds.unionfind.UnionFindPanel;
import algvis.ui.VisPanel;

public enum DS {

    // Dictionaries
    BST(BST.class, BSTPanel.class), // BST
    ROTATION(Rotations.class, RotPanel.class), // rotations
    AVL_TREE(AVL.class, AVLPanel.class), // AVL-tree
    A23(a23Tree.class, a23Panel.class), // 2-3-tree
    A234(a234Tree.class, a234Panel.class), // 2-3-4-tree
    B_TREE(BTree.class, BPanel.class), // B-tree
    RB_TREE(RB.class, RBPanel.class), // red-black tree
    AA_TREE(AA.class, AAPanel.class), // AA-tree
    TREAP(Treap.class, TreapPanel.class), // treap
    SKIPLIST(SkipList.class, SkipListPanel.class), // skiplist
    GB_TREE(GBTree.class, GBPanel.class), // scapegoat tree
    SPLAY_TREE(SplayTree.class, SplayPanel.class), // splay tree

    // Heaps
    HEAP(Heap.class, HeapPanel.class), // heap
    DARY_HEAP(DaryHeap.class, DaryHeapPanel.class), // d-ary heap

    // Meldable heaps
    LEFTIST_HEAP(LeftHeap.class, LeftHeapPanel.class), // leftist heap
    SKEW_HEAP(SkewHeap.class, SkewHeapPanel.class), // skew heap
    PAIRING_HEAP(PairingHeap.class, PairHeapPanel.class), // pairing heap
    BIN_HEAP(BinomialHeap.class, BinHeapPanel.class), // binomial heap
    LAZY_BIN_HEAP(LazyBinomialHeap.class, LazyBinHeapPanel.class), // lazy binomial heap
    FIB_HEAP(FibonacciHeap.class, FibHeapPanel.class), // Fibonacci heap

    // Union-find
    UNION_FIND(UnionFind.class, UnionFindPanel.class),

    // Interval tree
    INTERVAL_TREE(IntervalTree.class, IntervalPanel.class),

    // Stringology
    TRIE(Trie.class, TriePanel.class), // trie
    SUFFIX_TREE(SuffixTree.class, SuffixTreePanel.class), // suffix tree

    // Amortized data structures
    DYN_ARRAY(DynamicArray.class, DynamicArrayPanel.class);

    private Class<? extends DataStructure> s;
    private Class<? extends VisPanel> p;

    private DS(Class<? extends DataStructure> s, Class<? extends VisPanel> p) {
        this.s = s;
        this.p = p;
    }

    public String getName() {
        try {
            return (String) s.getDeclaredField("dsName").get(null);
        } catch (final Exception e) {
            System.err.println("Unable to get dsName for: " + s);
            return null;
        }
    }

    public VisPanel createPanel() {
        try {
            final Constructor<? extends VisPanel> ct = p.getConstructor();
            return (VisPanel) ct.newInstance();
        } catch (final Exception e) {
            System.out.println("Unable to construct panel: " + p);
            e.printStackTrace();
            // System.out.println(((InvocationTargetException)e).getTargetException().toString());
            return null;
        }
    }

}
