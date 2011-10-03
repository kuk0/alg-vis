package algvis;

public class GBPanel extends VisPanel {
	private static final long serialVersionUID = 5223738995380219622L;

	public GBPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "scapegoat";
	}

	@Override
	public void initDS() {
		D = new GBTree(this);
		B = new GBButtons(this);
	}
}
