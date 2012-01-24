package algvis.core;

abstract public class Dictionary extends DataStructure {
	public static String adtName = "dictionary";

	public Dictionary(VisPanel M, String dsName) {
		super(M, dsName);
	}

	@Override
	abstract public void insert(int x);

	abstract public void find(int x);

	abstract public void delete(int x);
	
	@Override
	public Layout getLayout() {
		return Layout.COMPACT;
	}
}
