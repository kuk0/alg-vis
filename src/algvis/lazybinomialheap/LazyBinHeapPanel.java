package algvis.lazybinomialheap;

import algvis.core.DataStructure;
import algvis.core.MeldablePQButtons;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class LazyBinHeapPanel extends VisPanel {
	private static final long serialVersionUID = 1997265264400223983L;
	public static Class<? extends DataStructure> DS = LazyBinomialHeap.class;

	public LazyBinHeapPanel(Languages L) {
		super(L);
	}

	@Override
	public void initDS() {
		D = new LazyBinomialHeap(this);
		B = new MeldablePQButtons(this);
	}
}
