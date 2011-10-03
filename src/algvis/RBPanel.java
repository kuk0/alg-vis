package algvis;

public class RBPanel extends VisPanel {
	private static final long serialVersionUID = 8641701120532396725L;
	final String title = "redblack";

	public RBPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "redblack";
	}

	@Override
	public void initDS() {
		D = new RB(this);
		B = new RBButtons(this);
	}
}
