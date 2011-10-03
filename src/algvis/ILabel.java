package algvis;

public class ILabel extends ChLabel {
	private static final long serialVersionUID = 8993404595330090194L;
	AlgVis a;
	String t;

	public ILabel(AlgVis a, String text) {
		super(a.getString(text));
		this.a = a;
		this.t = text;
	}

	public void setT(String text) {
		t = text;
		refresh();
	}

	public void refresh() {
		setText(a.getString(t));
		super.refresh();
	}
}
