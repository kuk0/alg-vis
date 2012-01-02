package algvis.core;

import java.util.LinkedList;
import java.util.List;

import algvis.internationalization.Languages;

public class Settings {
	public Layout layout;
	public Languages L;
	List<LayoutListener> listeners = new LinkedList<LayoutListener>();

	public Settings(Languages L) {
		this.L = L;
		layout = Layout.SIMPLE;
	}

	public void setLayout(String s) {
		if ("compact".equals(s)) {
			layout = Layout.COMPACT;
		} else {
			layout = Layout.SIMPLE;
		}
		for (LayoutListener l : listeners) {
			l.changeLayout();
		}
	}

	public void addLayoutListener(LayoutListener l) {
		listeners.add(l);
	}
}
