package algvis.btree;

import algvis.core.DataStructure;
import algvis.core.DictButtons;
import algvis.core.Settings;
import algvis.core.VisPanel;

public class a234Panel  extends VisPanel {
	private static final long serialVersionUID = 3849830550882705599L;
	public static Class<? extends DataStructure> DS = a234Tree.class;

	public a234Panel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new a234Tree(this);
		B = new DictButtons(this);
	}
}
