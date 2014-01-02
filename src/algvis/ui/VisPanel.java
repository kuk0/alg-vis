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
package algvis.ui;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.core.history.HistoryManager;
import algvis.core.visual.Scene;
import algvis.internationalization.ILabel;
import algvis.internationalization.LanguageListener;
import algvis.internationalization.Languages;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.undo.StateEditable;

public abstract class VisPanel extends JPanel implements LanguageListener,
    StateEditable {
    private static final long serialVersionUID = 5104769085118210624L;
    public static Class<? extends DataStructure> DS;

    // aplet pozostava z piatich zakladnych veci:
    public Buttons buttons; // gombikov (dolu)
    public Commentary commentary; // komentara (vpravo)
    public DataStructure D; // datovej struktury
    public Screen screen; // obrazovky v strede
    public final Scene scene = new Scene();
    public ILabel statusBar; // a status baru
    public final Settings S;
    private TitledBorder border;

    public volatile boolean pauses = true;
    public boolean small = false;
    public ExecutorService algorithmPool = Executors.newSingleThreadExecutor();
    public final HistoryManager history = new HistoryManager(scene);
    private boolean started = false;

    protected VisPanel(Settings S) {
        this.S = S;
        init();
    }

    protected void init() {
        setLayout(new BorderLayout(0, 0));
        final JPanel screenP = initScreen();
        final JScrollPane commentary = initCommentary();
        statusBar = new ILabel("EMPTYSTR");
        initDS();

        add(screenP, BorderLayout.CENTER);
        add(commentary, BorderLayout.LINE_END);

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

    private JScrollPane initCommentary() {
        // Commentary
        final JScrollPane SP = new JScrollPane() {
            private static final long serialVersionUID = -8618469733328277117L;

            @Override
            public Dimension getMaximumSize() {
                return new Dimension(400, 680);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 600);
            }

            @Override
            public Dimension getMinimumSize() {
                return new Dimension(250, 600);
            }
        };
        commentary = new Commentary(this, SP);
        SP.setViewportView(commentary);
        // JPanel CP = new JPanel();
        // CP.add(SP);
        SP.setBorder(BorderFactory.createTitledBorder(""));
        return SP;
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
        commentary.refresh();
    }

    @Override
    public void storeState(Hashtable<Object, Object> state) {
        buttons.storeState(state);
        commentary.storeState(state);
        scene.storeState(state);
    }

    @Override
    public void restoreState(Hashtable<?, ?> state) {
        buttons.restoreState(state);
        commentary.restoreState(state);
        scene.restoreState(state);
    }

    public void newAlgorithmPool() {
        algorithmPool.shutdownNow();
        algorithmPool = Executors.newSingleThreadExecutor();
    }
}
