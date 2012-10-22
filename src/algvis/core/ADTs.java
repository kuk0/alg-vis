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

import algvis.ds.intervaltree.IntervalTrees;
import algvis.ds.priorityqueues.MeldablePQ;
import algvis.ds.priorityqueues.PriorityQueue;
import algvis.ds.trie.Trie;
import algvis.ds.unionfind.UnionFind;

/**
 * The Class ADTs. This class contains the list of all abstract data types
 * (ADTs). The menus with data structures are populated from this list. Each ADT
 * should have field adtName with its name (key to resource bundle).
 */
public class ADTs {
	/**
	 * The list of all abstract data types.
	 */
	@SuppressWarnings("rawtypes")
    private static final Class[] ADT = { Dictionary.class, // insert, find, delete
			PriorityQueue.class, // insert, decrease-key, delete-min
			MeldablePQ.class, // insert, decrease-key, delete-min, meld
			UnionFind.class, // make-set, union, find
			Trie.class, // insert, find, delete
			IntervalTrees.class //insert, find sum/min/max of interval, decrease-key
	};
	public static final int N = ADT.length;

	public static String getName(int i) {
		if (i < 0 || i >= ADT.length) {
			System.err.println("ADTs.getName - index out of range.");
			return "";
		}
		String r = "";
		try {
			r = (String) (ADT[i].getDeclaredField("adtName").get(null));
		} catch (Exception e) {
			System.err.println("Unable to get field adtName - name of ADT: "
					+ ADT[i]);
		}
		return r;
	}
}
