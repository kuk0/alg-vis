package algvis.core.visual;

import java.awt.geom.Rectangle2D;

import algvis.core.Node;
import algvis.ui.view.View;

public class ShadePair extends VisualElement {
	Node u, v;

	public ShadePair(Node u, Node v) {
		super(Scene.MAXZ - 1);
		this.u = u;
		this.v = v;
	}

	@Override
	protected void draw(View V) {
		V.drawWideLine(u.x, u.y, v.x, v.y);
	}

	@Override
	protected void move() {
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return null;
	}
}
