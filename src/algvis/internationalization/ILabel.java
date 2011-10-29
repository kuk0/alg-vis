package algvis.internationalization;


public class ILabel extends ChLabel {
	private static final long serialVersionUID = 8993404595330090194L;
	Languages L;
	String t;

	public ILabel(Languages L, String text) {
		super(L.getString(text));
		this.L = L;
		this.t = text;
	}

	public void setT(String text) {
		t = text;
		refresh();
	}

	public void refresh() {
		setText(L.getString(t));
		super.refresh();
	}
}
