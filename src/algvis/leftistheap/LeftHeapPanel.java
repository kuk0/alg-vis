package algvis.leftistheap;

import algvis.core.AlgVis;
import algvis.core.VisPanel;
import algvis.core.MeldablePQButtons;

public class LeftHeapPanel extends VisPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6885107230514971633L;


	public LeftHeapPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "leftheap";
	}

	@Override
	public void initDS() {
		D = new LeftHeap(this);
		B = new MeldablePQButtons(this);
		
	}

}
