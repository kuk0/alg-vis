package algvis.btree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.core.VisPanel;

public class BPanel extends VisPanel {
	private static final long serialVersionUID = 3849830550882705599L;
	public static Class<? extends DataStructure> DS = BTree.class;

	public BPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new BTree(this);
		B = new BTreeButtons(this);
	}
}
