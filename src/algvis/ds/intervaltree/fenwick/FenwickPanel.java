package algvis.ds.intervaltree.fenwick;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ui.VisPanel;

public class FenwickPanel extends VisPanel {

	private static final long serialVersionUID = 8609582326933693475L;
	public static Class<? extends DataStructure> DS = FenwickTree.class;

	public FenwickPanel(Settings S) {
		super(S);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initDS() {
		D = new FenwickTree(this);
		scene.add(D);
		buttons = new FenwickButtons(this);
	}

	@Override
	public void start() {
		super.start();
		D.random(6);
	}

}
