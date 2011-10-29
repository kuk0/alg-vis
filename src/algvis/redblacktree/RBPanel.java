package algvis.redblacktree;

import algvis.core.AlgVis;
import algvis.core.DataStructure;
import algvis.core.VisPanel;

public class RBPanel extends VisPanel {
	private static final long serialVersionUID = 8641701120532396725L;
	public static Class<? extends DataStructure> DS = RB.class;

	public RBPanel(AlgVis a) {
		super(a);
	}

	@Override
	public void initDS() {
		D = new RB(this);
		B = new RBButtons(this);
	}
}
