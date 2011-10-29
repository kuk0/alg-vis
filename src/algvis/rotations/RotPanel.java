package algvis.rotations;

import algvis.core.AlgVis;
import algvis.core.DataStructure;
import algvis.core.VisPanel;

public class RotPanel extends VisPanel {
	private static final long serialVersionUID = -5154501209600594791L;
	public static Class<? extends DataStructure> DS = Rotations.class;

	public RotPanel(AlgVis a) {
		super(a);
	}

	@Override
	public void initDS() {
		D = new Rotations(this);
		B = new RotButtons(this);
	}
}
