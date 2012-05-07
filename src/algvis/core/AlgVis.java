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
	/* set the default panel
     * 0 - BST ("bst")
     * 1 - Rotations ("rotations")
     * 2 - AVL ("avltree")
     * 3 - 23 tree ("23tree")
     * 4 - 234 tree ("234tree")
     * 5 - B-tree ("btree")
     * 6 - Red-black tree ("redblack")
     * 7 - AA-tree ("aatree")
     * 8 - Treap ("treap")
     * 9 - SkipList ("skiplist")
     * 10 - Scapegoat tree ("scapegoat")
     * 11 - Splay tree ("splaytree")
     * 12 - Heap ("heap")
     * 13 - d-ary heap ("daryheap")
     * 14 - Leftist heap ("leftheap")
     * 15 - Skew heap ("skewheap")
     * 16 - Binomial heap ("binheap")
     * 17 - Lazy Binomial heap ("lazybinheap")
     * 18 - Fibonacci heap ("fibheap")
     * 19 - Union-find ("ufi")
     * 20 - Trie ("trie") 
     * 21 - Suffix Tree ("suffixtree") */
	final static int DEFAULT_DS = 21;
	
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
		Fonts.init(getGraphics());

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

		/*
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
		*/

		// Cards with data structures
		cards = new JPanel(new CardLayout());
		for (int i = 0; i < DataStructures.N; ++i) {
			VisPanel P = DataStructures.getPanel(i, S);
			if (P != null)
				cards.add(P, DataStructures.getName(i));
		}

		add(menuBar);
		P.setJMenuBar(menuBar);
		add(cards);

		CardLayout cl = (CardLayout) (cards.getLayout());
		cl.show(cards, DataStructures.getName(DEFAULT_DS));
	}

	@Override
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
