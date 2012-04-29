package algvis.skewheap;

import algvis.core.DataStructure;
import algvis.core.MeldablePQButtonsNoDecr;
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
		SkewHeap H = new SkewHeap(this);
		D = H;
		B = new MeldablePQButtonsNoDecr(this);
		H.active = 1;
		D.random(13);
		H.active = 2;
		D.random(10);
		H.lowlight();
		H.active = 3;
		D.random(7);
		H.lowlight();
		H.active = 1;
		D.M.screen.V.resetView();
	}

}
