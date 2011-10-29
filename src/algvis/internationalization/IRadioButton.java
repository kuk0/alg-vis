package algvis.internationalization;

import javax.swing.JRadioButton;

public class IRadioButton extends JRadioButton {
	private static final long serialVersionUID = -8675513915804080311L;
	Languages L;
	String t;

	public IRadioButton(Languages L, String text) {
		super(L.getString(text));
		this.L = L;
		this.t = text;
	}

	public void refresh() {
		setText(L.getString(t));
	}
}
