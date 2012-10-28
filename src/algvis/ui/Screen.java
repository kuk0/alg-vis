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
package algvis.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;
import javax.swing.Timer;

import algvis.core.DataStructure;
import algvis.ui.view.View;

public class Screen extends JPanel {
	private static final long serialVersionUID = -8279768206774288161L;
	// obrazovka (ak nie je suspendnuta) neustale vykresluje poziciu
	private final Timer timer;
	private DataStructure D = null;
	private final VisPanel panel;

	private Image I;
	private Graphics G;
	private Dimension size;

	public final View V;

	public Screen(VisPanel panel) {
		this.panel = panel;
		V = new View(this);
		timer = new Timer(50, new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				repaint();
			}
		});
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
			try {
				panel.scene.move();
				panel.scene.draw(V);
			} catch (ConcurrentModificationException ignored) {
			}
			V.endDrawing();
			// V.resetView();
		} else {
			System.err.println("[DS null !]");
		}
		g.drawImage(I, 0, 0, null);
	}

	public void suspend() {
		timer.stop();
	}

	public void resume() {
		timer.start();
	}

	public void start() {
		timer.start();
	}
}
