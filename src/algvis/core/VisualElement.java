package algvis.core;

import java.awt.geom.Rectangle2D;

public abstract class VisualElement {
	VisualElement prev = this, next = this;

	public abstract void draw(View v);

	public abstract void move();

	public void link(VisualElement prev, VisualElement next) {
		this.prev = prev;
		this.next = next;
		prev.next = next.prev = this;
	}
	
	public void unlink() {
		prev.next = next;
		next.prev = prev;
	}

	public abstract Rectangle2D getBoundingBox();
}
