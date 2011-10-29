package algvis.lazybinomialheap;

import algvis.core.AlgVis;
import algvis.core.DataStructure;
import algvis.core.MeldablePQButtons;
import algvis.core.VisPanel;

public class LazyBinHeapPanel extends VisPanel {
	private static final long serialVersionUID = 1997265264400223983L;
	public static Class<? extends DataStructure> DS = LazyBinomialHeap.class;

	public LazyBinHeapPanel(AlgVis a) {
		super(a);
	}

	@Override
	public void initDS() {
		D = new LazyBinomialHeap(this);
		B = new MeldablePQButtons(this);
	}
}
