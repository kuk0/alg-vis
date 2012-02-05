package algvis.internationalization;

public class IButton extends ChButton implements LanguageListener {
	private static final long serialVersionUID = -6020341462591231389L;
	Languages L;
	String t;

	public IButton(Languages L, String text) {
		super(L.getString(text));
		this.L = L;
		this.t = text;
		L.addListener(this);
	}

	public void setT(String text) {
		t = text;
		refresh();
	}

	@Override
	public void languageChanged() {
		setText(L.getString(t));
		super.refresh();
	}
}
