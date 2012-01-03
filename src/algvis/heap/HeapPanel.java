package algvis.heap;

import algvis.core.DataStructure;
import algvis.core.PQButtons;
import algvis.core.Settings;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class HeapPanel extends VisPanel {
	private static final long serialVersionUID = -2488725769022251713L;
	public static Class<? extends DataStructure> DS = Heap.class;

	public HeapPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new Heap(this);
		B = new PQButtons(this);
	}
}
