package algvis.splaytree;

import algvis.core.DataStructure;
import algvis.core.DictButtons;
import algvis.core.VisPanel;
import algvis.internationalization.Languages;

public class SplayPanel extends VisPanel {
	private static final long serialVersionUID = 7896254510404637883L;
	public static Class<? extends DataStructure> DS = Splay.class;

	public SplayPanel(Languages L) {
		super(L);
	}

	@Override
	public void initDS() {
		D = new Splay(this);
		B = new DictButtons(this);
	}
}
