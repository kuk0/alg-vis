package algvis.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

public class View {
	VisPanel P;
	Graphics2D g;
	final static double SCALE_FACTOR = 1.2, MIN_ZOOM = 0.16, MAX_ZOOM = 5.5;
	int W, H; // display width&height
	int minx, miny, maxx, maxy;
	double viewX, viewY, viewW, viewH, f; // view coordinates; factor

	public View(VisPanel P) {
		this.P = P;
	}

	public void setGraphics(Graphics2D g) {
		this.g = g;
	}

	public void setWH(int w, int h) {
		W = w;
		H = h;
		viewW = w / 2.0;
		viewH = h / 2.0;
		viewX = 0;
		viewY = viewH - 50;
		P.hSlider.setValue((int) viewX);
		P.vSlider.setMinimum((int) viewY);
		P.vSlider.setMaximum((int) viewY);
		P.vSlider.setValue((int) viewY);
		f = 1;
		// System.out.println(w+"  "+h);
	}

	public void setX(int x) {
		viewX = x;
	}

	public void setY(int y) {
		viewY = y;
	}

	public void setBounds(int minx, int miny, int maxx, int maxy) {
		this.minx = minx;
		this.miny = miny;
		this.maxx = maxx;
		this.maxy = maxy;
		P.hSlider.setMinimum(minx);
		P.hSlider.setMaximum(maxx);
		P.vSlider.setMinimum(miny);
		P.vSlider.setMaximum(maxy);
	}

	static int map(double x, double a, double b, double c, double d) {
		if (Math.abs(a - b) < 1e-11) {
			System.out.println("map dostal a==b !");
			return 0;
		} else {
			return (int) Math.round(c + (x - a) / (b - a) * (d - c));
		}
	}

	// zmeni "virtualne" suradnice na "realne" (pixel na obrazovke)
	public int v2rX(double x) {
		return map(x, viewX - viewW, viewX + viewW, 0, W);
	}

	public int v2rY(double y) {
		return map(y, viewY - viewH, viewY + viewH, 0, H);
	}

	public int v2rF(int fs) {
		fs = (int) Math.round(f * fs);
		if (fs < Fonts.MIN) {
			return 0;
		}
		if (fs > Fonts.MAX) {
			fs = Fonts.MAX;
		}
		return fs;
	}

	// naopak
	public int r2vX(double x) {
		return map(x, 0, W, viewX - viewW, viewX + viewW);
	}

	public int r2vY(double y) {
		return map(y, 0, H, viewY - viewH, viewY + viewH);
	}

	public void moveLeft() {
		if (viewX - 1.1 * viewW >= minx) {
			viewX -= 0.1 * viewW;
		}
	}

	public void moveRight() {
		if (viewX + 1.1 * viewW <= maxx) {
			viewX += 0.1 * viewW;
		}
	}

	public void moveUp() {
		viewY -= 0.1 * viewH;
	}

	public void moveDown() {
		viewY += 0.1 * viewH;
	}

	public void zoomIn() {
		if (f * SCALE_FACTOR <= MAX_ZOOM) {
			f = f * SCALE_FACTOR;
			viewW = viewW / SCALE_FACTOR;
			viewH = viewH / SCALE_FACTOR;
			// System.out.println(f);
		}
	}

	public void zoomOut() {
		if (f / SCALE_FACTOR >= MIN_ZOOM) {
			f = f / SCALE_FACTOR;
			viewW = viewW * SCALE_FACTOR;
			viewH = viewH * SCALE_FACTOR;
			// System.out.println(f);
		}
	}

	public boolean inside(int x, int y) {
		return (viewX - viewW <= x) && (x <= viewX + viewW)
				&& (viewY - viewH <= y) && (y <= viewY + viewH);
	}

	public void setColor(Color c) {
		g.setColor(c);
	}

	public void fillSqr(int x, int y, int a) {
		a = (int) Math.round(f * a);
		g.fillRect(v2rX(x) - a, v2rY(y) - a, 2 * a, 2 * a);
	}

	public void drawSqr(int x, int y, int a) {
		a = (int) Math.round(f * a);
		g.drawRect(v2rX(x) - a, v2rY(y) - a, 2 * a, 2 * a);
	}

	public void fillOval(int x, int y, int a, int b) {
		g.fillOval(v2rX(x), v2rY(y), (int) Math.round(f * a), (int) Math
				.round(f * b));
	}

	public void drawOval(int x, int y, int a, int b) {
		g.drawOval(v2rX(x), v2rY(y), (int) Math.round(f * a), (int) Math
				.round(f * b));
	}

	public void fillCircle(int x, int y, int r) {
		r = (int) Math.round(f * r);
		g.fillOval(v2rX(x) - r, v2rY(y) - r, 2 * r, 2 * r);
	}

	public void drawCircle(int x, int y, int r) {
		r = (int) Math.round(f * r);
		g.drawOval(v2rX(x) - r, v2rY(y) - r, 2 * r, 2 * r);
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		g.drawLine(v2rX(x1), v2rY(y1), v2rX(x2), v2rY(y2));
	}

	// square; x, y is the center
	public void drawSquare(int x, int y, int a) {
		g.drawRect(v2rX(x - a), v2rY(y - a), (int) Math.round(f * 2 * a),
				(int) Math.round(f * 2 * a));
	}

	public int stringWidth(String str, int fs) {
		fs = v2rF(fs);
		if (fs == 0) {
			return 0;
		}
		return Fonts.fm[fs].stringWidth(str);
	}

	public void drawString(String str, int x, int y, int fs) {
		fs = v2rF(fs);
		if (fs == 0) {
			return;
		}
		x = v2rX(x) - Fonts.fm[fs].stringWidth(str) / 2;
		y = v2rY(y) - Fonts.fm[fs].getHeight() / 2 + Fonts.fm[fs].getAscent();
		g.setFont(Fonts.f[fs]);
		g.drawString(str, x, y);
	}

	public void drawStringLeft(String str, int x, int y, int fs) {
		fs = v2rF(fs);
		if (fs == 0) {
			return;
		}
		x = v2rX(x) - Fonts.fm[fs].stringWidth(str);
		y = v2rY(y) - Fonts.fm[fs].getHeight() / 2 + Fonts.fm[fs].getAscent();
		g.setFont(Fonts.f[fs]);
		g.drawString(str, x, y);
	}

	public void drawStringTop(String str, int x, int y, int fs) {
		fs = v2rF(fs);
		if (fs == 0) {
			return;
		}
		x = v2rX(x) - Fonts.fm[fs].stringWidth(str) / 2;
		y = v2rY(y) - Fonts.fm[fs].getHeight();
		g.setFont(Fonts.f[fs]);
		g.drawString(str, x, y);
	}

	public void fillArc(int x, int y, int w, int h, int a1, int a2) {
		g.fillArc(v2rX(x), v2rY(y), (int) Math.round(f * w), (int) Math.round(f
				* h), a1, a2);
	}

	public void drawRoundRectangle(int x, int y, double w, double h,
			double arcw, double arch) {
		g.draw(new RoundRectangle2D.Double(v2rX(x) - f * w, v2rY(y) - f * h, 2
				* f * w, 2 * f * h, f * arcw, f * arch));
	}

	public void fillRoundRectangle(int x, int y, double w, double h,
			double arcw, double arch) {
		g.fill(new RoundRectangle2D.Double(v2rX(x) - f * w,
				v2rY(y) - f * h, 2 * f * w, 2 * f * h, f * arcw, f * arch));
	}

	private void arrowHead(int x, int y, int xx, int yy) {
		double alpha = f * 6.0, beta = 0.93;
		int[] xPoints = new int[3], yPoints = new int[3];
		double vecX, vecY, normX, normY, d, th, ta, baseX, baseY;

		xPoints[0] = xx;
		yPoints[0] = yy;

		// build the line vector
		vecX = xx - x;
		vecY = yy - y;

		// build the arrow base vector - normal to the line
		normX = -vecY;
		normY = vecX;

		// setup length parameters
		d = (float) Math.sqrt(vecX * vecX + vecY * vecY);
		th = alpha / (2.0 * d);
		ta = alpha / (beta * d);

		// find the base of the arrow
		baseX = xx - ta * vecX;
		baseY = yy - ta * vecY;

		// build the points on the sides of the arrow
		xPoints[1] = (int) (baseX + th * normX);
		yPoints[1] = (int) (baseY + th * normY);
		xPoints[2] = (int) (baseX - th * normX);
		yPoints[2] = (int) (baseY - th * normY);

		g.fillPolygon(xPoints, yPoints, 3);
	}

	public void drawArrow(int x1, int y1, int x2, int y2) {
		x1 = v2rX(x1);
		y1 = v2rY(y1);
		x2 = v2rX(x2);
		y2 = v2rY(y2);
		g.drawLine(x1, y1, x2, y2);
		arrowHead(x1, y1, x2, y2);
	}

	public void drawArcArrow(int x, int y, int w, int h, int a1, int a2) {
		x = v2rX(x);
		y = v2rY(y);
		w = (int) Math.round(f * w);
		h = (int) Math.round(f * h);
		g.drawArc(x, y, w, h, a1, a2 - a1);
		// g.drawRect(x, y, w, h);
		double a = a2 * Math.PI / 180;
		int x2 = x + (int) Math.round(w / 2.0 * (1 + Math.cos(a))), y2 = y
				+ (int) Math.round(h / 2.0 * (1 - Math.sin(a)));
		int dx = (int) Math.round(128 * Math.sin(a)), dy = (int) Math
				.round(128 * Math.cos(a));
		if (a1 <= a2) {
			x = x2 + dx;
			y = y2 + dy;
		} else {
			x = x2 - dx;
			y = y2 - dy;
		}
		// g.drawLine(x, y, x2, y2);
		arrowHead(x, y, x2, y2);
	}
}
