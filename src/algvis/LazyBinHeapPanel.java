package algvis;

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
