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

import algvis.core.visual.VisualElement;
import algvis.core.visual.ZDepth;
import algvis.ui.InputField;
import algvis.ui.VisPanel;
import algvis.ui.view.Layout;
import algvis.ui.view.View;

/**
 * Abstract base class for all data structure visualizations.
 * 
 * <p>This class provides the common infrastructure for visualizing data structures,
 * including boundary management, node selection, statistics tracking, and basic
 * operations like insertion and clearing. Each concrete data structure implementation
 * must provide its own drawing logic and specific operation implementations.</p>
 * 
 * <p>The class maintains layout constants for positioning (root position, dimensions,
 * minimum separations) and tracks the visual boundaries of the structure as it grows.</p>
 */
public abstract class DataStructure extends VisualElement {
    // Visual panel associated with this data structure
    public final VisPanel panel;
    
    public static final int rootx = 0;
    public static final int rooty = 0;
    public static final int sheight = 600;
    public static final int swidth = 400;
    public static final int minsepx = 38;
    public static final int minsepy = 30;
    
    // Boundary coordinates of the data structure
    public int x1; // left boundary
    public int x2; // right boundary
    public int y1 = -50; // top boundary
    public int y2; // bottom boundary
    
    public Node chosen = null;
    public static String dsName = null;

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
        runnable.run();
    }

    public void setStats() {
        panel.buttons.setStats(stats());
    }

    public double lg(int x) { // log_2
        return Math.log(x) / Math.log(2);
    }

    public void random(final int n) {
        for (int i = 0; i < n; ++i) {
            insert(MyRandom.Int(InputField.MAX + 1));
        }
    }

    protected void unmark() {
        if (chosen != null) {
            chosen.unmark();
            chosen = null;
        }
    }

    public Layout getLayout() {
        return Layout.COMPACT;
    }
}
