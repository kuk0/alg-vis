package algvis.core;

import java.lang.reflect.Constructor;

import algvis.aatree.AAPanel;
import algvis.avltree.AVLPanel;
import algvis.binomialheap.BinHeapPanel;
import algvis.bst.BSTPanel;
import algvis.btree.BPanel;
import algvis.fibonacciheap.FibHeapPanel;
import algvis.heap.HeapPanel;
import algvis.internationalization.Languages;
import algvis.lazybinomialheap.LazyBinHeapPanel;
import algvis.redblacktree.RBPanel;
import algvis.rotations.RotPanel;
import algvis.scapegoattree.GBPanel;
import algvis.skiplist.SkipListPanel;
import algvis.splaytree.SplayPanel;
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
	@SuppressWarnings("rawtypes")
	static final Class[] PANEL = { BSTPanel.class, RotPanel.class,
			AVLPanel.class, BPanel.class, RBPanel.class, AAPanel.class,
			TreapPanel.class, SkipListPanel.class, GBPanel.class,
			SplayPanel.class, HeapPanel.class, BinHeapPanel.class,
			LazyBinHeapPanel.class, FibHeapPanel.class };
	static final int N = PANEL.length;

	private static boolean check_range(int i) {
    	if (i < 0 || i >= N) {
    		System.out.println ("DataStructures - index out of range.");
    		return false;
    	}
    	return true;
	}

	@SuppressWarnings("unchecked")
	public static Class<? extends DataStructure> DS(int i) {
		if (!check_range(i)) return null;
		try {
			return (Class<? extends DataStructure>)(PANEL[i].getDeclaredField("DS").get(null));
		} catch (Exception e) {
			return null;
		}
	}
	
    public static String getName(int i) {
    	if (!check_range(i)) return "";
    	String r = "";
		try {
			r = (String)(DS(i).getDeclaredField("dsName").get(null));
		} catch (Exception e) {
			System.out.println ("DataStructures is unable to get field dsName - name of data structure: " + i);
		}
		return r;
    }

	@SuppressWarnings("unchecked")
	public static String getADT(int i) {
    	if (!check_range(i)) return "";
		try {
			// find the superclass which has the adtName field set 
			Class<? extends DataStructure> c = DS(i);
			while (true) {
				try {
					// DEBUG: System.out.println(c);
					c.getDeclaredField("adtName");
					break;
				} catch (NoSuchFieldException e) {
					c = (Class<? extends DataStructure>) c.getSuperclass();
				}
			}
			return (String)(c.getDeclaredField("adtName").get(null));
		} catch (Exception e) {
			System.out.println ("DataStructures is unable to get field adtName - abstract data type of data structure: " + i);
			return "";
		}
    }
    

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static VisPanel getPanel(int i, Languages L) {
    	if (!check_range(i)) return null;
    	try {
    		Constructor ct = DataStructures.PANEL[i].getConstructor(Languages.class);
    		return (VisPanel) ct.newInstance(L);
    	} catch (Exception e) {
    		System.out.println ("DataStructures is unable to get panel: " + i);
    		return null;
    	}
    }
}
