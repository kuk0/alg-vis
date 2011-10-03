package algvis;

import javax.swing.JMenuItem;

public class IMenuItem extends JMenuItem {
	private static final long serialVersionUID = -6522159616479156702L;
	AlgVis a;
	String t;

	public IMenuItem(AlgVis a, String text) {
		super(a.getString(text));
		this.a = a;
		this.t = text;
	}

	public IMenuItem(AlgVis a, String text, int K) {
		super(a.getString(text), K);
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
