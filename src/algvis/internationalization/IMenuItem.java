package algvis.internationalization;

import javax.swing.JMenuItem;

public class IMenuItem extends JMenuItem {
	private static final long serialVersionUID = -6522159616479156702L;
	Languages L;
	String t;

	public IMenuItem(Languages L, String text) {
		super(L.getString(text));
		this.L = L;
		this.t = text;
	}

	public IMenuItem(Languages L, String text, int K) {
		super(L.getString(text), K);
		this.L = L;
		this.t = text;
	}

	public void setT(String text) {
		t = text;
		refresh();
	}

	public void refresh() {
		setText(L.getString(t));
		//super.refresh();
	}
}
