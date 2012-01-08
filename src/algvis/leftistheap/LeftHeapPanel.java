package algvis.leftistheap;

import algvis.core.DataStructure;
import algvis.core.VisPanel;
import algvis.core.MeldablePQButtons;
import algvis.core.Settings;

public class LeftHeapPanel extends VisPanel {
	private static final long serialVersionUID = -6885107230514971633L;
	public static Class<? extends DataStructure> DS = LeftHeap.class;

	public LeftHeapPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new LeftHeap(this);
		B = new MeldablePQButtons(this);
	}

}
