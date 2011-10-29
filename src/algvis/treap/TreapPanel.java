package algvis.treap;

import algvis.core.AlgVis;
import algvis.core.DataStructure;
import algvis.core.DictButtons;
import algvis.core.VisPanel;

public class TreapPanel extends VisPanel {
	private static final long serialVersionUID = -6193823024855399059L;
	public static Class<? extends DataStructure> DS = Treap.class;

	public TreapPanel(AlgVis a) {
		super(a);
	}

	@Override
	public void initDS() {
		D = new Treap(this);
		B = new DictButtons(this);
	}
}
