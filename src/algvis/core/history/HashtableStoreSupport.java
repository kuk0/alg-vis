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

package algvis.core.history;

import java.util.Hashtable;

public class HashtableStoreSupport {
	private static final Object nullObject = new Object();

	/**
	 * Stores the (key, value) pair in the state.
	 * Value may be null and it is stored only if state contains the key.
	 * 
	 * @param state
	 * @param key
	 * @param value
	 */
	public static void store(Hashtable<Object, Object> state, Object key,
			Object value) {
		if (!state.containsKey(key)) {
			if (value == null) {
				state.put(key, nullObject);
			} else {
				state.put(key, value);
			}
		}
	}

	// TODO store collection support
	// public static void storeCollection(Hashtable<Object, Object> state,
	// String key, AbstractSet abstractSet) {
	// storeCollection(state, key, (Collection) abstractSet);
	// }
	//
	// public static void storeCollection(Hashtable<Object, Object> state,
	// String key, AbstractList abstractList) {
	// storeCollection(state, key, (Collection) abstractList);
	// }
	//
	// private static void storeCollection(Hashtable<Object, Object> state,
	// String key, Collection collection) {
	// if (collection == null) {
	// state.put(key, nullObject);
	// } else {
	// state.put(key, collection);
	// int hash = collection.hashCode();
	// collectionSizes.put(hash, collection.size());
	// int i = 0;
	// for (Object o : collection) {
	// String oKey = key + Integer.toString(i++);
	// String oKey = Integer.toString(o.hashCode());
	// store(state, oKey, o);
	// // TODO if o instanceof Collection
	// if (o instanceof StateEditable) {
	// ((StateEditable) o).storeState(state);
	// }
	// }
	// }
	// }

	/**
	 * Converts the nullObject into null.
	 * 
	 * @param obj
	 * @return obj or null
	 */
	public static Object restore(Object obj) {
		return nullObject.equals(obj) ? null : obj;
	}

	// public static void reStoreCollection(AbstractSet abstractSet) {
	// reStoreCollection((Collection) abstractSet);
	// }
	//
	// public static void reStoreCollection(Hashtable<Object, Object> state,
	// Object key, AbstractList abstractList) {
	// reStoreCollection((Collection) abstractList);
	// }
	//
	// private static Object reStoreCollection(Hashtable<Object, Object> state,
	// Object key, Collection collection) {
	// int i;
	// for (Object o : collection) {
	// Object oE = state.get(key);
	// }
	//
	// if (collection == null) {
	// state.put(key, nullObject);
	// } else {
	// state.put(key, collection);
	// for (Object o : collection) {
	// String oKey = key + Integer.toString(o.hashCode());
	// store(state, oKey, o);
	// if (o instanceof StateEditable) {
	// ((StateEditable) o).storeState(state);
	// }
	// }
	// }
	// return null;
	// }
}
