package algvis.core;

abstract public class PriorityQueue extends DataStructure {
	public static String adtName = "pq";
	public boolean minHeap = false;

	public PriorityQueue(VisPanel M, String dsName) {
		super(M, dsName);
	}

	@Override
	abstract public void insert(int x);

	abstract public void delete();
	
	abstract public void decreaseKey(Node v, int delta);
}
