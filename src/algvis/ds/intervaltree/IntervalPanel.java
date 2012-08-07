package algvis.ds.intervaltree;

import algvis.core.Settings;
import algvis.ds.DataStructure;
import algvis.gui.VisPanel;

public class IntervalPanel extends VisPanel{
	private static final long serialVersionUID = -5655533916806349111L;
	public static Class<? extends DataStructure> DS = IntervalTree.class;
	
	public IntervalPanel(Settings S) {
		super(S);
	}

	@Override
	public void initDS() {
		IntervalTree L = new IntervalTree(this);
		D = L;
		buttons = new IntervalButtons(this);
		L.random(14);
	}

}
