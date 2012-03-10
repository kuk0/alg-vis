package algvis.daryheap;

import algvis.core.ClickListener;
import algvis.core.Node;
import algvis.core.PriorityQueue;
import algvis.core.View;
import algvis.core.VisPanel;

public class DaryHeap extends PriorityQueue implements ClickListener{
	public static String dsName = "daryheap";
	DaryHeapNode root = null, v = null, v2 = null;
	DaryHeapNode last = null;
	int order = 5;

	public DaryHeap(VisPanel M) {
		super(M, dsName);
		last = new DaryHeapNode(this, 47); 
		M.screen.V.setDS(this);
		DaryHeap.minsepx = 30;  //zmenit na mensie
	}

	@Override
	public void mouseClicked(int x, int y) {
		if (root == null)
			return;
		DaryHeapNode v = root.find(x, y);
		if (v != null) {
			if (v.marked) {
				v.unmark();
				chosen = null;
			} else {
				if (chosen != null)
					chosen.unmark();
				v.mark();
				chosen = v;
			}
		}
	}

	@Override
	public void insert(int x) {
		start(new DaryHeapInsert(this, x));
		
	}

	@Override
	public void delete() {
		start(new DaryHeapDelete(this));
		
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		if (v == null) {
		// TODO: vypindat
		} else {
			start(new DaryHeapDecrKey(this, (DaryHeapNode) v, delta));
		}
	}

	@Override
	public String getName() {
		return dsName;
	}

	@Override
	public String stats() {

		if (root == null) {
			return M.S.L.getString("size") + ": 0 ("
					+ M.S.L.getString("emptyheap") + ")";
		} else if (root.nnodes == 1000) {
			return M.S.L.getString("size") + ": 1000 ("
					+ M.S.L.getString("fullheap") + ")";
		} else {
			return M.S.L.getString("size") + ": " + root.nnodes;
		}
	}

	@Override
	public void clear() {
		root = v = null;
		setStats();
		
	}

	@Override
	public void draw(View V) {
		if (root != null) {
			root.moveTree();
			root.drawTree(V);
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
		if (v2 != null) {
			v2.move();
			v2.draw(V);
		}
		
	}
	
	public void reposition() {
		if (root != null) {
			root._reposition();
			M.screen.V.setBounds(x1, y1, x2, y2);
		}
	}

	public int getOrder(){
		return this.order;
	}

}