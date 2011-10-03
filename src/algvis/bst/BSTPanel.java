package algvis.bst;

import algvis.core.AlgVis;
import algvis.core.DictButtons;
import algvis.core.VisPanel;

public class BSTPanel extends VisPanel {
	private static final long serialVersionUID = 2619694930536571557L;

	public BSTPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "bst";
	}

	@Override
	public void initDS() {
		D = new BST(this);
		B = new DictButtons(this);
	}
}
