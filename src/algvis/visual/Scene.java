package algvis.visual;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import algvis.core.View;

public class Scene implements VisualElement {
	ArrayList<VisualElement> E; // + different depths
	
	public Scene() {
		E = new ArrayList<VisualElement>();
	}
	
	public void draw(View v) {
		// draw everything
	}
	
	public void update() {
		// update everything
	}

	public Rectangle2D boundingBox() {
		// bound of everything
		return null;
	}
}
