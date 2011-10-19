package algvis.core;

import java.lang.reflect.Constructor;

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
 * This class contains the list of all visualized data structures.
 * The menus with data structures are populated from this list.
 * Each data structure should have field dsName with its name
 * and some superclass should have field adtName with the name
 * of the abstract data type (key to resource bundle).
 * The data structure can then be found in "Data structures -> adtName -> dsName". 
 */
public class DataStructures {
	static final int N = 14;
	@SuppressWarnings("unchecked")
	static final Class[] PANEL = { BSTPanel.class, RotPanel.class,
			AVLPanel.class, BPanel.class, RBPanel.class, AAPanel.class,
			TreapPanel.class, SkipListPanel.class, GBPanel.class,
			SplayPanel.class, HeapPanel.class, BinHeapPanel.class,
			LazyBinHeapPanel.class, FibHeapPanel.class };
	@SuppressWarnings("unchecked")
	static final Class[] DS = { BST.class, Rotations.class, AVL.class,
			BTree.class, RB.class, AA.class, Treap.class, SkipList.class,
			GBTree.class, Splay.class, Heap.class, BinomialHeap.class,
			LazyBinomialHeap.class, FibonacciHeap.class };

	private static boolean check_range(int i) {
    	if (i < 0 || i >= N) {
    		System.out.println ("DataStructures - index out of range.");
    		return false;
    	}
    	return true;
	}

    public static String getName(int i) {
    	if (!check_range(i)) return "";
    	String r = "";
		try {
			r = (String)(DS[i].getDeclaredField("dsName").get(null));
		} catch (Exception e) {
			System.out.println ("Unable to get field dsName - name of data structure: " + DS[i]);
		}
		return r;
    }

    @SuppressWarnings("unchecked")
	public static String getADT(int i) {
    	if (!check_range(i)) return "";
    	String r = "";
		try {
			// find the superclass which has the adtName field set 
			Class c = DS[i];
			while (true) {
				try {
					// DEBUG: System.out.println(c);
					c.getDeclaredField("adtName");
					break;
				} catch (NoSuchFieldException e) {
					c = c.getSuperclass();
				}
			}
			r = (String)(c.getDeclaredField("adtName").get(null));
		} catch (Exception e) {
			System.out.println ("Unable to get field adtName - abstract data type of data structure: " + DS[i]);
		}
		return r;
    }
    
    @SuppressWarnings("unchecked")
	public static VisPanel getPanel(int i, AlgVis a) {
    	if (!check_range(i)) return null;
    	try {
    		Constructor ct = DataStructures.PANEL[i].getConstructor(AlgVis.class);
    		return (VisPanel) ct.newInstance(a);
    	} catch (Exception e) {
    		return null;
    	}
    }
}
