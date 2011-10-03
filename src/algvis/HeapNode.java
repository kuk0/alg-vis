package algvis;

import java.awt.Color;

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
		setState(Node.UP);
	}

	public HeapNode(HeapNode v) {
		this(v.D, v.key, v.x, v.y);
	}

	public boolean less(Node v) {
		if (((PriorityQueue) D).minHeap) {
			return this.key < v.key;
		} else {
			return this.key > v.key;
		}
	}

	public boolean leq(Node v) {
		if (((PriorityQueue) D).minHeap) {
			return this.key <= v.key;
		} else {
			return this.key >= v.key;
		}
	}
}
