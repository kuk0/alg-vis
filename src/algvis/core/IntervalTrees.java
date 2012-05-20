package algvis.core;

public abstract class IntervalTrees extends DataStructure{
	public static String adtName = "intervaltrees";
	public boolean minTree = false;
	
	public IntervalTrees(VisPanel M) {
		super(M);
	}

	@Override
	abstract public void insert(int x);

	abstract public void decreaseKey(Node v, int delta);
	
	abstract public void ofinterval(int b, int e);

}
