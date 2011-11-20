package algvis.rotations;

import algvis.core.DataStructure;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class RotPanel extends VisPanel {
	private static final long serialVersionUID = -5154501209600594791L;
	public static Class<? extends DataStructure> DS = Rotations.class;

	public RotPanel(Languages L) {
		super(L);
	}

	@Override
	public void initDS() {
		D = new Rotations(this);
		B = new RotButtons(this);
	}
}
