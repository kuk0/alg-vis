package algvis.avltree;

import algvis.core.AlgVis;
import algvis.core.DataStructure;
import algvis.core.DictButtons;
import algvis.core.VisPanel;

public class AVLPanel extends VisPanel {
	private static final long serialVersionUID = 9157486805598407776L;
	public static Class<? extends DataStructure> DS = AVL.class;

	public AVLPanel(AlgVis a) {
		super(a);
	}

	@Override
	public void initDS() {
		D = new AVL(this);
		B = new DictButtons(this);
	}
}
