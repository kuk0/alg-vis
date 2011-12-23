package algvis.core;

import algvis.internationalization.Languages;

public class Settings {
	public Layout layout;
	public Languages L;

	public Settings(Languages L) {
		this.L = L;
		layout = Layout.SIMPLE;
	}
}
