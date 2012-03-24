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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JPanel;

public class Screen extends JPanel implements Runnable {
	private static final long serialVersionUID = -8279768206774288161L;
	// obrazovka (ak nie je suspendnuta) neustale vykresluje poziciu
	Thread t = null;
	boolean suspended;
	DataStructure D = null;
	public Scene S;

	VisPanel P;
	Image I;
	Graphics G;
	public View V;
	Dimension size;

	public Screen(VisPanel P) {
		this.P = P;
		V = new View(this);
	}

	public void setDS(DataStructure D) {
		this.D = D;
	}

	public void setScene(Scene S) {
		this.S = S;
	}

	void check_size() {
		Dimension d = getSize();
		if (I == null || d.width != size.width || d.height != size.height) {
			I = createImage(d.width, d.height);
			G = I.getGraphics();
			// V.setWH(d.width, d.height);
			V.setGraphics((Graphics2D) G, d.width, d.height);
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
		//if (V==null) System.out.println("bu");
		//if (G==null) System.out.println("ba");
		clear();
		if (S != null) {
			V.startDrawing();
			S.move();
			S.draw(V);
			V.endDrawing();
		}
		/*if (D != null) {
			V.startDrawing();
			D.draw(V);
			V.endDrawing();
			// V.resetView();
		} else {
			System.out.println("[DS null !]");
		}*/
		g.drawImage(I, 0, 0, null);
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

	@Override
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
}
