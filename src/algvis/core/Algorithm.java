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
package algvis.core;

import java.awt.EventQueue;
import java.awt.geom.Rectangle2D;

import algvis.core.visual.TextBubble;
import algvis.core.visual.VisualElement;
import algvis.internationalization.IParamString;
import algvis.ui.VisPanel;
import algvis.ui.view.REL;

/**
 * Each visualized data structure consists of data and
 * algorithms (such as insert, delete) that update the data. All such algorithms
 * are descendants of the class Algorithm.
 */
abstract public class Algorithm implements Runnable {
    private final VisPanel panel;

    protected Algorithm(VisPanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        begin();
        runAlgorithm();
        end();
    }

    public abstract void runAlgorithm();

    protected void pause() {
        panel.history.nextEdit(panel);
        panel.scene.next();
    }

    protected void setHeader(String s) {
        /*
         * if (!wrapped && !(panel instanceof VisPanel)) {
         * // TODO: just until we get rid of the old VisPanel
         * panel.commentary.setHeader(s);
         * }
         */
    }

    protected void setHeader(String s, String... par) {
        /*
         * if (!wrapped && !(panel instanceof VisPanel)) {
         * panel.commentary.setHeader(s, par);
         * }
         */
    }

    protected void setHeader(String s, int... par) {
        /*
         * if (!wrapped && !(panel instanceof VisPanel)) {
         * panel.commentary.setHeader(s, par);
         * }
         */
    }

    protected void addNote(String s) {
        /*
         * if (!(panel instanceof VisPanel)) {
         * panel.commentary.addNote(s);
         * }
         */
    }

    public void addNote(String s, String[] par) {
        /*
         * if (!(panel instanceof VisPanel)) {
         * panel.commentary.addNote(s, par);
         * }
         */
    }

    protected void addNote(String s, int... par) {
        /*
         * if (!(panel instanceof VisPanel)) {
         * panel.commentary.addNote(s, par);
         * }
         */
    }

    protected void addStep(Node v, REL pos, String s, String... par) {
        addToSceneUntilNext(new TextBubble(new IParamString(s, par),
            pos.boundaryPoint(v.getDestBoundingBox()), 200, pos));
    }

    protected void addStep(VisualElement v, int w, REL pos, String s,
        String... par) {
        addStep(v.getBoundingBox(), w, pos, s, par);
    }

    protected void addStep(Rectangle2D v, int w, REL pos, String s,
        String... par) {
        addToSceneUntilNext(new TextBubble(new IParamString(s, par),
            pos.boundaryPoint(v), w, pos));
    }

    protected void addToScene(VisualElement element) {
        panel.scene.add(element);
        panel.history.addToPreState(element);
    }

    protected void addToSceneUntilNext(VisualElement element) {
        panel.scene.addUntilNext(element);
        panel.history.addToPreState(element);
    }

    protected void removeFromScene(VisualElement element) {
        if (element == null) {
            return;
        }
        // if (panel.pauses) {
        panel.scene.remove(element);
        // } else {
        // element.removeFromSceneNow();
        // }
    }

    void begin() {
        panel.history.firstEdit(panel);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.buttons.refresh();
            }
        });
    }

    void end() {
        panel.D.setStats();
        panel.history.finishEdits();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                panel.refresh();
            }
        });
    }
}
