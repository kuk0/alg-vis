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

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.undo.StateEditable;

import algvis.core.DataStructure;
import algvis.core.history.HistoryManager;
import algvis.core.visual.Scene;
import algvis.internationalization.ILabel;
import algvis.internationalization.LanguageListener;
import algvis.internationalization.Languages;

public abstract class VisPanel extends JPanel implements LanguageListener,
    StateEditable {
    private static final long serialVersionUID = -5866649744399813386L;

    // aplet pozostava z piatich zakladnych veci:
    public Buttons buttons; // gombikov (dolu)
    public DataStructure D; // datovej struktury
    public Screen screen; // obrazovky v strede
    public final Scene scene = new Scene();
    public ILabel statusBar; // a status baru
    private TitledBorder border;

    public volatile boolean pauses = true;
    public boolean small = false;
    public final HistoryManager history = new HistoryManager(scene);
    private boolean started = false;

    protected VisPanel() {
        init();
    }

    protected void init() {
        setLayout(new BorderLayout(0, 0));
        final JPanel screenP = initScreen();
        statusBar = new ILabel("EMPTYSTR");
        initDS();

        add(screenP, BorderLayout.CENTER);
        JPanel buttonsAndStatusBar = new JPanel(new BorderLayout());
        buttonsAndStatusBar.add(buttons, BorderLayout.CENTER);
        buttonsAndStatusBar.add(statusBar, BorderLayout.PAGE_END);
        add(buttonsAndStatusBar, BorderLayout.PAGE_END);

        screen.setDS(D);
        languageChanged();
    }

    public void start() {
        started = true;
        screen.start();
        buttons.I.requestFocusInWindow();
    }

    protected JPanel initScreen() {
        screen = new Screen(this);
        final JPanel screenP = new JPanel(new BorderLayout());
        screenP.add(screen, BorderLayout.CENTER);

        border = BorderFactory.createTitledBorder("");
        border.setTitleJustification(TitledBorder.CENTER);
        border.setTitleFont(new Font("Sans-serif", Font.ITALIC, 12));
        Languages.addListener(this);
        screenP.setBorder(BorderFactory.createCompoundBorder(border,
            BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        return screenP;
    }

    protected abstract void initDS();

    /*
     * public void showStatus (String t) { statusBar.setT(t); }
     */

    @Override
    public void languageChanged() {
        border.setTitle("    " + Languages.getString(D.getName()) + "    ");
    }

    public void setOnAir(boolean onAir) {
        if (!onAir) {
            screen.suspend();
        } else {
            if (!started) {
                start();
            }
            screen.resume();
        }
    }

    public void refresh() {
        buttons.refresh();
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        buttons.storeState(state);
        scene.storeState(state);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        buttons.restoreState(state);
        scene.restoreState(state);
    }
}
