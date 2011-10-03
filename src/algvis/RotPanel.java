package algvis;

public class RotPanel extends VisPanel {
	private static final long serialVersionUID = -5154501209600594791L;

	public RotPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "rotations";
	}

	@Override
	public void initDS() {
		D = new Rotations(this);
		B = new RotButtons(this);
	}
}
