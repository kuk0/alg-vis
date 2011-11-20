package algvis.aatree;

import algvis.core.DataStructure;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class AAPanel extends VisPanel {
	private static final long serialVersionUID = 7589910312644830227L;
	public static Class<? extends DataStructure> DS = AA.class;

	public AAPanel(Languages L) {
		super(L);
	}

	@Override
	public void initDS() {
		D = new AA(this);
		B = new AAButtons(this);
	}
}
