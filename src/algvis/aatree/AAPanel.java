package algvis.aatree;

import algvis.core.AlgVis;
import algvis.core.DataStructure;
import algvis.core.VisPanel;

public class AAPanel extends VisPanel {
	private static final long serialVersionUID = 7589910312644830227L;
	public static Class<? extends DataStructure> DS = AA.class;

	public AAPanel(AlgVis a) {
		super(a);
	}

	@Override
	public void initDS() {
		D = new AA(this);
		B = new AAButtons(this);
	}
}
