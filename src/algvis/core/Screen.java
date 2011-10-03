package algvis.core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

public class Screen extends JPanel implements Runnable, MouseListener,
		MouseMotionListener, MouseWheelListener {
	private static final long serialVersionUID = -8279768206774288161L;
	// obrazovka (ak nie je suspendnuta) neustale vykresluje poziciu
	Thread t = null;
	boolean suspended;
	DataStructure D = null;

	VisPanel P;
	Image I;
	Graphics G;
	public View V;
	Dimension size;

	public Screen(VisPanel P) {
		this.P = P;
		V = new View(P);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}

	public void setDS(DataStructure D) {
		this.D = D;
	}

	void check_size() {
		Dimension d = getSize();
		if (I == null || d.width != size.width || d.height != size.height) {
			I = createImage(d.width, d.height);
			G = I.getGraphics();
			V.setWH(d.width, d.height);
			size = d;
		}
	}

	void clear() {
		G.setColor(Color.white);
		G.fillRect(0, 0, size.width, size.height);
	}

	@Override
	public void paint(Graphics g) {
		check_size();
		clear();
		if (D != null) {
			D.draw(G, V);
		} else {
			System.out.println("[DS null !]");
		}
		g.drawImage(I, 0, 0, null);
		// System.out.println ("screen paint");
	}

	public void suspend() {
		suspended = true;
	}

	public void resume() {
		if (suspended) {
			suspended = false;
			synchronized (this) {
				notify();
			}
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			suspended = false;
			t.start();
		}
	}

	public void run() {
		// repaint();
		try {
			while (true) {
				if (suspended) {
					synchronized (this) {
						while (suspended) {
							wait();
						}
					}
				}
				repaint();
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
		}
	}

	int mx, my; // mouse position

	public void mousePressed(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX(), y = e.getY();
		P.hSlider.setValue(P.hSlider.getValue() + mx - x);
		P.vSlider.setValue(P.vSlider.getValue() + my - y);
		mx = x;
		my = y;
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		D.mouseClicked(V.r2vX(e.getX()), V.r2vY(e.getY()));
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		if (notches > 0) {
			V.zoomOut();
		} else {
			V.zoomIn();
		}
	}
}
