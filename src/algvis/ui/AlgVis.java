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

import algvis.core.Settings;
import algvis.ds.ADT;
import algvis.ds.DS;
import algvis.internationalization.IMenu;
import algvis.internationalization.IMenuItem;
import algvis.internationalization.Languages;

public class AlgVis extends JPanel implements ActionListener {

    private static final long serialVersionUID = -5202486006824196688L;
    private final static DS DEFAULT_DS = DS.BST;

    /** Cards with data structures */
    public final JPanel cards;
    private final CardLayout cardlayout;
    private DS activePanel = null;
    private final Container container;
    private final Settings S;

    private final Map<String, IMenu> adtItems = new HashMap<String, IMenu>();
    private final Map<DS, VisPanel> panels = new HashMap<DS, VisPanel>();

    public AlgVis(Container c) {
        this(c, "en");
    }

    public AlgVis(Container c, String s) {
        this.container = c;
        Languages.selectLanguage(s);
        S = new Settings();
        cards = new JPanel(cardlayout = new CardLayout());
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
        for (ADT t : ADT.values()) {
            final String adtName = t.getName();
            IMenu m = new IMenu(adtName);
            adtItems.put(adtName, m);
            dsMenu.add(m);
        }
        /**
         * Create menu items for each data structure listed in the class
         * DataStructures.
         */
        for (DS s : DS.values()) {
            IMenuItem itm = new IMenuItem(s.getName());
            adtItems.get(s.getADT()).add(itm);
            itm.setActionCommand("ds-" + s.getName());
            itm.addActionListener(this);
            dsMenu.add(adtItems.get(s.getADT()));
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

        for (DS s : DS.values()) {
            panels.put(s, s.createPanel(S));
            cards.add(s.getName(), panels.get(s));
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
            for (DS s : DS.values()) {
                if (s.getName().equals(cmd[1])) {
                    showCard(s);
                    break;
                }
            }
        }
    }

    private void showCard(DS s) {
        if (activePanel != null) {
            panels.get(activePanel).setOnAir(false);
        }
        activePanel = s;
        panels.get(s).setOnAir(true);
        cardlayout.show(cards, s.getName());
    }
}
