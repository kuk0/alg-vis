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
package algvis.ds.priorityqueues.skewheap;

import algvis.core.Algorithm;
import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ds.priorityqueues.MeldablePQButtons;
import algvis.ds.priorityqueues.MeldablePQButtonsNoDecr;
import algvis.ui.VisPanel;

public class SkewHeapPanel extends VisPanel {
    private static final long serialVersionUID = -2947713003292797010L;
    public static Class<? extends DataStructure> DS = SkewHeap.class;

    public SkewHeapPanel(Settings S) {
        super(S);
    }

    @Override
    public void initDS() {
        D = new SkewHeap(this);
        scene.add(D);
        buttons = new MeldablePQButtonsNoDecr(this);
    }

    @Override
    public void start() {
        super.start();
        ((SkewHeap) D).active = 1;
        D.random(13);
        D.start(new Algorithm(this) {
            @Override
            public void runAlgorithm() {
                ((MeldablePQButtons) buttons).activeHeap.setValue(2);
            }
        });
        D.random(10);
        D.start(new Algorithm(this) {
            @Override
            public void runAlgorithm() {
                ((MeldablePQButtons) buttons).activeHeap.setValue(3);
            }
        });
        D.random(7);
        D.start(new Algorithm(this) {
            @Override
            public void runAlgorithm() {
                ((MeldablePQButtons) buttons).activeHeap.setValue(1);
            }
        });
        screen.V.resetView();
    }
}
