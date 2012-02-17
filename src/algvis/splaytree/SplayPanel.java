package algvis.splaytree;

import algvis.core.DataStructure;
import algvis.core.DictButtons;
import algvis.core.Settings;
import algvis.core.VisPanel;

public class SplayPanel extends VisPanel {
	private static final long serialVersionUID = 7896254510404637883L;
	public static Class<? extends DataStructure> DS = SplayTree.class;

	public SplayPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new SplayTree(this);
		B = new DictButtons(this);
	}
}
