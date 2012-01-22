package algvis.internationalization;

import java.awt.Color;

import javax.swing.JComboBox;

public class IComboBox extends JComboBox implements LanguageListener {
	private static final long serialVersionUID = 8795452558528688577L;
	Languages L;
	String[] choices;

	public IComboBox(Languages L, String[] choices) {
		super(choices);
		this.L = L;
		this.choices = choices;
		languageChanged();
		L.addListener(this);
		setBackground(Color.WHITE);
	}

	@Override
	public void languageChanged() {
		removeAllItems();
		for (int i = 0; i < choices.length; ++i) {
			this.addItem(L.getString(choices[i]));
		}
	}
}
