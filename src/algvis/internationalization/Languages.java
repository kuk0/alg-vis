package algvis.internationalization;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Languages {
	final int N = 2;
	int current_lang;
	Locale[] all_locales = new Locale[N];
	ResourceBundle[] all_msgs = new ResourceBundle[N];
	Locale locale;
	ResourceBundle msg;

	public Languages() {
		all_locales[0] = new Locale("en");
		all_msgs[0] = ResourceBundle.getBundle("Messages", all_locales[0]);
		all_locales[1] = new Locale("sk");
		all_msgs[1] = ResourceBundle.getBundle("Messages_sk", all_locales[1]);
	}

	public Languages(int i) {
		this();
		selectLanguage(i);
	}

	public Languages(String s) {
		this();
		selectLanguage(s);
	}

	public void selectLanguage(int i) {
		if (i < 0 || i >= N) {
			i = 0;
		}
		current_lang = i;
		locale = all_locales[current_lang];
		msg = all_msgs[current_lang];
	}

	public void selectLanguage(String s) {
		int i;
		if ("sk".equals(s)) {
			i = 1;
		} else {
			i = 0;
		}
		selectLanguage(i);
	}

	public String getString(String s) {
		try {
			return all_msgs[current_lang].getString(s);
		} catch (MissingResourceException e) {
			System.out.println(s);
			System.out.println(e.getMessage());
			return "";
		}
	}
}
