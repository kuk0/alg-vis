package algvis.core;

import algvis.aatree.AA;
import algvis.aatree.AAPanel;
import algvis.avltree.AVL;
import algvis.avltree.AVLPanel;
import algvis.binomialheap.BinHeapPanel;
import algvis.binomialheap.BinomialHeap;
import algvis.bst.BST;
import algvis.bst.BSTPanel;
import algvis.btree.BPanel;
import algvis.btree.BTree;
import algvis.fibonacciheap.FibHeapPanel;
import algvis.fibonacciheap.FibonacciHeap;
import algvis.heap.Heap;
import algvis.heap.HeapPanel;
import algvis.lazybinomialheap.LazyBinHeapPanel;
import algvis.lazybinomialheap.LazyBinomialHeap;
import algvis.redblacktree.RB;
import algvis.redblacktree.RBPanel;
import algvis.rotations.RotPanel;
import algvis.rotations.Rotations;
import algvis.scapegoattree.GBPanel;
import algvis.scapegoattree.GBTree;
import algvis.skiplist.SkipList;
import algvis.skiplist.SkipListPanel;
import algvis.splaytree.Splay;
import algvis.splaytree.SplayPanel;
import algvis.treap.Treap;
import algvis.treap.TreapPanel;

/**
 * The Class DataStructures.
 * Contains list of all visualized data structures.
 * This is the base for "Data Structures" menu.
 */
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
