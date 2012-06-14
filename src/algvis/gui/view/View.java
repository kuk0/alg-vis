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
package algvis.gui.view;

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
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import algvis.gui.Fonts;

public class View implements MouseListener, MouseMotionListener,
		MouseWheelListener {
	private Graphics2D g;
	final static double SCALE_FACTOR = 1.1, MIN_ZOOM = 0.16, MAX_ZOOM = 5.5;
	public int W, H; // display width&height
	public int minx, miny, maxx, maxy;
	private int mouseX, mouseY; // mouse position
	public Alignment align = Alignment.CENTER;

	private double x, y, f; // TODO not needed 120429
	public final AffineTransform at;
	private AffineTransform oldTransform;
	private ClickListener D;

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

	public Point2D cut (double x, double y, double x2, double y2, double c) {
		double d = new Point2D.Double(x, y).distance(x2, y2);
		return new Point2D.Double(x + (x2-x)*(d-c)/d, y + (y2-y)*(d-c)/d);
	}

	public void fillSqr(double x, double y, double a) {
		g.fillRect((int) (x - a), (int) (y - a), 2 * (int) a, 2 * (int) a);
	}

	public void drawSqr(double x, double y, double a) {
		g.drawRect((int) (x - a), (int) (y - a), 2 * (int) a, 2 * (int) a);
	}

	public void fillOval(double x, double y, double a, double b) {
		g.fillOval((int) x, (int) y, (int) a, (int) b);
	}

	public void drawOval(double x, double y, double a, double b) {
		g.drawOval((int) x, (int) y, (int) a, (int) b);
	}

	public void fillCircle(double x, double y, double r) {
		g.fillOval((int) (x - r), (int) (y - r), 2 * (int) r, 2 * (int) r);
	}

	public void drawCircle(double x, double y, double r) {
		g.drawOval((int) (x - r), (int) (y - r), 2 * (int) r, 2 * (int) r);
	}

	public void drawLine(double x1, double y1, double x2, double y2) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}

	public void drawWideLine(double x1, double y1, double x2, double y2,
			float width, Color col) {
		final Stroke old = g.getStroke(), wide = new BasicStroke(width,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		final Color c = g.getColor();
		g.setColor(col);
		g.setStroke(wide);
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		g.setStroke(old);
		g.setColor(c);
	}

	public void drawWideLine(double x1, double y1, double x2, double y2,
			float width) {
		drawWideLine(x1, y1, x2, y2, width, new Color(230, 230, 230));
	}

	public void drawWideLine(double x1, double y1, double x2, double y2) {
		drawWideLine(x1, y1, x2, y2, 27.0f, new Color(230, 230, 230));
	}

	public void drawDashedLine(double x1, double y1, double x2, double y2) {
		final float dash1[] = { 2.0f, 5.0f };
		final Stroke old = g.getStroke(), dashed = new BasicStroke(1.0f,
				BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1,
				0.0f);
		g.setStroke(dashed);
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		g.setStroke(old);
	}

	// square; x, y is the center
	public void drawSquare(double x, double y, double a) {
		g.drawRect((int) (x - a), (int) (y - a), 2 * (int) a, 2 * (int) a);
	}

	public int stringWidth(String str, Fonts f) {
		return f.fm.stringWidth(str);
	}

	public void drawString(String str, double x, double y, Fonts f) {
		x -= f.fm.stringWidth(str) / 2;
		y -= f.fm.getHeight() / 2 - f.fm.getAscent();
		g.setFont(f.font);
		g.drawString(str, (int) x, (int) y);
	}

	public void drawStringLeft(String str, double x, double y, Fonts f) {
		x -= f.fm.stringWidth(str);
		y -= f.fm.getHeight() / 2 - f.fm.getAscent();
		g.setFont(f.font);
		g.drawString(str, (int) x, (int) y);
	}

	public void drawStringTop(String str, double x, double y, Fonts f) {
		x -= f.fm.stringWidth(str) / 2;
		y -= f.fm.getHeight();
		g.setFont(f.font);
		g.drawString(str, (int) x, (int) y);
	}

	public void drawVerticalString(String str, double x, double y, Fonts f) {
		int xx = (int)x;
		int yy = (int)y - str.length() * f.fm.getHeight() / 2;
		g.setFont(f.font);
		for (int i=0; i<str.length(); ++i) {
			g.drawString(""+str.charAt(i), xx, yy);
			yy += f.fm.getHeight();
		}
	}

	public void fillArc(double x, double y, double w, double h, double a1,
			double a2) {
		g.fillArc((int) x, (int) y, (int) w, (int) h, (int) a1, (int) a2);
	}

	public void drawRoundRectangle(double x, double y, double w, double h,
			double arcw, double arch) {
		g.draw(new RoundRectangle2D.Double(x - w, y - h, 2 * w, 2 * h, arcw,
				arch));
	}

	public void fillRoundRectangle(double x, double y, double w, double h,
			double arcw, double arch) {
		g.fill(new RoundRectangle2D.Double(x - w, y - h, 2 * w, 2 * h, arcw,
				arch));
	}

	private void arrowHead(double x, double y, double xx, double yy) {
		double alpha = 6.0, beta = 1.5;
		double vecX, vecY, normX, normY, d, th, ta, baseX, baseY;

		// build the line vector
		vecX = xx - x;
		vecY = yy - y;

		// build the arrow base vector - normal to the line
		normX = -vecY;
		normY = vecX;

		// setup length parameters
		d = Math.sqrt(vecX * vecX + vecY * vecY);
		th = alpha / (2.0 * d);
		ta = alpha / (beta * d);

		// find the base of the arrow
		baseX = xx - ta * vecX;
		baseY = yy - ta * vecY;

		// build the points on the sides of the arrow
		Path2D p = new Path2D.Double();
		p.moveTo(xx + vecX * 2 / d, yy + vecY * 2 / d);
		p.lineTo(baseX + th * normX, baseY + th * normY);
		p.lineTo(baseX - th * normX, baseY - th * normY);
		p.closePath();
		g.fill(p);
	}

	public void drawArrow(double x1, double y1, double x2, double y2) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		arrowHead((int)x1, (int)y1, (int)x2, (int)y2);
	}

	public void drawDoubleArrow(double x1, double y1, double x2, double y2) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
		arrowHead((int)x1, (int)y1, (int)x2, (int)y2);
		arrowHead((int)x2, (int)y2, (int)x1, (int)y1);
	}

	// elliptical arc
	// x,y,w,h is the bounding rectangle
	// a1,a2 is the starting and ending angle in degrees
	public void drawArc(double x, double y, double w, double h, double a1,
			double a2) {
		g.drawArc((int) x, (int) y, (int) w, (int) h, (int) a1, (int) (a2 - a1));
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
	public void drawQuarterArc(double x1, double y1, double x2, double y2) {
		double w = Math.abs(x1 - x2), h = Math.abs(y1 - y2), a1, a2;
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

	public void drawFancyArc(double x1, double y1, double x3, double y3) {
		g.draw(new CubicCurve2D.Double(x1, y1, x1, y1 + 10, x3, y3 - 40, x3, y3));
	}

	public void drawArcArrow(double x, double y, double w, double h, double a1,
			double a2) {
		drawArc(x, y, w, h, a1, a2);
		double a = a2 * Math.PI / 180;
		double x2 = x + w / 2.0 * (1 + Math.cos(a)), y2 = y + h / 2.0
				* (1 - Math.sin(a));
		double dx = 128 * Math.sin(a), dy = 128 * Math.cos(a);
		if (a1 <= a2) {
			x = x2 + dx;
			y = y2 + dy;
		} else {
			x = x2 - dx;
			y = y2 - dy;
		}
		arrowHead(x, y, x2, y2);
	}

	public void drawCurve(double x1, double y1, double cx1, double cy1, double cx2, double cy2,
			double x2, double y2) {
		g.draw(new CubicCurve2D.Double(x1, y1, cx1, cy1, cx2, cy2, x2, y2));
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

	public void drawImage(Image img, double x, double y, double w, double h) {
		g.drawImage(img, (int)x, (int)y, (int)w, (int)h, null);
	}

	public void setDS(ClickListener D) {
		this.D = D;
	}
}
