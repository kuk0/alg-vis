package algvis.fibonacciheap;

import algvis.core.DataStructure;
import algvis.core.MeldablePQButtons;
import algvis.core.Settings;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class FibHeapPanel extends VisPanel {
	private static final long serialVersionUID = 2755087791754509441L;
	public static Class<? extends DataStructure> DS = FibonacciHeap.class;

	public FibHeapPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new FibonacciHeap(this);
		B = new MeldablePQButtons(this);
	}

}
