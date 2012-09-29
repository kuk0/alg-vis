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

import algvis.core.history.UpdatableStateEdit;
import algvis.core.visual.VisualElement;
import algvis.gui.VisPanel;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * The Class Algorithm. Each visualized data structure consists of data and
 * algorithms (such as insert, delete) that update the data. All such algorithms
 * are descendants of the class Algorithm.
 * <p/>
 * A visualized algorithm has its own thread which can be suspended (e.g., after
 * each step of the algorithm; see method pause) and is automatically
 * resumed (method myresume) after pressing the "Next" button.
 */
abstract public class Algorithm implements Runnable {
	private final VisPanel panel;
	private final Semaphore gate = new Semaphore(1);
	private volatile boolean done = false;
	private UpdatableStateEdit panelState;
	private boolean wrapped = false;
	private Algorithm wrapperAlg;

	protected Algorithm(VisPanel panel) {
		this.panel = panel;
		try {
			gate.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected Algorithm(VisPanel panel, Algorithm a) {
		this(panel);
		if (a != null) {
			wrapped = true;
			wrapperAlg = a;
		}
	}

	@Override
	public void run() {
		panel.D.A = this;
		begin();
		try {
			runAlgorithm();
		} catch (InterruptedException e) {
			this.done = true;
			panel.history.trimToEnd();
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					panel.refresh();
				}
			});
//			e.printStackTrace();
			return;
		}
		end();
	}

	public abstract void runAlgorithm() throws InterruptedException;

	protected void pause() throws InterruptedException {
		if (wrapped) {
			wrapperAlg.pause();
		} else {
			panelState.end();
			if (panel.pauses) {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						panel.refresh();
					}
				});
				gate.acquire();
			}
			panel.history.addEdit(panelState = new UpdatableStateEdit(panel, panel.history.getNextId()));
		}
	}
	
	public void resume() {
		if (wrapped) {
			wrapperAlg.resume();
		} else {
			gate.release();
		}
	}

	protected void setHeader(String s) {
		panel.commentary.setHeader(s);
	}

	protected void setHeader(String s, String... par) {
		panel.commentary.setHeader(s, par);
	}

	protected void setHeader(String s, int... par) {
		panel.commentary.setHeader(s, par);
	}

	protected void addNote(String s) {
		panel.commentary.addNote(s);
	}

	public void addNote(String s, String[] par) {
		panel.commentary.addNote(s, par);
	}

	protected void addNote(String s, int... par) {
		panel.commentary.addNote(s, par);
	}

	protected void addStep(String s) {
		panel.commentary.addStep(s);
	}

	protected void addStep(String s, String... par) {
		panel.commentary.addStep(s, par);
	}

	protected void addStep(String s, int... par) {
		panel.commentary.addStep(s, par);
	}
	
	protected void addToScene(VisualElement element) {
		if (wrapped) {
			wrapperAlg.addToScene(element);
		} else {
			element.addToScene();
			panelState.addToPreState(element);
		}
	}

	protected void removeFromScene(VisualElement element) {
		element.removeFromScene();
	}

	void begin() {
		panel.history.addEdit(panelState = new UpdatableStateEdit(panel, panel.history.getNextId()));
		panel.commentary.clear();
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				panel.buttons.refresh();
			}
		});
	}

	void end() {
		panel.D.setStats();
		panelState.end();
		panel.history.putAlgorithmEnd();
		
		this.done = true;
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				panel.refresh();
			}
		});
	}

	public boolean isDone() {
		return done;
	}
	
	public abstract HashMap<String, Object> getResult();
}
