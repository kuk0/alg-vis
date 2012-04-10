package algvis.daryheap;

import java.awt.Color;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.PriorityQueue;
import algvis.core.View;
import algvis.heap.HeapNode;

public class DaryHeapNode extends HeapNode{
	int width;//, leftw, rightw;
	DaryHeapNode parent = null;
	int numChildren = 0;
	int nson = -1 ; //kolky je to syn svojho otca
	DaryHeapNode[] c;
	
	int nnodes = 1, height = 1; // pre setStats
	
	public DaryHeapNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
		bgKeyColor();
		c = new DaryHeapNode[((DaryHeap) D).getOrder()];
		//setColor(NodeColor.NORMAL);
		width = DataStructure.minsepx;
		//mark();
	}

	public DaryHeapNode(DataStructure D, int key) {
		this(D, key, 0, 0);
		getReady();
	}

	public DaryHeapNode(DaryHeapNode v) {
		this(v.D, v.key, v.x, v.y);
	}

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return numChildren == 0;
	}

	/*
	 * public boolean isLeft() { return parent.left==this; } public void
	 * linkleft(BSTNode v) { left = v; v.parent = this; } public void
	 * linkright(BSTNode v) { right = v; v.parent = this; } public void
	 * isolate() { left = right = parent = null; }
	 */

	public void calcTree() {
		nnodes = 1;
		for (int i = 0; i < numChildren; ++i) {
			c[i].calcTree();
			nnodes += c[i].nnodes;
		}
		height = 1 + (isLeaf() ? 0 : c[0].height);
	}
	
	/*
	public void addLeaf(int x) {
		c[numChildren++].key = x;
		for (int i = numChildren - 1; i > 0; --i) {
			if (c[i].key < c[i - 1].key) {
				int tmp = c[i].key;
				c[i].key = c[i - 1].key;
				c[i - 1].key = tmp;
			}
		}
		width = _width();
	}
	*/


	public int order() {
		for (int i = 0; i < parent.numChildren; ++i) {
			if (getParent().c[i] == this) {
				return i;
			}
		}
		return -5; // TODO: vypindat exception
	}
/*
	int _width() {
		
		if (key != Node.NOKEY && numChildren > 0) {
			return (2 * Node.radius + DataStructure.minsepx)*numChildren - DataStructure.minsepx;		
		} else {
			return 2*Node.radius;
		}
	}
	*/	
	
	@Override
	public void draw(View v) {
		if (state == Node.INVISIBLE || key == NULL) {
			return;
		}
		
		boolean a = D.scenario.isAddingEnabled();
		D.scenario.enableAdding(false);
		//setColor(isRed() ? NodeColor.RED : NodeColor.BLACK);
		D.scenario.enableAdding(a);
		

		super.draw(v);
	}
	
	static int i;
	public void drawTree(View v) {
		i = 0;
		drawTree2(v);
	}
	
	private void drawTree2(View v) {
		
		if (state != INVISIBLE){// && !thread) {
			/*if (thread) {
				v.setColor(Color.yellow);
			} else {*/
				v.setColor(Color.black);
			//}
			for (int i = 0; i< numChildren; i++){
				v.drawLine(x, y, c[i].x, c[i].y);
			}
		}
		for (int i = 0; i< numChildren; i++){
			c[i].drawTree2(v);
		}
		/*
		v.setColor(Color.LIGHT_GRAY);
		++i;
		v.drawLine(x, y, x, -20);
		v.drawString("" + i, x, -23, 10);
		*/
		draw(v);
	}	

	
	/*
	public void drawTree(View v) {
		for (int i = 0; i < numChildren; ++i) {
			v.setColor(Color.black);
			/*
			 * int xx, yy; if (i==0 || i==numChildren-1) { xx = x; yy = y; }
			 * else { xx = (pos(i-1)+pos(i))/2; yy = y+D.radius; }
			 *//*
			v.drawLine(x, y, c[i].x, c[i].y - Node.radius);
			c[i].drawTree(v);
		}
		draw(v);
	}

*/
	@Override
	public void moveTree() {
		for(int i = 0; i < numChildren; i++){
			c[i].moveTree();
		}
		move();
	}	
	
	public void reboxTree() {
		for (int i = 0; i < numChildren; ++i) {
			c[i].reboxTree();
		}
		this.rebox();
	}
	
	@Override
	public void rebox() {

		leftw = 0; //-DataStructure.minsepx / 2;
		rightw = 0; //-DataStructure.minsepx / 2;		
		if (isLeaf()){
			leftw = DataStructure.minsepx / 2;
			rightw = DataStructure.minsepx / 2;
			width = leftw + rightw;
			//System.out.print("moje suradnice su " + tox + " a " + toy + ", moje leftw a rightw je " + leftw + " " + rightw + " a som " + nson + ". syn \n" );			
			return;
		}

		if (numChildren < ((DaryHeap)D).getOrder()){
			leftw =  (((DaryHeap)D).getOrder() / 2) * DataStructure.minsepx;
			if (((DaryHeap)D).getOrder() % 2 == 1){
				leftw += (DataStructure.minsepx / 2); 
			}
			if (numChildren > ((DaryHeap)D).getOrder() / 2){
				rightw = leftw - (((DaryHeap)D).getOrder() - numChildren) * DataStructure.minsepx;
			}else{
				rightw = DataStructure.minsepx/2;
			}
			
			width = leftw + rightw;
			return;
		}
		
		leftw = 0;
		rightw = 0;
		for (int i = 1; i <= ((DaryHeap)D).getOrder()/2; i++){
			leftw += c[i - 1].width;  //<<--- mozno tutok mozu byt problemy
			//System.out.print("pricitavam k " + nson + ". leftw " + c[i-1].width + " za " + i + ". syna \n" );
			
		}
		
		for (int i = (((DaryHeap)D).getOrder()/2) +1; i<=((DaryHeap)D).getOrder(); i++){
			rightw += c[i - 1].width;  //<<--- mozno tutok mozu byt problemy
			//System.out.print("pricitavam k " + nson + ". rightw " + c[i-1].width + "\n" );
		}
		
		if (((DaryHeap)D).getOrder() % 2 == 1){
			rightw -= c[(((DaryHeap)D).getOrder() / 2)].leftw;
			leftw += c[(((DaryHeap)D).getOrder() / 2)].leftw;
		}
		
		//leftw += DataStructure.minsepx / 2;
		//rightw += DataStructure.minsepx / 2;
		width = leftw + rightw;
		//System.out.print("moje suradnice su " + tox + " a " + toy + ", moje leftw a rightw je " + leftw + " " + rightw + " a som " + nson + ". syn \n" );
	}
	
	private void repos() {
		if (isRoot()) {
			goToRoot();
			D.x1 = -width / 2 ; //-leftw;
			D.x2 = width / 2; //rightw;
			D.y2 = this.toy;
		}
		if (this.toy > D.y2) {
			D.y2 = this.toy;
		}
		/*
		if (numChildren == 0) {
			return;
		}
		*/

		for (int i = 0; i < numChildren; i++){
			if (i == 0){
				c[0].goTo(this.tox - (this.leftw) + c[0].leftw,// - DataStructure.minsepx/2,// + c[0].leftw,
						this.toy + DataStructure.minsepy);
			}else{
				c[i].goTo( c[i-1].tox + c[i-1].rightw + c[i].leftw,
							this.toy + DataStructure.minsepy);
			}
			c[i].repos();
		}
		//System.out.print("moje suradnice su " + tox + " a " + toy + ", moje leftw a rightw je " + leftw + " " + rightw + " a som " + nson + ". syn \n" );		
	}

	public void _reposition() {
		reboxTree();
		repos();
	}

	/*
	public int _goToX(DaryHeapNode v) {
		int x = key, p = v.numChildren;
		for (int i = 0; i < p; ++i) {
			if (x <= v.c[i].key) {
				p = i;
			}
		}
		return (v.pos(p - 1) + v.pos(p)) / 2;
	}

	public void goTo(DaryHeapNode v) {
		goTo(_goToX(v), v.toy);
	}

	public void goAbove(DaryHeapNode v) {
		goTo(_goToX(v), v.toy - 2 * Node.radius + 2);
	}

	public void goBelow(DaryHeapNode v) {
		goTo(_goToX(v), v.toy + 2 * Node.radius - 2);
	}
	*/

	/*
	 * public void goToRoot() { if (((DaryHeapTree)D).root == null) { goTo (D.rootx,
	 * D.rooty); } else { goTo(_goToX(((DaryHeapTree)D).root), D.rooty); } }
	 * 
	 * public void goAboveRoot() { if (((DaryHeapTree)D).root == null) { goTo (D.rootx,
	 * D.rooty - 2*D.radius); } else { goTo(_goToX(((DaryHeapTree)D).root),
	 * D.rooty-2*D.radius); } }
	 */
	
	public DaryHeapNode getParent() {
		return parent;
	}
	
	public void setParent(DaryHeapNode v) {
		this.parent = v;
	}
	
	public boolean prec(DaryHeapNode v) {
		if (((PriorityQueue) D).minHeap) {
			return this.key < v.key;
		} else {
			return this.key > v.key;
		}
	}

	/**
	 * Precedes or equals (see prec).
	 */
	public boolean preceq(DaryHeapNode v) {
		if (((PriorityQueue) D).minHeap) {
			return this.key <= v.key;
		} else {
			return this.key >= v.key;
		}
	}
	
	// vracia otca najblizsieho suseda vpravo
	// funguje iba pre listy
	public DaryHeapNode nextneighbour(){
		if (isRoot()) {
			return this;
		}

		if (getParent().numChildren < ((DaryHeap) D).getOrder()){   // pre root nson == -1
			//System.out.print("malo synov kluca " + getParent().key + ", konkretne " + getParent().numChildren + " a order mame prave " + ((DaryHeap) D).getOrder() + "\n" );
			return getParent();
		}
		
		//if (this.nson == ((DaryHeap) D).getOrder()){ 
			DaryHeapNode v = this;		
			while ( (!v.isRoot()) && (v.nson == ((DaryHeap) D).getOrder()) ){
				v = v.getParent();
			}
			
			if (v.isRoot()){
				while (v.c[0] != null) {
					v = v.c[0];
				}
				return v;
			}
			
			v = v.getParent().c[v.nson]; // v poli je n-ty syn na mieste (nson - 1) 
			while (v.c[0] != null) {
				v = v.c[0];
			}
			return v;
		//}
		
		//return this.getParent();
	}
	
	// vracia najblizsieho suseda vlavo, (prerobit na otca?)
	// funguje iba pre listy
	public DaryHeapNode prevneighbour(){
		if (isRoot()) {
			return null;
		}

		if (nson > 1){   // pre root nson == -1
			//System.out.print("dost synov, konkretne " + getParent().numChildren + " a order mame prave " + ((DaryHeap) D).getOrder() + "\n" );
			return getParent().c[nson-2];
		}

		DaryHeapNode v = this;
		while ( (!v.isRoot()) && (v.nson == 1)){
			v = v.getParent();
		}
		if (!v.isRoot()){
			v = v.getParent().c[v.nson - 2];
		}

		while (!v.isLeaf()){
			v = v.c[((DaryHeap) D).getOrder() - 1];
		}

		return v;

	}
	
	public void linknewson(DaryHeapNode v){
		numChildren ++;
		v.nson = numChildren;
		c[numChildren - 1] = v;
		c[numChildren - 1].setParent(this); 
		((DaryHeap) D).last = c[numChildren - 1];
	}

	public DaryHeapNode find(int x, int y) {
		if (inside(x, y))
			return this;

		for (int i = 0; i < numChildren; i++){
			DaryHeapNode tmp = c[i].find(x, y);
			if (tmp != null)
				return tmp;
		}
		return null;
	}

	public DaryHeapNode findMaxSon(){
		DaryHeapNode v = c[0];
		for (int i = 0; i < numChildren; i++){
			if (c[i].prec(v)) {
				v = c[i];
			}
		}

		return v;
	}

}
