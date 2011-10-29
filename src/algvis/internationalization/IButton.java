package algvis.internationalization;


public class IButton extends ChButton {
	private static final long serialVersionUID = -6020341462591231389L;
	Languages L;
	String t;

	public IButton(Languages L, String text) {
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
