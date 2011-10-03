package algvis;

abstract public class MeldablePQ extends DataStructure {
	static final int numHeaps = 10;
	boolean minHeap = false;
	int active = 1;

	public MeldablePQ(VisPanel M) {
		super(M);
	}

	@Override
	abstract public void insert(int x);

	abstract public void delete();

	abstract public void meld(int i, int j);
	
	abstract public void increaseKey(Node v, int delta);
}
