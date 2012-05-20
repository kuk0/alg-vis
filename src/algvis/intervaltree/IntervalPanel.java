package algvis.intervaltree;

import algvis.core.DataStructure;
import algvis.core.IntervalButtons;
import algvis.core.Settings;
import algvis.core.VisPanel;

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
		B = new IntervalButtons(this);
		
	}

}
