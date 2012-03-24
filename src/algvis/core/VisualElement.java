package algvis.core;

import java.awt.geom.Rectangle2D;

public interface VisualElement {
	public void draw(View v);
	public void move();
	Rectangle2D getBoundingBox();
}
