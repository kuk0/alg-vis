package algvis;

import javax.swing.JRadioButton;

public class IRadioButton extends JRadioButton {
	private static final long serialVersionUID = -8675513915804080311L;
	AlgVis a;
	String t;

	public IRadioButton(AlgVis a, String text) {
		super(a.getString(text));
		this.a = a;
		this.t = text;
	}

	public void refresh() {
		setText(a.getString(t));
	}
}
