package algvis.heap;

import algvis.core.AlgVis;
import algvis.core.DataStructure;
import algvis.core.PQButtons;
import algvis.core.VisPanel;

public class HeapPanel extends VisPanel {
	private static final long serialVersionUID = -2488725769022251713L;
	public static Class<? extends DataStructure> DS = Heap.class;

	public HeapPanel(AlgVis a) {
		super(a);
	}

	@Override
	public void initDS() {
		D = new Heap(this);
		B = new PQButtons(this);
	}
}
