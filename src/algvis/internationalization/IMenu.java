package algvis.internationalization;

import javax.swing.JMenu;

public class IMenu extends JMenu implements LanguageListener {
	private static final long serialVersionUID = -6631532284442911505L;
	Languages L;
	String t;

	public IMenu(Languages L, String text) {
		super(L.getString(text));
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
		setText(L.getString(t));
	}
}
