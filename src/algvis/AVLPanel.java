package algvis;

public class AVLPanel extends VisPanel {
	private static final long serialVersionUID = 9157486805598407776L;

	public AVLPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "avltree";
	}

	@Override
	public void initDS() {
		D = new AVL(this);
		B = new DictButtons(this);
	}
}
