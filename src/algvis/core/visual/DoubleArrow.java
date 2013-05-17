package algvis.core.visual;

import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;

import algvis.ui.view.View;

public class DoubleArrow extends VisualElement {
	int x1, y1, x2, y2;
	
	public DoubleArrow(int x1, int y1, int x2, int y2) {
		super(0);
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	protected void draw(View v) throws ConcurrentModificationException {
		v.drawDoubleArrow(x1, y1, x2, y2);
	}

	@Override
	protected void move() throws ConcurrentModificationException {
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return new Rectangle2D.Double(x1, y1, x2, y2);
	}
}