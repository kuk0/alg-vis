package algvis.heap;

import java.awt.Color;

import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.PriorityQueue;

public class HeapNode extends BSTNode {
	HeapNode left, right, parent;
	Color color = Color.yellow;
	int height = 1;

	public HeapNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		bgKeyColor();
	}

	public HeapNode(DataStructure D, int key) {
		super(D, key);
		bgKeyColor();
	}

	public HeapNode(HeapNode v) {
		this(v.D, v.key, v.x, v.y);
	}

	@Override
	public HeapNode getLeft() {
		return (HeapNode) super.getLeft();
	}

	@Override
	public HeapNode getRight() {
		return (HeapNode) super.getRight();
	}

	@Override
	public HeapNode getParent() {
		return (HeapNode) super.getParent();
	}

	/**
	 * v.prec(w) iff v precedes w in the heap order, i.e., should be higher in
	 * the heap v precedes w if v.key < w.key when we have a min heap, but v
	 * precedes w if v.key > w.key when we have a max heap
	 */
	public boolean prec(Node v) {
		if (((PriorityQueue) D).minHeap) {
			return this.key < v.key;
		} else {
			return this.key > v.key;
		}
	}

	/**
	 * Precedes or equals (see prec).
	 */
	public boolean preceq(Node v) {
		if (((PriorityQueue) D).minHeap) {
			return this.key <= v.key;
		} else {
			return this.key >= v.key;
		}
	}
}
