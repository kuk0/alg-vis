package algvis.ds;

import algvis.core.DataStructure;
import algvis.core.Dictionary;
import algvis.ds.dynamicarray.DynamicArray;
import algvis.ds.intervaltree.IntervalTrees;
import algvis.ds.priorityqueues.MeldablePQ;
import algvis.ds.priorityqueues.PriorityQueue;
import algvis.ds.trie.Trie;
import algvis.ds.unionfind.UnionFind;

public enum ADT {
    DICTIONARY(Dictionary.class),        // insert, find, delete
    PRIORITY_QUEUE(PriorityQueue.class), // insert, decrease-key, delete-min
    MELDABLE_PQ(MeldablePQ.class),       // insert, decrease-key, delete-min, meld
    UNION_FIND(UnionFind.class),         // make-set, union, find
    TRIE(Trie.class),                    // insert, find, delete
    INTERVAL_TREE(IntervalTrees.class),  // insert, find sum/min/max of interval, decrease-key
    DYNAMIC_ARRAY(DynamicArray.class);   // push, pop

    public final Class<? extends DataStructure> c;

    private ADT(Class<? extends DataStructure> c) {
        this.c = c;
    }

    public String getName() {
        try {
            return (String) (c.getDeclaredField("adtName").get(null));
        } catch (final Exception e) {
            System.err.println("Unable to get adtName for: " + c);
            return null;
        }
    }
}
