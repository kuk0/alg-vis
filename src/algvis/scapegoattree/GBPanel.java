package algvis.scapegoattree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.core.VisPanel;

public class GBPanel extends VisPanel {
	private static final long serialVersionUID = 5223738995380219622L;
	public static Class<? extends DataStructure> DS = GBTree.class;

	public GBPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new GBTree(this);
		B = new GBButtons(this);
	}
}
