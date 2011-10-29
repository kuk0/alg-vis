package algvis.bst;

import algvis.core.DataStructure;
import algvis.core.DictButtons;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class BSTPanel extends VisPanel {
	private static final long serialVersionUID = 2619694930536571557L;
	public static Class<? extends DataStructure> DS = BST.class;

	public BSTPanel(Languages L) {
		super(L);
	}

	@Override
	public void initDS() {
		D = new BST(this);
		B = new DictButtons(this);
	}
}
