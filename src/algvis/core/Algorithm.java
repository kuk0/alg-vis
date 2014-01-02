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

import algvis.core.history.UpdatableStateEdit;
import algvis.core.visual.DoubleArrow;
import algvis.core.visual.Edge;
import algvis.core.visual.ShadePair;
import algvis.core.visual.ShadeSubtree;
import algvis.core.visual.ShadeTriple;
import algvis.core.visual.TextBubble;
import algvis.core.visual.VisualElement;
import algvis.internationalization.IIntParamString;
import algvis.internationalization.IParamString;
import algvis.internationalization.IString;
import algvis.ui.NewVisPanel;
import algvis.ui.VisPanel;
import algvis.ui.view.REL;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * The Class Algorithm. Each visualized data structure consists of data and
 * algorithms (such as insert, delete) that update the data. All such algorithms
 * are descendants of the class Algorithm.
 * <p/>
 * A visualized algorithm has its own thread which can be suspended (e.g., after
 * each step of the algorithm; see method pause) and is automatically resumed
 * (method myresume) after pressing the "Next" button.
 */
abstract public class Algorithm implements Runnable {
    private final VisPanel panel;
    private final Semaphore gate = new Semaphore(1);
    private volatile boolean done = false;
    private UpdatableStateEdit panelState;
    private boolean wrapped = false;
    private Algorithm wrapperAlg;

    protected Algorithm(VisPanel panel) {
        this.panel = panel;
        wrapperAlg = null;
        wrapped = false;
        try {
            gate.acquire();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected Algorithm(VisPanel panel, Algorithm a) {
        this(panel);
        wrapperAlg = a;
        wrapped = (a != null);
    }

    @Override
    public void run() {
        panel.D.A = this;
        begin();
        try {
            runAlgorithm();
        } catch (final InterruptedException e) {
            this.done = true;
            panel.history.trimToEnd();
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    panel.refresh();
                }
            });
            // e.printStackTrace();
            return;
        }
        end();
    }

    public abstract void runAlgorithm() throws InterruptedException;

    protected void pause() throws InterruptedException {
        if (wrapped) {
            wrapperAlg.pause();
        } else {
            //System.out.println("panel state end");
            panelState.end();
            if (panel.pauses) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        panel.refresh();
                    }
                });
                gate.acquire();
            }
            //System.out.println("new panel state");
            panelState = new UpdatableStateEdit(panel,
                panel.history.getNextId());
            panel.history.addEdit(panelState);
            panel.scene.next();
        }
    }

    public void resume() {
        if (wrapped) {
            wrapperAlg.resume();
        } else {
            gate.release();
        }
    }

    protected void setHeader(String s) {
        if (!wrapped && !(panel instanceof NewVisPanel)) { // TODO: just until we get rid of the old VisPanel
            panel.commentary.setHeader(s);
        }
    }

    protected void setHeader(String s, String... par) {
        if (!wrapped && !(panel instanceof NewVisPanel)) {
            panel.commentary.setHeader(s, par);
        }
    }

    protected void setHeader(String s, int... par) {
        if (!wrapped && !(panel instanceof NewVisPanel)) {
            panel.commentary.setHeader(s, par);
        }
    }

    protected void addNote(String s) {
        if (!(panel instanceof NewVisPanel)) {
            panel.commentary.addNote(s);
        }
    }

    public void addNote(String s, String[] par) {
        if (!(panel instanceof NewVisPanel)) {
            panel.commentary.addNote(s, par);
        }
    }

    protected void addNote(String s, int... par) {
        if (!(panel instanceof NewVisPanel)) {
            panel.commentary.addNote(s, par);
        }
    }

    protected void addStep(int x, int y, int w, REL pos, String s) {
        addToSceneUntilNext(new TextBubble(new IString(s), x, y, w, pos));
    }

    protected void addStep(int x, int y, int w, REL pos, String s,
        String... par) {
        addToSceneUntilNext(new TextBubble(new IParamString(s, par), x, y, w,
            pos));
    }

    protected void addStep(int x, int y, int w, REL pos, String s, int... par) {
        addToSceneUntilNext(new TextBubble(new IIntParamString(s, par), x, y,
            w, pos));
    }

    protected void addStep(Node v, REL pos, String s) {
        addToSceneUntilNext(new TextBubble(new IString(s), v.tox, v.toy, 200,
            pos));
    }

    protected void addStep(Node v, REL pos, String s, String... par) {
        addToSceneUntilNext(new TextBubble(new IParamString(s, par), v.tox,
            v.toy, 200, pos));
    }

    protected void addStep(Node v, REL pos, String s, int... par) {
        addToSceneUntilNext(new TextBubble(new IIntParamString(s, par), v.tox,
            v.toy, 200, pos));
    }

    protected void addStep(String s) {
        if (!(panel instanceof NewVisPanel)) {
            panel.commentary.addStep(s);
        }
    }

    protected void addStep(String s, String... par) {
        if (!(panel instanceof NewVisPanel)) {
            panel.commentary.addStep(s, par);
        }
    }

    protected void addStep(String s, int... par) {
        if (!(panel instanceof NewVisPanel)) {
            panel.commentary.addStep(s, par);
        }
    }

    protected void addToScene(VisualElement element) {
        if (wrapped) {
            wrapperAlg.addToScene(element);
        } else {
            if (!panel.pauses
                && (element instanceof ShadeSubtree
                    || element instanceof ShadePair
                    || element instanceof ShadeTriple
                    || element instanceof DoubleArrow
                    || element instanceof Edge || element instanceof TextBubble))
                return;
            panel.scene.add(element);
            panelState.addToPreState(element);
        }
    }

    protected void addToSceneUntilNext(VisualElement element) {
        if (wrapped) {
            wrapperAlg.addToSceneUntilNext(element);
        } else {
            if (!panel.pauses
                && (element instanceof ShadeSubtree
                    || element instanceof ShadePair
                    || element instanceof ShadeTriple
                    || element instanceof DoubleArrow
                    || element instanceof Edge || element instanceof TextBubble))
                return;
            panel.scene.addUntilNext(element);
            panelState.addToPreState(element);
        }
    }

    protected void removeFromScene(VisualElement element) {
        if (element == null)
            return;
        if (!panel.pauses
            && (element instanceof ShadeSubtree || element instanceof ShadePair
                || element instanceof ShadeTriple
                || element instanceof DoubleArrow || element instanceof Edge || element instanceof TextBubble))
            return;
        // if (panel.pauses) {
        panel.scene.remove(element);
        // } else {
        // element.removeFromSceneNow();
        // }
    }

    void begin() {
        panelState = new UpdatableStateEdit(panel, panel.history.getNextId());
        panel.history.addEdit(panelState);
        if (!(panel instanceof NewVisPanel)) {
            panel.commentary.clear();
        }

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.buttons.refresh();
            }
        });
    }

    void end() {
        panel.D.setStats();
        panelState.end();
        panel.history.putAlgorithmEnd();

        this.done = true;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.refresh();
            }
        });
    }

    public boolean isDone() {
        return done;
    }

    public HashMap<String, Object> getResult() {
        return null;
    }
}
