package algvis;

public class SkipListPanel extends VisPanel {
	private static final long serialVersionUID = -283805545295164774L;
	final String title = "skiplist";

	public SkipListPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "skiplist";
	}

	@Override
	public void initDS() {
		D = new SkipList(this);
		B = new DictButtons(this);
	}
}
