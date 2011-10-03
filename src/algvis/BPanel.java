package algvis;

public class BPanel extends VisPanel {
	private static final long serialVersionUID = 3849830550882705599L;

	public BPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "btree";
	}

	@Override
	public void initDS() {
		D = new BTree(this);
		B = new BTreeButtons(this);
	}
}
