package algvis.bst;

import algvis.core.DataStructure;
import algvis.core.LayoutListener;
import algvis.core.Settings;
import algvis.core.VisPanel;

public class BSTPanel extends VisPanel {
	private static final long serialVersionUID = 2619694930536571557L;
	public static Class<? extends DataStructure> DS = BST.class;

	public BSTPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new BST(this);
		B = new BSTButtons(this);
		S.addLayoutListener((LayoutListener) D);
	}
}
