package algvis.core;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import algvis.internationalization.IMenu;
import algvis.internationalization.IMenuItem;
import algvis.internationalization.Languages;

public class AlgVis extends JPanel implements ActionListener {
	private static final long serialVersionUID = -5202486006824196688L;

	JPanel cards;
	JRootPane P;
	Languages L;

	Map<String, IMenu> adtItems = new HashMap<String, IMenu>();
	IMenuItem[] dsItems;

	public AlgVis(JRootPane P) {
		this(P, "en");
	}

	public AlgVis(JRootPane P, String s) {
		this.P = P;
		// Internationalization
		L = new Languages(s);
		NodeImages.loadImages();
	}

	public void init() {
		// Menu
		JMenuBar menuBar;
		menuBar = new JMenuBar();
		IMenu dsMenu = new IMenu(L, "datastructures");
		dsMenu.setMnemonic(KeyEvent.VK_D);
		IMenu lMenu = new IMenu(L, "language");
		lMenu.setMnemonic(KeyEvent.VK_L);

		// Data structures menu
		// Dictionaries
		/**
		 * Create a submenu (IMenu) for each abstract data type listed in the
		 * class ADTs.
		 */
		for (int i = 0; i < ADTs.N; ++i) {
			String adtName = ADTs.getName(i);
			adtItems.put(adtName, new IMenu(L, adtName));
		}
		/**
		 * Create menu items for each data structure listed in the class
		 * DataStructures.
		 */
		dsItems = new IMenuItem[DataStructures.N];
		for (int i = 0; i < DataStructures.N; ++i) {
			dsItems[i] = new IMenuItem(L, DataStructures.getName(i));
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
			VisPanel P = DataStructures.getPanel(i, L);
			if (P != null)
				cards.add(P, DataStructures.getName(i));
		}

		add(menuBar);
		P.setJMenuBar(menuBar);
		add(cards);

		Fonts.init(getGraphics());
	}

	public void actionPerformed(ActionEvent e) {
		String[] cmd = e.getActionCommand().split("-", 2);
		if ("lang".equals(cmd[0])) {
			L.selectLanguage(cmd[1]);
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
