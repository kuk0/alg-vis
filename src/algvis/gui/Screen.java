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
package algvis.gui;

import algvis.ds.DataStructure;
import algvis.gui.view.View;

import javax.swing.*;
import java.awt.*;

public class Screen extends JPanel implements Runnable {
	private static final long serialVersionUID = -8279768206774288161L;
	// obrazovka (ak nie je suspendnuta) neustale vykresluje poziciu
	private final Thread t;
	private boolean suspended = true;
	private DataStructure D = null;
	private final VisPanel panel;

	private Image I;
	private Graphics G;
	private Dimension size;
	
	public final View V;

	public Screen(VisPanel panel) {
		this.panel = panel;
		V = new View(this);
		t = new Thread(this);
	}

	public void setDS(DataStructure D) {
		this.D = D;
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
	public void paintComponent(Graphics g) {
		check_size();
		clear();
		if (D != null) {
			V.startDrawing();
			panel.scene.move();
			panel.scene.draw(V);
			V.endDrawing();
			// V.resetView();
		} else {
			System.err.println("[DS null !]");
		}
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
		//suspended = false;
		if (!t.isAlive()) {
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
