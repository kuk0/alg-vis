package algvis;

public class FibHeapPanel extends VisPanel {
	private static final long serialVersionUID = 2755087791754509441L;

	public FibHeapPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "fibheap";
	}

	@Override
	public void initDS() {
		D = new FibonacciHeap(this);
		B = new MeldablePQButtons(this);
	}

}
