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

package algvis.ds.intervaltree;

import algvis.ui.InputField;

public class IntervalChangeKey extends IntervalAlg {
    private final int value;

    public IntervalChangeKey(IntervalTree T, IntervalNode v, int value) {
        super(T);
        this.v = v;
        this.value = value;
    }

    @Override
    public void runAlgorithm() {
        setHeader("changekey");

        v.setKey(value);
        if (v.getKey() < 1) {
            v.setKey(1);
        }
        if (v.getKey() > InputField.MAX) {
            v.setKey(InputField.MAX);
        }

        addNote("intervalchangev");
        v.mark();
        pause();
        v.unmark();
        v = v.getParent();
        adjustValues(v);
        addNote("done");
    }
}
