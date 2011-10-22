package algvis.example;

import java.awt.Graphics;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;
import algvis.core.VisPanel;

/**
 * The Class ExampleDS.
 * This is not a real data structure; its sole purpose is to show (in couple of commits)
 * how to develop a new data structure.
 * In this example we will just have nodes that can be moved to different positions. 
 */
public class ExampleDS extends DataStructure {
	// in this case we have the same class for the abstract data type
	// and for the concrete data structure
	public static String adtName = "example";
	public static String dsName = "example";
	
	// this is the node we're going to move
	public Node v;

	public ExampleDS(VisPanel M) {
		super(M);
		// key = 47, position = (0,0)
		v = new Node(this, 47, 0, 0);
	}


	@Override
	public void insert(int x) {
		// TODO Auto-generated method stub
		
	}
	
	public void move(int x, int y) {
		start(new ExampleMove(this, x, y));
	}

	@Override
	public void clear() {
		// in this case; lets just reposition
		// the node back to the origin
		v = new Node(this, 47, 0, 0);
	}

	@Override
	public void draw(Graphics G, View V) {
		v.move();
		v.draw(G, V);		
	}

	@Override
	public String stats() {
		// the exact string that stats returns is displayed
		// so we should ask the getString method for the message in the correct language
		return M.a.getString("eok") + v.key;
	}
}
