package algvis.ds.intervaltree;

import algvis.core.Node;
import algvis.core.visual.ZDepth;
import algvis.ds.intervaltree.IntervalNode.focusType;
import algvis.ds.intervaltree.IntervalTrees.mimasuType;

import java.util.HashMap;

public class IntervalFindMin extends IntervalAlg {
	private final int i;
    private final int j;
	private IntervalNode maxi;
	private static final int ninf = -2147483648;
	private static final int pinf = 2147483647;

	public IntervalFindMin(IntervalTree T, int i, int j) {
		super(T);
		this.T = T;
		if (i > j) {
			int tmp = j;
			j = i;
			i = tmp;
		}
		if (i < 1 || i > T.numLeafs) {
			i = 1;
		}
		if (j > T.numLeafs || j < 1) {
			j = T.numLeafs;
		}
		this.i = i;
		this.j = j;
		if (T.minTree == mimasuType.MAX) {
			maxi = new IntervalNode(T, ninf, ZDepth.NODE);
			setHeader("findmax", i, j);
		} else if (T.minTree == mimasuType.MIN) {
			maxi = new IntervalNode(T, pinf,ZDepth.NODE);
			setHeader("findmin", i, j);
		} else {
			maxi = new IntervalNode(T, 0,ZDepth.NODE);
			setHeader("findsum", i, j);
		}
		T.markColor(T.root, i, j);
		// System.out.println(i + " " + j);
		// System.out.println(T.root.b + " " + T.root.e);
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		// kazdy vrchol ma zapamatany interval, ktory reprezentuje (je to
		// b-e+1=2^k

		if (T.root != null) {
			// We have to find the nodes that represent the
			// interval &lt;#1,#2&gt;. We will search for these nodes with DFS.
			// Budeme h�ada� vrcholy, ktor� reprezentuj� interval &lt;#1,#2&gt
			// pomocou DFS.
			addNote("intervalfind", i, j); // vysvetlenie
			find(T.root, i, j);
			pause();
			if (T.minTree == mimasuType.MAX) {
				addStep("maximum", maxi.getKey());
			} else if (T.minTree == mimasuType.MIN) {
				addStep("minimum", maxi.getKey());
			} else if (T.minTree == mimasuType.SUM) {
				addStep("sumimum", maxi.getKey());
			}
			if (T.minTree != mimasuType.SUM) {
				T.unfocus(T.root);
				maxi.mark();
				T.markColor(T.root, i, j);
			}
			pause();
			// if (T.minTree == mimasuType.SUM){
			// }
			T.unfocus(T.root);
			addNote("done");
		} else {
			// addNote(); //strom je prazdny/zly interval
		}
	}

	@Override
	public HashMap<String, Object> getResult() {
		return null;
	}

	void find(IntervalNode w, int b, int e) throws InterruptedException {

		w.mark();
		// w.markSubtree = true;

		if ((w.b > e) || (w.e < b)) {
			if (w.getKey() != Node.NOKEY) {
				addStep("intervalout", i, j, w.getKey(), w.b, w.e); // mimo
																	// intervalu
			} else {
				addStep("intervalempty", w.b, w.e); // prazdny vrchol
			}
			w.focused = focusType.TOUT;
			pause();
			w.unmark();
			w.focused = focusType.FALSE;
			return;
		}

		if ((w.b >= b) && (w.e <= e)) {
			if (T.minTree != mimasuType.SUM) {
				if (w.prec(maxi)) {
					maxi = w;
				}
			} else {
				maxi.setKey(maxi.getKey() + w.getKey());
			}
			addStep("intervalin", i, j, w.getKey(), w.b, w.e); // dnu intervalu
			w.focused = focusType.TIN;
			pause();
			// w.unmark();
			// w.unfocus();
			return;
		}

		if ((w.b <= b) || (w.e >= e)) {
			addStep("intervalpart", i, j, w.getKey(), w.b, w.e); // neprazdny prienik
			w.focused = focusType.TOUT;
			pause();
			w.focused = focusType.TWAIT;
			find(w.getLeft(), b, e);
			find(w.getRight(), b, e);
		}
		w.unmark();
		w.focused = focusType.FALSE;
	}
}
