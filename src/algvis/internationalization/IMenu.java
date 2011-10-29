package algvis.internationalization;

import javax.swing.JMenu;

public class IMenu extends JMenu {
	private static final long serialVersionUID = -6631532284442911505L;
	Languages L;
	String t;

	public IMenu(Languages L, String text) {
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
		//super.refresh();
	}
}
