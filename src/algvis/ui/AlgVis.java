/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ui;

import algvis.core.ADTs;
import algvis.core.DataStructures;
import algvis.core.Settings;
import algvis.internationalization.IMenu;
import algvis.internationalization.IMenuItem;
import algvis.internationalization.Languages;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class AlgVis extends JPanel implements ActionListener {
    /*
     * set the default panel 0 - BST ("bst") 1 - Rotations ("rotations") 2 - AVL
     * ("avltree") 3 - 23 tree ("23tree") 4 - 234 tree ("234tree") 5 - B-tree
     * ("btree") 6 - Red-black tree ("redblack") 7 - AA-tree ("aatree") 8 -
     * Treap ("treap") 9 - SkipList ("skiplist") 10 - Scapegoat tree
     * ("scapegoat") 11 - Splay tree ("splaytree") 12 - Heap ("heap") 13 - d-ary
     * heap ("daryheap") 14 - Leftist heap ("leftheap") 15 - Skew heap
     * ("skewheap") 16 - Pairing heap ("pairheap") 17 - Binomial heap
     * ("binheap") 18 - Lazy Binomial heap ("lazybinheap") 19 - Fibonacci heap
     * ("fibheap") 20 - Union-find ("ufi") 21 - Interval tree ("intervaltree")
     * 22 - Trie ("trie") 23 - Suffix Tree ("suffixtree")
     */
    private final static int DEFAULT_DS = 11;

    private static final long serialVersionUID = -5202486006824196688L;

    /** Cards with data structures */
    public final JPanel cards;
    private final CardLayout cardlayout;
    public final VisPanel[] panels; //TODO: private
    private int activePanel = -1;
    private final Container container;
    private final Settings S;

    private final Map<String, IMenu> adtItems = new HashMap<String, IMenu>();
    private IMenuItem[] dsItems;

    public AlgVis(Container c) {
        this(c, "en");
    }

    public AlgVis(Container c, String s) {
        this.container = c;
        Languages.selectLanguage(s);
        S = new Settings();
        cards = new JPanel(cardlayout = new CardLayout());
        panels = new VisPanel[DataStructures.N];
    }

    public void init() {
        Fonts.init(getGraphics());

        // Menu
        JMenuBar menuBar;
        menuBar = new JMenuBar();
        final IMenu dsMenu = new IMenu("datastructures");
        dsMenu.setMnemonic(KeyEvent.VK_D);
        final IMenu langMenu = new IMenu("language");
        langMenu.setMnemonic(KeyEvent.VK_L);
        final IMenu layoutMenu = new IMenu("layout");
        layoutMenu.setMnemonic(KeyEvent.VK_Y);

        // Data structures menu
        // Dictionaries
        /**
         * Create a submenu (IMenu) for each abstract data type listed in the
         * class ADTs.
         */
        for (int i = 0; i < ADTs.N; ++i) {
            final String adtName = ADTs.getName(i);
            adtItems.put(adtName, new IMenu(adtName));
        }
        /**
         * Create menu items for each data structure listed in the class
         * DataStructures.
         */
        dsItems = new IMenuItem[DataStructures.N];
        for (int i = 0; i < DataStructures.N; ++i) {
            dsItems[i] = new IMenuItem(DataStructures.getName(i));
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
        final JMenuItem enItem = new JMenuItem("English", KeyEvent.VK_B);
        final JMenuItem skItem = new JMenuItem("Slovensky", KeyEvent.VK_B);
        enItem.setActionCommand("lang-en");
        skItem.setActionCommand("lang-sk");
        enItem.addActionListener(this);
        skItem.addActionListener(this);

        langMenu.add(enItem);
        langMenu.add(skItem);
        menuBar.add(langMenu);

        /*
         * // Layout menu IMenuItem sItem = new IMenuItem(L, "layout-simple",
         * KeyEvent.VK_S); IMenuItem cItem = new IMenuItem(L, "layout-compact",
         * KeyEvent.VK_C); sItem.setActionCommand("layout-simple");
         * cItem.setActionCommand("layout-compact");
         * sItem.addActionListener(this); cItem.addActionListener(this);
         * 
         * layoutMenu.add(sItem); layoutMenu.add(cItem);
         * menuBar.add(layoutMenu);
         */

        for (int i = 0; i < DataStructures.N; ++i) {
            panels[i] = DataStructures.createPanel(i, S);
            if (panels[i] != null) {
                cards.add(panels[i], DataStructures.getName(i));
            }
        }

        getRootPane().setJMenuBar(menuBar);
        container.add(cards);
        showCard(DEFAULT_DS);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final String[] cmd = e.getActionCommand().split("-", 2);

        // set language
        if ("lang".equals(cmd[0])) {
            Languages.selectLanguage(cmd[1]);
        }

        // set layout
        if ("layout".equals(cmd[0])) {
            S.setLayout(cmd[1]);
        }

        // set different data structure
        if ("ds".equals(cmd[0])) {
            for (int i = 0; i < DataStructures.N; ++i) {
                if (DataStructures.getName(i).equals(cmd[1])) {
                    showCard(i);
                    break;
                }
            }
        }
    }

    private void showCard(int i) {
        if (activePanel != -1) {
            panels[activePanel].setOnAir(false);
        }
        activePanel = i;
        panels[i].setOnAir(true);
        cardlayout.show(cards, DataStructures.getName(i));
    }
}
