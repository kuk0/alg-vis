/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.core;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

public class View implements MouseListener, MouseMotionListener,
		MouseWheelListener {
	Graphics2D g;
	final static double SCALE_FACTOR = 1.1, MIN_ZOOM = 0.16, MAX_ZOOM = 5.5;
	public int W, H; // display width&height
	public int minx, miny, maxx, maxy;
	int mouseX, mouseY; // mouse position
	public Alignment align = Alignment.CENTER;

	double x, y, f;
	AffineTransform at, oldTransform;
	ClickListener D;

	public View(JPanel P) {
		P.addMouseListener(this);
		P.addMouseMotionListener(this);
		P.addMouseWheelListener(this);
		at = new AffineTransform();
		setBounds(0, 0, 0, 0);
	}

	public void setGraphics(Graphics2D g, int W, int H) {
		this.g = g;
		this.W = W;
		this.H = H;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		resetView();
	}

	public Graphics2D getGraphics() {
		return g;
	}

	public void resetView() {
		at.setToIdentity();
		double f = 1, f2 = 1;
		if ((maxx - minx) > W || (maxy - miny) > H) {
			f2 = Math.min(W / (double) (maxx - minx), H
					/ (double) (maxy - miny));
			f = Math.max(f2, MIN_ZOOM);
		}
		if (align == Alignment.CENTER) {
			if (f2 < f) {
				at.translate(W / 2, 0);
				at.scale(f, f);
				at.translate(-(maxx + minx) / 2, -miny);
			} else if (-f * minx >= W / 1.99999) {
				at.scale(f, f);
				at.translate(-minx, -miny);
			} else if (f * maxx >= W / 1.99999) {
				at.translate(W, 0);
				at.scale(f, f);
				at.translate(-maxx, -miny);
			} else {
				at.translate(W / 2, 0);
				at.scale(f, f);
				at.translate(0, -miny);
			}
		} else if (align == Alignment.LEFT) {
			at.scale(f, f);
			at.translate(-minx, -miny);
		}
	}

	public void setBounds(int minx, int miny, int maxx, int maxy) {
		this.minx = minx - 50;
		this.miny = miny - 50;
		this.maxx = maxx + 50;
		this.maxy = maxy + 50;
	}

	public void zoom(int x, int y, double f) {
		if (at.getScaleX() * f <= MAX_ZOOM) {
			AffineTransform t = new AffineTransform();
			t.translate(x, y);
			t.scale(f, f);
			t.translate(-x, -y);
			at.preConcatenate(t);
		}
	}

	public void zoomIn() {
		zoom(W / 2, H / 2, SCALE_FACTOR);
	}

	public void zoomIn(int x, int y) {
		zoom(x, y, SCALE_FACTOR);
	}

	public void zoomOut(int x, int y) {
		zoom(x, y, 1 / SCALE_FACTOR);
	}

	public void zoomOut() {
		zoom(W / 2, H / 2, 1 / SCALE_FACTOR);
	}

	public Point2D v2r(double x, double y) {
		Point2D p = new Point2D.Double(x, y);
		at.transform(p, p);
		return p;
	}

	public Point2D r2v(double x, double y) {
		Point2D p = new Point2D.Double(x, y);
		try {
			at.inverseTransform(p, p);
		} catch (NoninvertibleTransformException exc) {
			exc.printStackTrace();
		}
		return p;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		at.preConcatenate(AffineTransform.getTranslateInstance(x - mouseX, y
				- mouseY));
		mouseX = x;
		mouseY = y;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point2D p = r2v(e.getX(), e.getY());
		if (D != null) {
			D.mouseClicked((int) p.getX(), (int) p.getY());
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		if (notches > 0) {
			zoomOut(e.getX(), e.getY());
		} else {
			zoomIn(e.getX(), e.getY());
		}
	}

	public boolean inside(int x, int y) {
		Point2D p = v2r(x, y);
		return (0 <= p.getX()) && (p.getX() <= W) && (0 <= p.getY())
				&& (p.getY() <= H);
	}

	public void setColor(Color c) {
		g.setColor(c);
	}

	public void startDrawing() {
		oldTransform = g.getTransform();
		g.transform(at);
	}

	public void endDrawing() {
		g.setTransform(oldTransform);
	}

	public Point2D cut (int x, int y, int x2, int y2, int c) {
		double d = new Point2D.Double(x, y).distance(x2, y2);
		return new Point2D.Double(x + (x2-x)*(d-c)/d, y + (y2-y)*(d-c)/d);
	}

	public void fillSqr(int x, int y, int a) {
		g.fillRect(x - a, y - a, 2 * a, 2 * a);
	}

	public void drawSqr(int x, int y, int a) {
		g.drawRect(x - a, y - a, 2 * a, 2 * a);
	}

	public void fillOval(int x, int y, int a, int b) {
		g.fillOval(x, y, a, b);
	}

	public void drawOval(int x, int y, int a, int b) {
		g.drawOval(x, y, a, b);
	}

	public void fillCircle(int x, int y, int r) {
		g.fillOval(x - r, y - r, 2 * r, 2 * r);
	}

	public void drawCircle(int x, int y, int r) {
		g.drawOval(x - r, y - r, 2 * r, 2 * r);
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
	}

	public void drawWideLine(int x1, int y1, int x2, int y2, float width,
			Color col) {
		final Stroke old = g.getStroke(), wide = new BasicStroke(width,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		final Color c = g.getColor();
		g.setColor(col);
		g.setStroke(wide);
		g.drawLine(x1, y1, x2, y2);
		g.setStroke(old);
		g.setColor(c);
	}

	public void drawWideLine(int x1, int y1, int x2, int y2, float width) {
		drawWideLine(x1, y1, x2, y2, width, new Color(230, 230, 230));
	}

	public void drawWideLine(int x1, int y1, int x2, int y2) {
		drawWideLine(x1, y1, x2, y2, 27.0f, new Color(230, 230, 230));
	}

	public void drawDashedLine(int x1, int y1, int x2, int y2) {
		final float dash1[] = { 2.0f, 5.0f };
		final Stroke old = g.getStroke(), dashed = new BasicStroke(1.0f,
				BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
				0.0f);
		g.setStroke(dashed);
		g.drawLine(x1, y1, x2, y2);
		g.setStroke(old);
	}

	// square; x, y is the center
	public void drawSquare(int x, int y, int a) {
		g.drawRect(x - a, y - a, 2 * a, 2 * a);
	}

	public int stringWidth(String str, Fonts f) {
		return f.fm.stringWidth(str);
	}

	public void drawString(String str, int x, int y, Fonts f) {
		x -= f.fm.stringWidth(str) / 2;
		y -= f.fm.getHeight() / 2 - f.fm.getAscent();
		g.setFont(f.font);
		g.drawString(str, x, y);
	}

	public void drawStringLeft(String str, int x, int y, Fonts f) {
		x -= f.fm.stringWidth(str);
		y -= f.fm.getHeight() / 2 - f.fm.getAscent();
		g.setFont(f.font);
		g.drawString(str, x, y);
	}

	public void drawStringTop(String str, int x, int y, Fonts f) {
		x -= f.fm.stringWidth(str) / 2;
		y -= f.fm.getHeight();
		g.setFont(f.font);
		g.drawString(str, x, y);
	}

	public void fillArc(int x, int y, int w, int h, int a1, int a2) {
		g.fillArc(x, y, w, h, a1, a2);
	}

	public void drawRoundRectangle(int x, int y, double w, double h,
			double arcw, double arch) {
		g.draw(new RoundRectangle2D.Double(x - w, y - h, 2 * w, 2 * h, arcw,
				arch));
	}

	public void fillRoundRectangle(int x, int y, double w, double h,
			double arcw, double arch) {
		g.fill(new RoundRectangle2D.Double(x - w, y - h, 2 * w, 2 * h, arcw,
				arch));
	}

	private void arrowHead(int x, int y, int xx, int yy) {
		double alpha = 6.0, beta = 0.93;
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
		g.drawLine(x1, y1, x2, y2);
		arrowHead(x1, y1, x2, y2);
	}

	public void drawDoubleArrow(int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
		arrowHead(x1, y1, x2 + 2, y2);
		arrowHead(x2, y2, x1 - 2, y1);
	}

	// elliptical arc
	// x,y,w,h is the bounding rectangle
	// a1,a2 is the starting and ending angle in degrees
	public void drawArc(int x, int y, int w, int h, int a1, int a2) {
		g.drawArc(x, y, w, h, a1, a2 - a1);
	}

	// let A=[x1,y1] and B=[x2,y2]
	// _B--\___/--B
	// _____\_/
	// ______|
	// ______A
	// ______|
	// _____/_\
	// _B--/___\--B
	//
	public void drawQuarterArc(int x1, int y1, int x2, int y2) {
		int w = Math.abs(x1 - x2), h = Math.abs(y1 - y2), a1, a2;
		if (y2 < y1) {
			if (x2 < x1) {
				a1 = 0;
				a2 = 90;
			} else {
				a1 = 90;
				a2 = 180;
			}
		} else {
			if (x2 > x1) {
				a1 = 180;
				a2 = 270;
			} else {
				a1 = 270;
				a2 = 360;
			}
		}
		drawArc(x2 - w, y1 - h, 2 * w, 2 * h, a1, a2);
	}

	public void drawFancyArc(int x1, int y1, int x3, int y3) {
		g.draw(new CubicCurve2D.Float(x1, y1, x1, y1 + 10, x3, y3 - 40, x3, y3));
	}

	public void drawArcArrow(int x, int y, int w, int h, int a1, int a2) {
		drawArc(x, y, w, h, a1, a2);
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
		arrowHead(x, y, x2, y2);
	}

	public void drawCurve(int x1, int y1, int cx1, int cy1, int cx2, int cy2,
			int x2, int y2) {
		g.draw(new CubicCurve2D.Float(x1, y1, cx1, cy1, cx2, cy2, x2, y2));
	}

	public void fillPolygon(Polygon p) {
		final Stroke old = g.getStroke(), wide = new BasicStroke(27.0f,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		final Color c = g.getColor();
		g.setColor(new Color(230, 230, 230));
		g.setStroke(wide);
		g.fillPolygon(p);
		g.drawPolygon(p);
		g.setStroke(old);
		g.setColor(c);
	}

	public void drawImage(Image img, int x, int y, int w, int h) {
		g.drawImage(img, x, y, w, h, null);
	}

	public void setDS(ClickListener D) {
		this.D = D;
	}
}
