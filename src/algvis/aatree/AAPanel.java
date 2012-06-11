package algvis.aatree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.core.VisPanel;

public class AAPanel extends VisPanel {
	private static final long serialVersionUID = 7589910312644830227L;
	public static Class<? extends DataStructure> DS = AA.class;

	public AAPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new AA(this);
		B = new AAButtons(this);
	}
}
