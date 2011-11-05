package algvis.leftistheap;

import java.awt.Color;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.MeldablePQ;

public class LeftHeapNode extends BSTNode {
	LeftHeapNode left, right, parent;
	Color color = Color.yellow;
	int height = 1;
	
	public LeftHeapNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		bgKeyColor();		
	}
	
	public LeftHeapNode(DataStructure D, int key) {
		super(D, key);
		bgKeyColor();
		setState(Node.UP);
	}
	
	public LeftHeapNode(LeftHeapNode v) {
		this(v.D, v.key, v.x, v.y);
	}
	
	//TOTO MOZE NEFUNGOVAT!
	/**
	 * v.prec(w) iff v precedes w in the heap order, i.e., should be higher in the heap
	 * v precedes w if v.key < w.key when we have a min heap, but
	 * v precedes w if v.key > w.key when we have a max heap
	 */
	public boolean prec(Node v) {
		if (((MeldablePQ) D).minHeap) {
			return this.key < v.key;
		} else {
			return this.key > v.key;
		}
	}

	/**
	 * Precedes or equals (see prec).
	 */
	public boolean preceq(Node v) {
		if (((MeldablePQ) D).minHeap) {
			return this.key <= v.key;
		} else {
			return this.key >= v.key;
		}
	}	
}
