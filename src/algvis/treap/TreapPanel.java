package algvis.treap;

import algvis.core.AlgVis;
import algvis.core.DictButtons;
import algvis.core.VisPanel;

public class TreapPanel extends VisPanel {
	private static final long serialVersionUID = -6193823024855399059L;

	public TreapPanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "treap";
	}

	@Override
	public void initDS() {
		D = new Treap(this);
		B = new DictButtons(this);
	}
}
