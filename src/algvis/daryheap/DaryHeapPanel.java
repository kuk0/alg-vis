package algvis.daryheap;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.gui.VisPanel;

public class DaryHeapPanel extends VisPanel{
	private static final long serialVersionUID = 5387116424458217311L;
	public static Class<? extends DataStructure> DS = DaryHeap.class;

	public DaryHeapPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new DaryHeap(this);
		B = new DaryHeapButtons(this);
		D.random(14);
	}
}
