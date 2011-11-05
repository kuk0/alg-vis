package algvis.treenode;

import algvis.core.DataStructure;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class TreePanel extends VisPanel {
	private static final long serialVersionUID = -4985801885982297379L;
	public static Class<? extends DataStructure> DS = TreeDS.class;

	public TreePanel(Languages L) {
		super(L);
	}

	@Override
	public void initDS() {
		D = new TreeDS(this);
		B = new TreeButtons(this);
	}

}
