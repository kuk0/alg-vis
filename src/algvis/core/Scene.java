package algvis.core;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Scene extends VisualElement {
	public static final int MAXZ = 10, MIDZ = 5;
	ArrayList<VisualElement> E;
	Rectangle2D.Float B;

	public Scene() {
		E = new ArrayList<VisualElement>();
		for (int z=0; z<MAXZ; ++z) {
			E.add(new DummyElement());
		}
	}

	public void add(VisualElement e, int z) {
		if (z < 0)
			z = 0;
		if (z >= MAXZ)
			z = MAXZ - 1;
		VisualElement d = E.get(z);
		e.link(d.prev, d);
	}

	public void draw(View V) {
		for (VisualElement d : E) {
			VisualElement e = d.next;
			while (!(e instanceof DummyElement)) {
				e.draw(V);
				e = e.next;
			}
		}
	}

	public void move() {
		for (VisualElement d : E) {
			VisualElement e = d.next;
			while (!(e instanceof DummyElement)) {
				e.move();
				e = e.next;
			}
		}
	}

	public Rectangle2D getBoundingBox() {
		return B;
	}

	public boolean toRemove() {
		return false;
	}
}
