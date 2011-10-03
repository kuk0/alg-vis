package algvis.internationalization;

import javax.swing.JMenu;

import algvis.core.AlgVis;

public class IMenu extends JMenu {
	private static final long serialVersionUID = -6631532284442911505L;
	AlgVis a;
	String t;

	public IMenu(AlgVis a, String text) {
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
		//super.refresh();
	}
}
