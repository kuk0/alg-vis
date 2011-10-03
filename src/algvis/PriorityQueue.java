package algvis;

abstract public class PriorityQueue extends DataStructure {
	boolean minHeap = false;

	public PriorityQueue(VisPanel M) {
		super(M);
	}

	@Override
	abstract public void insert(int x);

	abstract public void delete();
	
	abstract public void increaseKey(Node v, int delta);
}
