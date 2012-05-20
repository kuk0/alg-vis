package algvis.intervaltree;

import algvis.core.ClickListener;
import algvis.core.IntervalTrees;
import algvis.core.Node;
import algvis.core.View;
import algvis.core.VisPanel;

public class IntervalTree extends IntervalTrees implements ClickListener{
	public static String dsName = "intervaltree";
	IntervalNode root = null, v = null, v2 = null;
	int numLeafs = 0; //pocet obsadenych listov

	public IntervalTree(VisPanel M) {
		super(M);
		//IntervalTree.minsepx = 10;
	}

	@Override
	public String getName() {
		return "intervaltree";
	}

	@Override
	public String stats() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(int x) {
		start(new IntervalInsert(this, x));
		
	}

	
	@Override
	public void clear() {
		root = null;
		v = null; 
		v2 = null;
		numLeafs = 0;
	}
	

	@Override
	public void draw(View v) {
		if (getRoot() != null) {
			getRoot().moveTree();
			getRoot().drawTree(v);
		}
		
		if (getV() != null) {
			getV().move();
			getV().draw(v);
		}
		
		
	}

	@Override
	public void mouseClicked(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decreaseKey(Node v, int delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ofinterval(int b, int e) {
		start(new IntervalFindMin(this, b, e));
		//start(new IntervalInsert(this, b));
	}
	
	public IntervalNode getRoot() {
		return root;
	}

	public IntervalNode setRoot(IntervalNode root) {
		this.root = root;
		return this.root;
	}
	
	public IntervalNode getV() {
		return (IntervalNode) v;
	}

	public IntervalNode setV(IntervalNode v) {
		this.v = v;
		return v;
	}
	
	public void reposition() {
		x1 = x2 = y1 = y2 = 0;
		if (getRoot() != null) {
			getRoot().reposition();
		}
		M.screen.V.setBounds(x1, y1, x2, y2);
	}

	public int getHeight(){
		int tmp = numLeafs;
		int res = 1;
		while (tmp > 1){
			tmp /= 2;
			res++;
		} 
		if (numLeafs > 0){
			return res;
		}else{
			return 0;
		}
	}
	
	int numL = numLeafs + 2;
	
	public IntervalNode generateEmpty(int h){
		IntervalNode w = new IntervalNode(this, Node.NOKEY);
		if (h>0){
			IntervalNode tmp1 = generateEmpty(h-1);
			IntervalNode tmp2 = generateEmpty(h-1);
			w.setLeft(tmp1);
			tmp1.setParent(w);
			w.setRight(tmp2);
			tmp2.setParent(w);
			w.setInterval(tmp1.b, tmp2.e);
		} else {
			w.setInterval(numL, numL);
			numL++;
		}
		
		return w;
		
	}
	
	public void extend(){
		IntervalNode w = new IntervalNode(this, 0); //pre suctovy strom je 0, min je +inf, max je -inf
		w.key = Node.NOKEY;
		IntervalNode w2 = root;
		w.setLeft(w2);
		w2.setParent(w);
		root = w;
		
		IntervalNode tmp = generateEmpty(getHeight() - 1);
		root.setRight(tmp);
		tmp.setParent(root);
		System.out.println(this.getHeight());
		reposition();
		//root.add();
		
	}
}
