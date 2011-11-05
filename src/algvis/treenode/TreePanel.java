package algvis.treenode;

import algvis.bst.BST;
import algvis.core.DataStructure;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class TreePanel extends VisPanel {
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
