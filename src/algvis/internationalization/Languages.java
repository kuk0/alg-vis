/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.internationalization;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Languages {
	private static final int N = 2;
	private static int current_lang;
	private static final Locale[] all_locales = new Locale[N];
	private static final ResourceBundle[] all_msgs = new ResourceBundle[N];
	// private static Locale locale;
	// private static ResourceBundle msg;
	private static final List<LanguageListener> listeners = new LinkedList<LanguageListener>();

	static {
		all_locales[0] = new Locale("en");
		all_msgs[0] = ResourceBundle.getBundle("Messages", all_locales[0]);
		all_locales[1] = new Locale("sk");
		all_msgs[1] = ResourceBundle.getBundle("Messages", all_locales[1]);
	}

	public static void addListener(LanguageListener l) {
		Languages.listeners.add(l);
	}

	static void selectLanguage(int i) {
		if (i < 0 || i >= N) {
			i = 0;
		}
		current_lang = i;
		// locale = all_locales[current_lang];
		// msg = all_msgs[current_lang];
		// notify all listeners
		for (final LanguageListener l : listeners) {
			l.languageChanged();
		}
	}

	public static void selectLanguage(String s) {
		int i;
		if ("sk".equals(s)) {
			i = 1;
		} else {
			i = 0;
		}
		selectLanguage(i);
	}

	public static String getString(String s) {
		try {
			return all_msgs[current_lang].getString(s);
		} catch (final MissingResourceException e) {
			System.err.println(e.getMessage() + ": " + s);
			return "???";
		}
	}

	public static int getCurrentLanguage() {
		return current_lang;
	}
}
