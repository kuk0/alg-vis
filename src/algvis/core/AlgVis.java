package algvis.core;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import algvis.internationalization.IMenu;
import algvis.internationalization.IMenuItem;

public class AlgVis extends JPanel implements ActionListener {
	private static final long serialVersionUID = -5202486006824196688L;
	String lang;

	int current_lang;
	Locale[] all_locales = new Locale[2];
	ResourceBundle[] all_msgs = new ResourceBundle[2];
	Locale locale;
	ResourceBundle msg;
	JPanel cards;
	JRootPane P;

	Map<String, IMenu> adtItems = new HashMap<String, IMenu>();
	IMenuItem[] dsItems;
//	DataStructures DS;

	public AlgVis(JRootPane P) {
		this.P = P;
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

	public void init() {
		// Internationalization
		all_locales[0] = new Locale("en");
		all_msgs[0] = ResourceBundle.getBundle("Messages", all_locales[0]);
		all_locales[1] = new Locale("sk");
		all_msgs[1] = ResourceBundle.getBundle("Messages_sk", all_locales[1]);

		lang = "en"; // getParameter("lang");
		if (lang.equals("sk")) {
			current_lang = 1;
		} else {
			current_lang = 0;
		}
		locale = all_locales[current_lang];
		msg = all_msgs[current_lang];

		// Menu
		JMenuBar menuBar;
		menuBar = new JMenuBar();
		IMenu dsMenu = new IMenu(this, "datastructures");
		dsMenu.setMnemonic(KeyEvent.VK_D);
		IMenu lMenu = new IMenu(this, "language");
		lMenu.setMnemonic(KeyEvent.VK_L);

		// Data structures menu
		// Dictionaries
		/**
		 * Create a submenu (IMenu) for each abstract data type listed in the class ADTs.
		 */
		for (int i = 0; i < ADTs.N; ++i) {
			String adtName = ADTs.getName(i);
			adtItems.put(adtName, new IMenu(this, adtName));
		}
		/**
		 * Create menu items for each data structure listed in the class DataStructures.
		 */
		dsItems = new IMenuItem[DataStructures.N];
		for (int i = 0; i < DataStructures.N; ++i) {
			dsItems[i] = new IMenuItem(this, DataStructures.getName(i));
			adtItems.get(DataStructures.getADT(i)).add(dsItems[i]);
			dsItems[i].setActionCommand("ds-" + DataStructures.getName(i));
			dsItems[i].addActionListener(this);
		}
		/**
		 * Put all the ADT submenus under the all data structures menu.
		 */
		for (int i = 0; i < ADTs.N; ++i) {
			dsMenu.add(adtItems.get(ADTs.getName(i)));
		}
		menuBar.add(dsMenu);

		// Language menu
		JMenuItem enItem = new JMenuItem("English", KeyEvent.VK_B);
		JMenuItem skItem = new JMenuItem("Slovensky", KeyEvent.VK_B);
		enItem.setActionCommand("lang-en");
		skItem.setActionCommand("lang-sk");
		enItem.addActionListener(this);
		skItem.addActionListener(this);

		lMenu.add(enItem);
		lMenu.add(skItem);
		menuBar.add(lMenu);

		// Cards with data structures
		cards = new JPanel(new CardLayout());
		for (int i = 0; i < DataStructures.N; ++i) {
			VisPanel P = DataStructures.getPanel(i, this);
			if (P != null) cards.add(P, DataStructures.getName(i));
		}

		add(menuBar);
		P.setJMenuBar(menuBar);
		add(cards);

		Fonts.init(this);
	}

	public void refresh() {
		Collection<IMenu> c = adtItems.values();
		for (Iterator<IMenu> it = c.iterator(); it.hasNext();) {
			it.next().refresh();
		}
		/*for (Iterator it = adtItems.entrySet().iterator(); it.hasNext();) {
			((Map.Entry<String, IMenu>) it.next()).getValue().refresh();
		}*/
		for (int i = 0; i < DataStructures.N; ++i) {
			dsItems[i].refresh();
			((VisPanel)(cards.getComponent(i))).refresh();
		}
	}

	public void actionPerformed(ActionEvent e) {
		String[] cmd = e.getActionCommand().split("-", 2);
		if ("lang".equals(cmd[0])) {
			if ("en".equals(cmd[1])) {
				current_lang = 0;
			} else if ("sk".equals(cmd[1])) {
				current_lang = 1;
			}
			locale = all_locales[current_lang];
			msg = all_msgs[current_lang];
			refresh();
			// DEBUG: System.out.println("akcia: " + cmd[1]);
		}
		if ("ds".equals(cmd[0])) {
			// DEBUG: System.out.println("akcia: " + cmd[1]);
			for (int i = 0; i < DataStructures.N; ++i) {
				if (DataStructures.getName(i).equals(cmd[1])) {
					// nastav i-ty panel
					CardLayout cl = (CardLayout) (cards.getLayout());
					cl.show(cards, DataStructures.getName(i));
					break;
				}
			}
		}
	}
}
