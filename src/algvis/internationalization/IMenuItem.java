package algvis.internationalization;

import javax.swing.JMenuItem;

public class IMenuItem extends JMenuItem implements LanguageListener {
	private static final long serialVersionUID = -6522159616479156702L;
	Languages L;
	String t;

	public IMenuItem(Languages L, String text) {
		super(L.getString(text));
		this.L = L;
		this.t = text;
		L.addListener(this);
	}

	public IMenuItem(Languages L, String text, int K) {
		super(L.getString(text), K);
		this.L = L;
		this.t = text;
	}

	public void setT(String text) {
		t = text;
		setText(L.getString(t));
	}

	public void languageChanged() {
		setText(L.getString(t));
	}
}
