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
		L.addListener(this);
	}

	public void setT(String text) {
		t = text;
		setText(L.getString(t));
	}

	@Override
	public void languageChanged() {
		if (t.equals("bst"))
			System.out.println("bst");
		if (t.equals("layout-simple"))
			System.out.println("tu");
		setText(L.getString(t));
	}
}
