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

package algvis.tests;

import algvis.ds.dictionaries.bst.BST;

public class BSTTest {

    public static boolean testSmall(BST T) {
        final int MAX = 3;
        int[] ins = new int[MAX];
        int[] del = new int[MAX];
        for (int n = 1; n <= MAX; ++n) {
            ins[n] = del[n] = n;
            if (true) {   // for all permutations
                //BST T = new BST();
                for (int i = 0; i < n; ++i) {
                    T.insert(ins[i]);
                }
                if (!T.getRoot().testOrder()) {
                    return false;
                }
                if (!T.getRoot().testStructure()) {
                    return false;
                }
                for (int i = 0; i < n; ++i) {
                    T.delete(del[i]);
                }
                if (T.getRoot() != null) {
                    return false;
                }
            }
        }
        return true;
    }
}
