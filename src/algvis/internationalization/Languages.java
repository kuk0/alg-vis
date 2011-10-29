package algvis.internationalization;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Languages {
	int current_lang;
	Locale[] all_locales = new Locale[2];
	ResourceBundle[] all_msgs = new ResourceBundle[2];
	Locale locale;
	ResourceBundle msg;

	public Languages() {
		all_locales[0] = new Locale("en");
		all_msgs[0] = ResourceBundle.getBundle("Messages", all_locales[0]);
		all_locales[1] = new Locale("sk");
		all_msgs[1] = ResourceBundle.getBundle("Messages_sk", all_locales[1]);
	}
	
	public void selectLanguage(int i) {
		current_lang = i;
		locale = all_locales[current_lang];
		msg = all_msgs[current_lang];		
	}
	
	public void selectLanguage(String s) {
		int i;
		if (s.equals("sk")) {
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
