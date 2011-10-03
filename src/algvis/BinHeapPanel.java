package algvis;

public class BinHeapPanel extends VisPanel {
	private static final long serialVersionUID = 2070258718656241421L;

	public BinHeapPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "binheap";
	}

	@Override
	public void initDS() {
		D = new BinomialHeap(this);
		B = new MeldablePQButtons(this);
	}
}
