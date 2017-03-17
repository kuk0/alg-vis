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

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.ui.VisPanel;

public abstract class IntervalTrees extends DataStructure {
    public static String adtName = "intervaltrees";

    public enum mimasuType {
        MIN, MAX, SUM
    }

    public mimasuType minTree = mimasuType.MAX;

    IntervalTrees(VisPanel M) {
        super(M);
    }

    @Override
    abstract public void insert(int x);

    abstract public void changeKey(Node v, int value);

    abstract public void ofinterval(int b, int e);

}
