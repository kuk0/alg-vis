package algvis.example;

import java.awt.Graphics;

import algvis.core.DataStructure;
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

	public ExampleDS(VisPanel M) {
		super(M);
	}


	@Override
	public void insert(int x) {
		// TODO Auto-generated method stub
		
	}
	
	public void move(int x, int y) {
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics g, View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String stats() {
		// TODO Auto-generated method stub
		return null;
	}
}
