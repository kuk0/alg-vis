package algvis.skewheap;

import algvis.core.ClickListener;
import algvis.core.MeldablePQ;
import algvis.core.MeldablePQButtons;
import algvis.core.Node;
import algvis.core.Pair;
import algvis.core.StringUtils;
import algvis.core.View;
import algvis.core.VisPanel;

public class SkewHeap extends MeldablePQ implements ClickListener{
	public static String dsName = "skewheap";
	SkewHeapNode root[] = null, v = null, v2 = null;
	
	public SkewHeap(VisPanel M) {
		super(M, dsName);
		root = new SkewHeapNode[numHeaps + 1];
		M.screen.V.setDS(this);
	}	
	
	@Override
	public void highlight(int i) {
		active = i;
		if (root[active] != null) {
			root[active].highlightTree();
		}		
	}

	@Override
	public void lowlight() {
		if (root[active] != null) {
			root[active].lowlightTree();
		}		
	}

	@Override
	public void mouseClicked(int x, int y) {
		int h = 0;
		SkewHeapNode v = null;
		for (int i = 1; i <= numHeaps; ++i) {
			if (root[i] != null) {
				v = (SkewHeapNode) root[i].find(x, y);
				if (v != null) {
					h = i;
					break;
				}
			}
		}
		if (v != null) {
			if (v.marked) {
				v.unmark();
				chosen = null;
			} else {
				if (chosen != null)
					chosen.unmark();
				if (h == active) {
					v.mark();
					chosen = v;
				} else {
					((MeldablePQButtons) M.B).activeHeap.setValue(h);
					// lowlight();
					// highlight(h);
				}
			}
		}
		
	}

	@Override
	public void insert(int x) {
		start(new SkewHeapInsert(this, active, x));
		
	}

	@Override
	public void delete() {
		start(new SkewHeapDelete(this, active));
		
	}
	protected Pair chooseHeaps(int i, int j) {
		if (i < 1 || i > numHeaps) {
			i = -1;
		}
		if (j < 1 || j > numHeaps) {
			j = -1;
		}
		if (i == -1 || j == -1) {
			j = i;
			i = active;
			if (i == j || j == -1) {
				j = (active == 1) ? 2 : 1;
				for (int k = 0; k <= numHeaps; ++k) {
					if (k != active && root[k] != null) {
						j = k;
						break;
					}
				}
			}
		}
		return new Pair(i, j);
	}

	@Override
	public void meld(int i, int j) {
		Pair p = chooseHeaps(i, j);
		i = p.first;
		j = p.second;
		((MeldablePQButtons) M.B).activeHeap.setValue(i);
		start(new SkewHeapMeld(this, i, j));
		
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		if (v == null) {
		} else {
			start(new SkewHeapDecrKey(this, (SkewHeapNode) v, delta));
		}
		
	}

	@Override
	public String getName() {
		return "skewheap";
	}

	@Override
	public String stats() {

		if (root[active] == null) {
			return M.S.L.getString("size") + ": 0;   "
					+ M.S.L.getString("height") + ": 0 =  1.00\u00b7"
					+ M.S.L.getString("opt") + ";   "
					+ M.S.L.getString("avedepth") + ": 0";
		} else {
			root[active].calcTree();
			return M.S.L.getString("size")
					+ ": "
					+ root[active].size
					+ ";   "
					+ M.S.L.getString("height")
					+ ": "
					+ root[active].height
					+ " = "
					+ StringUtils.format(
							root[active].height
									/ (Math.floor(lg(root[active].size)) + 1),
							2, 5)
					+ "\u00b7"
					+ M.S.L.getString("opt")
					+ ";   "
					+ M.S.L.getString("avedepth")
					+ ": "
					+ StringUtils.format(root[active].sumh
							/ (double) root[active].size, 2, -5);
		}

	}

	@Override
	public void clear() {
		for (int i = 0; i <= numHeaps; i++) {
			root[i] = null;
		}

		setStats();
		
	}

	@Override
	public void draw(View V) {
		for (int i = 0; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i].moveTree();
				root[i].drawTree(V);
			}
		}
		
		if (v != null) { 
			v.moveTree(); 
			v.drawTree(V); 
		}
		
		if (v2 != null) {
			v2.moveTree(); 
			v2.drawTree(V); 
		}
		
	}
	
	public void reposition() {

		int sumx = 0;
		for (int i = 1; i <= numHeaps; ++i) {
			if (root[i] != null) {
				root[i].reposition();
				root[i].reboxTree();
				sumx += root[i].leftw;
				root[i].repos(sumx, root[i].toy);
				sumx += root[i].rightw;
			}
			if (i == active) {
				if (root[0] != null) {
					root[0].reposition();
					root[0].reboxTree();
					sumx += root[0].leftw;
					if (root[0].y >= 0) { // nie je na zaciatku vkladania
						root[0].repos(sumx, root[0].y);
					} else {
						root[0].repos(sumx, root[0].toy);
					}
					sumx += root[0].rightw;
				}
			}
		}
		M.screen.V.setBounds(0, 0, sumx, y2);
	}


}
