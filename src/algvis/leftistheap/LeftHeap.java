package algvis.leftistheap;

import algvis.core.MeldablePQ;
import algvis.core.Node;
import algvis.core.StringUtils;
import algvis.core.View;
import algvis.core.VisPanel;

//robene podla BinomialHeap
public class LeftHeap extends MeldablePQ {
	public static String dsName = "leftheap";
	int n = 0; // pocet vrcholov
	LeftHeapNode root = null, v = null, v2 = null;

	public LeftHeap(VisPanel M) {
		super(M);
		//root = new LeftHeapNode(this);//[numHeaps + 1];		
	}

	@Override
	public void insert(int x) {
		start(new LeftHeapInsert(this, x));
		n++;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void meld(int i, int j) {
		// TODO Auto-generated method stub

	}

	@Override
	public String stats() {
		
	
			
		if (root == null) {
			return M.L.getString("size") + ": 0;   " + M.L.getString("height")
					+ ": 0 =  1.00\u00b7" + M.L.getString("opt") + ";   "
					+ M.L.getString("avedepth") + ": 0";
		} else {
			root.calcTree();
			return M.L.getString("size")
					+ ": "
					+ root.size
					+ ";   "
					+ M.L.getString("height")
					+ ": "
					+ root.height
					+ " = "
					+ StringUtils.format(root.height / (Math.floor(lg(root.size)) + 1), 2,
							5) + "\u00b7" + M.L.getString("opt") + ";   "
					+ M.L.getString("avedepth") + ": "
					+ StringUtils.format(root.sumh / (double) root.size, 2, -5);
		}
		
	}

	@Override
	public void clear() {	
			root = null;
		
		setStats();

	}

	@Override
	public void decreaseKey(Node v, int delta) {
		// TODO Auto-generated method stub

	}

	// tuto potom zmazat
	//opravit, aby sa posuvali, uplne prerobit.
	public void reposition() {
		//for (int i = 1; i <= numHeaps; ++i){
			if (root != null) {
				root.reposition();
				M.S.V.setBounds(x1, y1, x2, y2);
			}
		//}
	}

	@Override
	public void draw(View V) {
		// TODO Auto-generated method stub
	//	for (int i = 1; i <= numHeaps; ++i){
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
		//}
	}

}
