package algvis.core.history;

import javax.swing.undo.StateEditable;
import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Hashtable;

public class HashtableStoreSupport {
	private static final String nullHash = "47null47";

	/**
	 * Use if want to store null to HashTable. Vlaue is stored only if state does not contain key. 
	 * @param state
	 * @param key
	 * @param value
	 */
	public static void store(Hashtable<Object, Object> state, Object key, Object value) {
		if (!state.containsKey(key)) {
			if (value == null) {
				state.put(key, nullHash);
			} else {
				state.put(key, value);
			}
		}
	}
	
	// TODO store collection support
	public static void storeCollection(Hashtable<Object, Object> state, String key, AbstractSet abstractSet) {
		storeCollection(state, key, (Collection) abstractSet);
	}

	public static void storeCollection(Hashtable<Object, Object> state, String key, AbstractList abstractList) {
		storeCollection(state, key, (Collection) abstractList);
	}

	private static void storeCollection(Hashtable<Object, Object> state, String key, Collection collection) {
		if (collection == null) {
			state.put(key, nullHash);
		} else {
			state.put(key, collection);
			int hash = collection.hashCode();
//			collectionSizes.put(hash, collection.size());
			int i = 0;
			for (Object o : collection) {
				String oKey = key + Integer.toString(i++);
//				String oKey = Integer.toString(o.hashCode());
				store(state, oKey, o);
				// TODO if o instanceof Collection
				if (o instanceof StateEditable) {
					((StateEditable) o).storeState(state);
				}
			}
		}
	}

	/**
	 * Use if obj was stored in HashTable by calling HashtableStoreSupport.store().
	 * @param obj
	 * @return
	 */
	public static Object restore(Object obj) {
		if (nullHash.equals(obj)) {
			return null;
		} else {
			return obj;
		}
	}

	public static void reStoreCollection(AbstractSet abstractSet) {
//		reStoreCollection((Collection) abstractSet);
	}

	public static void reStoreCollection(Hashtable<Object, Object> state, Object key, AbstractList abstractList) {
//		reStoreCollection((Collection) abstractList);
	}

	private static Object reStoreCollection(Hashtable<Object, Object> state, Object key, Collection collection) {
		int i; 
		for (Object o : collection) {
			Object oE = state.get(key);
		}
		
		if (collection == null) {
			state.put(key, nullHash);
		} else {
			state.put(key, collection);
			for (Object o : collection) {
				String oKey = key + Integer.toString(o.hashCode());
				store(state, oKey, o);
				if (o instanceof StateEditable) {
					((StateEditable) o).storeState(state);
				}
			}
		}
		return null;
	}
}
