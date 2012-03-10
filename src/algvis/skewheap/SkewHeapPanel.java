package algvis.skewheap;

import algvis.core.DataStructure;
import algvis.core.MeldablePQButtons;
import algvis.core.Settings;
import algvis.core.VisPanel;

public class SkewHeapPanel extends VisPanel {
	private static final long serialVersionUID = -2947713003292797010L;
	public static Class<? extends DataStructure> DS = SkewHeap.class;
	
	public SkewHeapPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new SkewHeap(this);
		B = new MeldablePQButtons(this);
		
	}

}
