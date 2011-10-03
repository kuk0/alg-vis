package algvis;

public class DataStructures {
	static final int N = 14;
	static final Class[] PANEL = { BSTPanel.class, RotPanel.class,
			AVLPanel.class, BPanel.class, RBPanel.class, AAPanel.class,
			TreapPanel.class, SkipListPanel.class, GBPanel.class,
			SplayPanel.class, HeapPanel.class, BinHeapPanel.class,
			LazyBinHeapPanel.class, FibHeapPanel.class };
	static final Class[] DS = { BST.class, Rotations.class, AVL.class,
			BTree.class, RB.class, AA.class, Treap.class, SkipList.class,
			GBTree.class, Splay.class, Heap.class, BinomialHeap.class,
			LazyBinomialHeap.class, FibonacciHeap.class };
	static final String[] TYPE = { "dict", "dict", "dict", "dict", "dict",
			"dict", "dict", "dict", "dict", "dict", "pq", "pq", "pq", "pq" };
	static final String[] NAME = { "bst", "rotations", "avltree", "btree",
			"redblack", "aatree", "treap", "skiplist", "scapegoat",
			"splaytree", "heap", "binheap", "lazybinheap", "fibheap" };
}
