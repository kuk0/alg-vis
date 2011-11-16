package algvis.internationalization;

import javax.swing.JCheckBox;

public class ICheckBox extends JCheckBox implements LanguageListener {
	private static final long serialVersionUID = -8231264680063436446L;
	Languages L;
	String t;

	public ICheckBox(Languages L, String title, boolean on) {
		super(L.getString(title), on);
		this.L = L;
		this.t = title;
		L.addListener(this);
	}

	@Override
	public void languageChanged() {
		setText(L.getString(t));
	}
}
