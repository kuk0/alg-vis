package algvis.visual;

import java.awt.geom.Rectangle2D;
import algvis.core.View;

public interface VisualElement {
	public void draw(View v);
	public void update();
	Rectangle2D boundingBox();
}
