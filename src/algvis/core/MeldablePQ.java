package algvis.core;

abstract public class MeldablePQ extends DataStructure {
	public static String adtName = "meldable-pq";

	public static final int numHeaps = 10;
	public boolean minHeap = false;
	public int active = 1;

	public MeldablePQ(VisPanel M, String dsName) {
		super(M);
	}

	public static String adtName() {
		return "meldable-pq";
	}

	@Override
	abstract public void insert(int x);

	abstract public void delete();

	abstract public void meld(int i, int j);

	abstract public void decreaseKey(Node v, int delta);
}
