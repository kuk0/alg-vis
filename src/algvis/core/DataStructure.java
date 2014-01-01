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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import algvis.core.visual.VisualElement;
import algvis.core.visual.ZDepth;
import algvis.ui.InputField;
import algvis.ui.VisPanel;
import algvis.ui.view.Layout;
import algvis.ui.view.View;

abstract public class DataStructure extends VisualElement {
    // datova struktura musi vediet gombikom povedat, kolko ich potrebuje,
    // kolko ma vstupov, ake to su a co treba robit
    public Algorithm A = null;
    public final VisPanel panel;
    public static final int rootx = 0, rooty = 0, sheight = 600, swidth = 400,
        minsepx = 38, minsepy = 30;
    public int x1, x2, y1 = -50, y2;
    public Node chosen = null;
    public static String adtName = "";
    public static String dsName = "";

    protected DataStructure(VisPanel panel) {
        super(ZDepth.DS);
        this.panel = panel;
    }

    abstract public String getName();

    abstract public String stats();

    abstract public void insert(int x);

    abstract public void clear();

    @Override
    abstract public void draw(View v);

    public void start(Runnable runnable) {
        unmark();
        final Future result = panel.algorithmPool.submit(runnable);
        // TODO only for debugging purposes:
        // mozno to trochu spomali vkladanie vrcholov, ale bez tohto by sa nedal
        // najst bug v algoritmoch (nevypisal
        // by sa ziadny exception)
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    result.get();
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                } catch (final ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public void setStats() {
        panel.buttons.setStats(stats());
    }

    /*
     * protected void showStatus(String t) { panel.showStatus(t); }
     */

    public double lg(int x) { // log_2
        return Math.log(x) / Math.log(2);
    }

    public void random(final int n) {
        final boolean p = panel.pauses;
        panel.pauses = false;
        for (int i = 0; i < n; ++i) {
            insert(MyRandom.Int(InputField.MAX + 1));
        }
        start(new Runnable() {
            @Override
            public void run() {
                panel.pauses = p;
            }
        });
    }

    void unmark() {
        if (chosen != null) {
            chosen.unmark();
            chosen = null;
        }
    }

    public Layout getLayout() {
        return panel.S.layout;
    }

    public Algorithm getA() {
        return A;
    }
}
