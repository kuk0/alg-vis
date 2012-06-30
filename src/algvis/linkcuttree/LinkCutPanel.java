package algvis.linkcuttree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.gui.VisPanel;

public class LinkCutPanel extends VisPanel {
	private static final long serialVersionUID = 4784662431815459414L;
	public static Class<? extends DataStructure> DS = ETtree.class;

	public LinkCutPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		D = new LinkCutDS(this);
		B = new LinkCutButtons(this);
		D.random(20);
	}
}
