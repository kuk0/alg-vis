package algvis.core;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.util.Locale;
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

	IMenu dictItem, pqItem;
	IMenuItem[] dsItems;
	DataStructures DS;

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
		dictItem = new IMenu(this, "dict");
		pqItem = new IMenu(this, "pq");
		dsItems = new IMenuItem[DataStructures.N];
		for (int i = 0; i < DataStructures.N; ++i) {
			dsItems[i] = new IMenuItem(this, DataStructures.NAME[i]);
			if (DataStructures.TYPE[i].equals("pq")) {
				pqItem.add(dsItems[i]);
			} else {
				dictItem.add(dsItems[i]);
			}
			dsItems[i].setActionCommand("ds-" + DataStructures.NAME[i]);
			dsItems[i].addActionListener(this);
		}
		dsMenu.add(dictItem);
		dsMenu.add(pqItem);
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
			try {
				Constructor ct = DataStructures.PANEL[i]
						.getConstructor(AlgVis.class);
				cards.add((VisPanel) ct.newInstance(this),
						DataStructures.NAME[i]);
			} catch (Exception e) {
			}
		}

		add(menuBar);
		P.setJMenuBar(menuBar);
		add(cards);

		Fonts.init(this);
	}

	public void refresh() {
		dictItem.refresh();
		pqItem.refresh();
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
			System.out.println("akcia: " + cmd[1]);
		}
		if ("ds".equals(cmd[0])) {
			System.out.println("akcia: " + cmd[1]);
			for (int i = 0; i < DataStructures.N; ++i) {
				if (DataStructures.NAME[i].equals(cmd[1])) {
					// nastav i-ty panel
					CardLayout cl = (CardLayout) (cards.getLayout());
					cl.show(cards, DataStructures.NAME[i]);
					break;
				}
			}
		}
	}
}
