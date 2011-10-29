package algvis.redblacktree;

import algvis.core.DataStructure;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class RBPanel extends VisPanel {
	private static final long serialVersionUID = 8641701120532396725L;
	public static Class<? extends DataStructure> DS = RB.class;

	public RBPanel(Languages L) {
		super(L);
	}

	@Override
	public void initDS() {
		D = new RB(this);
		B = new RBButtons(this);
	}
}
