package algvis.pairingheap;


import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.core.VisPanel;

public class PairHeapPanel extends VisPanel{
	private static final long serialVersionUID = 7766114341156126683L;
	public static Class<? extends DataStructure> DS = PairingHeap.class;

	public PairHeapPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new PairingHeap(this);
		B = new PairHeapButtons(this);
	}

}
