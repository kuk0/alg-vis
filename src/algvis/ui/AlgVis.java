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
import java.util.Vector;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import algvis.core.Pair;
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

    private final Map<DS, VisPanel> panels = new HashMap<>();

    private final static Vector<Object> DS_MENU = vec(
        sop("dictionary",
            vec(DS.BST, DS.ROTATION, DS.AVL_TREE, DS.A23, DS.A234, DS.B_TREE,
                DS.RB_TREE, DS.AA_TREE, DS.TREAP, DS.SKIPLIST, DS.GB_TREE,
                DS.SPLAY_TREE)), //
        sop("pq", vec(DS.HEAP, DS.DARY_HEAP)), //
        sop("meldable-pq",
            vec(DS.LEFTIST_HEAP, DS.SKEW_HEAP, DS.PAIRING_HEAP, DS.BIN_HEAP,
                DS.LAZY_BIN_HEAP, DS.FIB_HEAP)), //
        sop("ufa", vec(DS.UNION_FIND)), //
        sop("stringology", vec(DS.TRIE, DS.SUFFIX_TREE)), //
        sop("intervaltrees", vec(DS.INTERVAL_TREE)), //
        sop("dynamicarray", vec(DS.DYN_ARRAY)) //
    );

    public AlgVis(Container c) {
        this(c, "en");
    }

    public AlgVis(Container c, String s) {
        this.container = c;
        Languages.selectLanguage(s);
        cards = new JPanel(cardlayout = new CardLayout());
    }

    @SuppressWarnings("unchecked")
    private void menuFactory(IMenu m, Vector<Object> items) {
        for (Object p : items) {
            if (p instanceof Pair) {
                String s = ((Pair<String, Vector<Object>>) p).first;
                Vector<Object> v = ((Pair<String, Vector<Object>>) p).second;
                IMenu sub = new IMenu(s);
                m.add(sub);
                menuFactory(sub, v);
            } else if (p instanceof DS) {
                String n = ((DS) p).getName();
                IMenuItem itm = new IMenuItem(n);
                itm.setActionCommand("ds-" + n);
                itm.addActionListener(this);
                m.add(itm);
            } else {
                // this shouldn't happen
            }
        }

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

        menuFactory(dsMenu, DS_MENU);
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
            panels.put(s, s.createPanel());
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

    private static Pair<String, Object> sop(String x, Object y) {
        return new Pair<>(x, y);
    }

    @SafeVarargs
    private static Vector<Object> vec(Object... objs) {
        Vector<Object> v = new Vector<>(objs.length);
        for (Object o : objs) {
            v.add(o);
        }
        return v;
    }
}
