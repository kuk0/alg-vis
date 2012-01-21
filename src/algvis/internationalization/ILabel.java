package algvis.internationalization;

import java.awt.Color;


public class ILabel extends ChLabel implements LanguageListener {
	private static final long serialVersionUID = 8993404595330090194L;
	Languages L;
	String t;

	public ILabel(Languages L, String text) {
		super(L.getString(text));
		this.L = L;
		this.t = text;
		L.addListener(this);
	}

	// aaah... JLabel constructor calls setText but at that time L is null...
	public void setT(String text) {
		t = text;
		setText(L.getString(t));
		refresh();
	}

	public void languageChanged() {
		setText(L.getString(t));
		refresh();
	}
}
