package algvis;

public class IButton extends ChButton {
	private static final long serialVersionUID = -6020341462591231389L;
	AlgVis a;
	String t;

	public IButton(AlgVis a, String text) {
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
		super.refresh();
	}
}
