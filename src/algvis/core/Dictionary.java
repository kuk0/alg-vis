package algvis.core;

abstract public class Dictionary extends DataStructure {
	public static String adtName = "dictionary";
	
	public Dictionary(VisPanel M) {
		super(M);
	}

	@Override
	abstract public void insert(int x);

	abstract public void find(int x);

	abstract public void delete(int x);
}
