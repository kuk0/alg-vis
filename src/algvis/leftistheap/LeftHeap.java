package algvis.leftistheap;

import algvis.core.MeldablePQ;
import algvis.core.MeldablePQButtons;
import algvis.core.Node;
import algvis.core.Pair;
import algvis.core.StringUtils;
import algvis.core.View;
import algvis.core.VisPanel;

//robene podla BinomialHeap
public class LeftHeap extends MeldablePQ {
	public static String dsName = "leftheap";
	int n = 0; // pocet vrcholov
	LeftHeapNode root[]  = null, v = null, v2 = null;

	public LeftHeap(VisPanel M) {
		super(M);
		root = new LeftHeapNode[numHeaps + 1];

	}

	@Override
	public void insert(int x) {
		start(new LeftHeapInsert(this, active,  x));
		active = 0;
		start(new LeftHeapInsert(this, 0,  x+1));
		active = 1;
	//	n++;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	//pripadne to skontrolovat...
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
		Pair p = chooseHeaps(1, 0);		//<<---- potom i,j
		i = p.first;
		j = p.second;
		((MeldablePQButtons) M.B).activeHeap.setValue(i);
		start(new LeftHeapMeld(this, i, j));	
	}

	@Override
	public String stats() {
		
	
			
		if (root[active] == null) {   //<<---- root
			return M.L.getString("size") + ": 0;   " + M.L.getString("height")
					+ ": 0 =  1.00\u00b7" + M.L.getString("opt") + ";   "
					+ M.L.getString("avedepth") + ": 0";
		} else {
			root[active].calcTree();
			return M.L.getString("size")
					+ ": "
					+ root[active].size
					+ ";   "
					+ M.L.getString("height")
					+ ": "
					+ root[active].height
					+ " = "
					+ StringUtils.format(root[active].height / (Math.floor(lg(root[active].size)) + 1), 2,
							5) + "\u00b7" + M.L.getString("opt") + ";   "
					+ M.L.getString("avedepth") + ": "
					+ StringUtils.format(root[active].sumh / (double) root[active].size, 2, -5);
		}		
		
	} 

	@Override
	public void clear() {	
		root[1] = null;		
		setStats();

	}

	@Override
	public void decreaseKey(Node v, int delta) {
		

	}

	// tuto potom zmazat
	//opravit, aby sa posuvali, uplne prerobit.
	public void reposition() {
		
		for (int i = 0; i <= numHeaps; ++i){ //<<----- 10 = numHeaps, nepomaha., i od 1 potom
			if (root[i] != null) {
			  root[i].reposition();
				M.S.V.setBounds(x1, y1, x2, y2);
			}
		}		
		root[1].reboxTree();		
		if (root[0] != null){
			root[0].reboxTree();
			//root[0].goTo(root[0].x + root[1].leftw, root[0].y);
			//root[0].tox = root[0].tox + root[1].leftw;
			int plus = root[1].rightw + root[0].leftw;
			root[0].repos(plus, root[0]);
		}
		
		
	}

	@Override
	public void draw(View V) {
		//pre ten for cyklus sa to tak rychlo vykreslovalo!!
		for (int i = 0; i <= numHeaps; ++i){  //<<----- i od 1 potom
			if (root[i] != null) {
				root[i].moveTree();
				root[i].drawTree(V);				
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
	}

}
