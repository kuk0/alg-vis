package algvis.treap;

import algvis.core.DataStructure;
import algvis.core.DictButtons;
import algvis.core.Settings;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class TreapPanel extends VisPanel {
	private static final long serialVersionUID = -6193823024855399059L;
	public static Class<? extends DataStructure> DS = Treap.class;

	public TreapPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new Treap(this);
		B = new DictButtons(this);
	}
}
