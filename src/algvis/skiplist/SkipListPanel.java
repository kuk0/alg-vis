package algvis.skiplist;

import algvis.core.DataStructure;
import algvis.core.DictButtons;
import algvis.core.Settings;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class SkipListPanel extends VisPanel {
	private static final long serialVersionUID = -283805545295164774L;
	public static Class<? extends DataStructure> DS = SkipList.class;

	public SkipListPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new SkipList(this);
		B = new DictButtons(this);
	}
}
