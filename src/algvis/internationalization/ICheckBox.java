package algvis.internationalization;

import javax.swing.JCheckBox;

import algvis.core.AlgVis;

public class ICheckBox extends JCheckBox {
	private static final long serialVersionUID = -8231264680063436446L;
	AlgVis a;
	String t;

	public ICheckBox(AlgVis a, String title, boolean on) {
		super(a.getString(title), on);
		this.a = a;
		this.t = title;
	}

	public void refresh() {
		setText(a.getString(t));
	}
}
