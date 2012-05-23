package algvis.core;

public abstract class IntervalTrees extends DataStructure{
	public static String adtName = "intervaltrees";
	public enum mimasuType {
		MIN, MAX, SUM
	}
	public mimasuType minTree = mimasuType.MAX;
	
	public IntervalTrees(VisPanel M) {
		super(M);
	}

	@Override
	abstract public void insert(int x);

	abstract public void changeKey(Node v, int value);
	
	abstract public void ofinterval(int b, int e);

}
