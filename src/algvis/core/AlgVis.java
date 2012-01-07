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
	Settings S;

	Map<String, IMenu> adtItems = new HashMap<String, IMenu>();
	IMenuItem[] dsItems;

	public AlgVis(JRootPane P) {
		this(P, "en");
	}

	public AlgVis(JRootPane P, String s) {
		this.P = P;
		L = new Languages(s);
		S = new Settings(L);
	}

	public void init() {
		// Menu
		JMenuBar menuBar;
		menuBar = new JMenuBar();
		IMenu dsMenu = new IMenu(L, "datastructures");
		dsMenu.setMnemonic(KeyEvent.VK_D);
		IMenu langMenu = new IMenu(L, "language");
		langMenu.setMnemonic(KeyEvent.VK_L);
		IMenu layoutMenu = new IMenu(L, "layout");
		layoutMenu.setMnemonic(KeyEvent.VK_Y);

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

		langMenu.add(enItem);
		langMenu.add(skItem);
		menuBar.add(langMenu);
		
		// Layout menu
		IMenuItem sItem = new IMenuItem(L, "layout-simple", KeyEvent.VK_S);
		IMenuItem cItem = new IMenuItem(L, "layout-compact", KeyEvent.VK_C);
		sItem.setActionCommand("layout-simple");
		cItem.setActionCommand("layout-compact");
		sItem.addActionListener(this);
		cItem.addActionListener(this);
		
		layoutMenu.add(sItem);
		layoutMenu.add(cItem);
		menuBar.add(layoutMenu);

		// Cards with data structures
		cards = new JPanel(new CardLayout());
		for (int i = 0; i < DataStructures.N; ++i) {
			VisPanel P = DataStructures.getPanel(i, S);
			if (P != null) cards.add(P, DataStructures.getName(i));
		}

		add(menuBar);
		P.setJMenuBar(menuBar);
		add(cards);

		Fonts.init(getGraphics());
		
		// set default panel for testing; TODO delete these lines
		CardLayout cl = (CardLayout) (cards.getLayout());
		/* 2 - AVL
		 * 7 - AA
		 * 0 - BST
		 */
		cl.show(cards, DataStructures.getName(7));
	}

	public void actionPerformed(ActionEvent e) {
		String[] cmd = e.getActionCommand().split("-", 2);
		
		// set language
		if ("lang".equals(cmd[0])) {
			L.selectLanguage(cmd[1]);
		}
		
		// set layout
		if ("layout".equals(cmd[0])) {
			S.setLayout(cmd[1]);
		}
		
		// set different data structure
		if ("ds".equals(cmd[0])) {
			for (int i = 0; i < DataStructures.N; ++i) {
				if (DataStructures.getName(i).equals(cmd[1])) {
					CardLayout cl = (CardLayout) (cards.getLayout());
					cl.show(cards, DataStructures.getName(i));
					break;
				}
			}
		}
	}
}
