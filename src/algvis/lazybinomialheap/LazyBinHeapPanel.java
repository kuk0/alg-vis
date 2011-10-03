package algvis.lazybinomialheap;

import algvis.core.AlgVis;
import algvis.core.MeldablePQButtons;
import algvis.core.VisPanel;

public class LazyBinHeapPanel extends VisPanel {
	private static final long serialVersionUID = 1997265264400223983L;

	public LazyBinHeapPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "lazybinheap";
	}

	@Override
	public void initDS() {
		D = new LazyBinomialHeap(this);
		B = new MeldablePQButtons(this);
	}
}
