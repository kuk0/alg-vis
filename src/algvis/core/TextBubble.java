package algvis.core;

import java.awt.geom.Rectangle2D;

public class TextBubble extends VisualElement {
	String s;
	int x, y, w, alpha;
	int state; // 0 = fade-in; 1 = normal; 2 = fade-out;
	
	public TextBubble (String s, int x, int y, int w) {
		this.s = s;
		this.x = x;
		this.y = y;
		this.w = w;
		this.alpha = 0;
	}

	@Override
	public void draw(View v) {
		v.drawTextBubble(s, x, y, w, alpha);
	}

	@Override
	public void move() {
		switch (state) {
		case 0:
			alpha += 21;
			if (alpha >= 0xf0) state = 1;
			break;
		case 1:
			break;
		case 2:
			alpha -= 21;
			if (alpha <= 0) {
				alpha = 0;
				state = 3;
				unlink();
			}
			break;
		}
	}

	public void hide() {
		state = 2;
	}
	
	@Override
	public Rectangle2D getBoundingBox() {
		return null;
	}
	 
}
