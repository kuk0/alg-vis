package algvis.example;

import algvis.core.AlgVis;
import algvis.core.VisPanel;

public class ExamplePanel extends VisPanel {
	private static final long serialVersionUID = 1403179935384787992L;

	public ExamplePanel(AlgVis a) {
		super(a);
	}

	@Override
	public String getTitle() {
		return "example";
	}

	@Override
	public void initDS() {
		D = new ExampleDS(this);
		B = new ExampleButtons(this);
	}

}
