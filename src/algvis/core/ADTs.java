package algvis.core;

import algvis.example.ExampleDS;

/**
 * The Class ADTs.
 * This class contains the list of all abstract data types (ADTs).
 * The menus with data structures are populated from this list.
 * Each ADT should have field adtName with its name (key to resource bundle).
 */
public class ADTs {
	/**
	 * The list of all abstract data types.
	 */
	@SuppressWarnings("unchecked")
	static final Class[] ADT = {
		Dictionary.class,    // insert, find, delete
		PriorityQueue.class, // insert, decrease-key, delete-min
		MeldablePQ.class,    // insert, decrease-key, delete-min, meld
		// we should register our new ADT here... in this special case
		// we have the same class for ADT and for the DS...
		// we should also document, which methods are supported by this ADT:
		ExampleDS.class      // move
	};
	static final int N = ADT.length;

	public static String getName(int i) {
		if (i < 0 || i >= ADT.length) {
			System.out.println("ADTs.getName - index out of range.");
			return "";
		}
		String r = "";
		try {
			r = (String) (ADT[i].getDeclaredField("adtName").get(null));
		} catch (Exception e) {
			System.out.println("Unable to get field adtName - name of ADT: "
					+ ADT[i]);
		}
		return r;
	}
}
