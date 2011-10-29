package algvis.fibonacciheap;

import algvis.core.AlgVis;
import algvis.core.DataStructure;
import algvis.core.MeldablePQButtons;
import algvis.core.VisPanel;

public class FibHeapPanel extends VisPanel {
	private static final long serialVersionUID = 2755087791754509441L;
	public static Class<? extends DataStructure> DS = FibonacciHeap.class;

	public FibHeapPanel(AlgVis a) {
		super(a);
	}

	@Override
	public void initDS() {
		D = new FibonacciHeap(this);
		B = new MeldablePQButtons(this);
	}

}
