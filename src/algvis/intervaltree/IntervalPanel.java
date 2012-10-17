package algvis.intervaltree;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.gui.VisPanel;

public class IntervalPanel extends VisPanel{
	private static final long serialVersionUID = -5655533916806349111L;
	public static Class<? extends DataStructure> DS = IntervalTree.class;
	
	public IntervalPanel(Settings S, boolean isScenarioEnabled) {
		super(S, isScenarioEnabled);
	}

	@Override
	public void initDS() {
		IntervalTree L = new IntervalTree(this);
		D = L;
		B = new IntervalButtons(this);
		L.random(14);
	}

}
