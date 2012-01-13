package algvis.visual;

public class RelPos extends Position { // bod posunuty vzhladom na p
	Position p;
	float offsetX, offsetY;

	public RelPos(Position pos, float x, float y) {
		p = pos;
		offsetX = x;
		offsetY = y;
		update();
	}

	public void update() {
		p.update();
		x = p.x + offsetX;
		y = p.y + offsetY;
		tx = p.tx + offsetX;
		ty = p.ty + offsetY;
	}
}