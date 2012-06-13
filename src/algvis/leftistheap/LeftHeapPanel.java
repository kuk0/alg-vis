package algvis.leftistheap;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.gui.MeldablePQButtonsNoDecr;
import algvis.gui.VisPanel;

public class LeftHeapPanel extends VisPanel {
	private static final long serialVersionUID = -6885107230514971633L;
	public static Class<? extends DataStructure> DS = LeftHeap.class;

	public LeftHeapPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		LeftHeap L = new LeftHeap(this);
		D = L;
		B = new MeldablePQButtonsNoDecr(this);
		L.active = 1;
		D.random(13);
		L.active = 2;
		D.random(10);
		L.lowlight();
		L.active = 3;
		D.random(7);
		L.lowlight();
		L.active = 1;
		D.M.screen.V.resetView();
	}

}
