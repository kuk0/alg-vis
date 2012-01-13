package algvis.visual;

public class MovingPos extends Position {
	static final float DAMPING = 0.7f; // formerly 0.9f
	static final float ATTRACTION = 0.1f; // formerly 0.1f
	static final float M = 1; // mass

	float vx = 0, vy = 0, ax = 0, ay = 0, fx = 0, fy = 0; // velocity, acceleration, force
	Position target;

	public MovingPos(float x, float y) {
		this.x = x;
		this.y = y;
		target = new FixedPos(x, y);
		update();
	}

	public void moveTo(Position t) {
		target = t;
	}

	public void update() {
		target.update();
		tx = target.x;
		ty = target.y;
	}

	private float dist2(float x1, float y1, float x2, float y2) {
		return (x2-x1)*(x2-x1)+(y2-y1)*(y2-y1);
	}
	
	public boolean move() {
		update();
		if (x == tx && y == ty)
			return false;

		fx = ATTRACTION * (tx - x);
		fy = ATTRACTION * (ty - y);
		ax = fx / M;
		ay = fy / M;
		vx = (vx + ax) * DAMPING;
		vy = (vy + ay) * DAMPING;
		x += vx;
		y += vy;

		if (dist2(x, y, tx, ty) < 1 && vx < 0.1 && vy < 0.1) {
			x = tx;
			y = ty;
			vx = vy = ax = ay = 0;
		}
		return true;
	}
}
