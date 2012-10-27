package algvis.core.visual;

import java.awt.geom.Rectangle2D;

import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.view.View;

public class ShadeTriple extends VisualElement {
	BSTNode u, v, w;

	public ShadeTriple(Scene scene, BSTNode u, BSTNode v, BSTNode w) {
		super(scene, Scene.MAXZ - 1);
		this.u = u;
		this.v = v;
		this.w = w;
	}

	@Override
	protected void draw(View V) {
		BSTNode z;
		if (u != null) {
			z = u.getParent();
			if (z == v || z == w) {
				V.drawWideLine(u.x, u.y, z.x, z.y);
			}
		}
		if (v != null) {
			z = v.getParent();
			if (z == u || z == w) {
				V.drawWideLine(v.x, v.y, z.x, z.y);
			}
		}
		if (w != null) {
			z = w.getParent();
			if (z == u || z == v) {
				V.drawWideLine(w.x, w.y, z.x, z.y);
			}
		}
	}

	@Override
	protected void move() {
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return null;
	}

	@Override
	protected void endAnimation() {
	}

	@Override
	protected boolean isAnimationDone() {
		return true;
	}
}
