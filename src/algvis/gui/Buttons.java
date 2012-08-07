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

import algvis.core.Algorithm;
import algvis.core.AlgorithmAdapter;
import algvis.core.history.HashtableStoreSupport;
import algvis.ds.DataStructure;
import algvis.internationalization.ChLabel;
import algvis.internationalization.IButton;
import algvis.internationalization.ICheckBox;

import javax.swing.*;
import javax.swing.undo.StateEditable;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

/**
 * The Class Buttons. This is a panel with standard buttons such as input field,
 * the "Next", "Clear", and "Random" buttons, "Pause" checkbox, label with
 * statistics, and "Zoom in/Zoom out" buttons. Panels with data
 * structure-specific buttons (such "Insert" or "Delete") are created by
 * extending this class (see for example classes DictButtons, PQButtons).
 */
abstract public class Buttons extends JPanel implements ActionListener, StateEditable {
	private static final long serialVersionUID = 1417759004124906334L;
	protected final VisPanel panel;
	protected final DataStructure D;
	public InputField I;
	private IButton previous;
    protected IButton next;
    private IButton clear;
    protected IButton random;
    private IButton save;
	private ICheckBox pause;
	private ChLabel stats;
	private String statsText;
	private JButton zoomIn;
    private JButton zoomOut;
    private JButton resetView;
	private final String hash = Integer.toString(hashCode());

	protected abstract void actionButtons(JPanel P);

	protected void actionButtons2(JPanel P) {
	}

	protected Buttons(VisPanel panel) {
		this.panel = panel;
		D = panel.D;
		assert D != null : "data structure not initialized yet";

		JPanel first = initFirstRow();
		JPanel second = initSecondRow();
		JPanel third = initThirdRow();
		JPanel statsPanel = initStats();

		// put everything together
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(first);
		add(second);
		if (third != null)
			add(third);
		add(statsPanel);
		setBorder(BorderFactory.createTitledBorder(""));
	}

	// input field, actions (insert, find, delete,...), previous, next
    JPanel initFirstRow() {
		JPanel first = new JPanel();
		first.setLayout(new FlowLayout());

		I = new InputField(5, panel.statusBar, panel.S);
		first.add(I);
		actionButtons(first);
		initPrevious();
		initNext();
		first.add(previous);
		first.add(next);
		actionButtons2(first);
		return first;
	}

	// [x] pause, clear, random, zoom in/out
    JPanel initSecondRow() {
		JPanel second = new JPanel();
		initPause();
		initClear();
		initRandom();
		// initSave();
		initZoom();
		second.setLayout(new FlowLayout());
		second.add(pause);
		second.add(clear);
		if (random != null)
			second.add(random);
		// second.add(save);
		// second.add(zoomLabel);
		// second.add(zoomIn);
		// second.add(zoomOut);
		second.add(resetView);
		otherButtons(second);
		return second;
	}

	protected JPanel initThirdRow() {
		return null;
	}

	// statistics
    JPanel initStats() {
		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new FlowLayout());
		stats = new ChLabel(D.stats());
		statsText = stats.getText();
		statsPanel.add(stats);
		return statsPanel;
	}

	void initPrevious() {
		previous = new IButton("previous");
		previous.setMnemonic(KeyEvent.VK_O);
		previous.setEnabled(false);
		previous.addActionListener(this);
	}

	void initNext() {
		next = new IButton("next");
		next.setMnemonic(KeyEvent.VK_N);
		next.setEnabled(false);
		next.addActionListener(this);
	}

	void initPause() {
		pause = new ICheckBox("button-pause", true);
		pause.setMnemonic(KeyEvent.VK_P);
		pause.addActionListener(this);
	}

	void initClear() {
		clear = new IButton("button-clear");
		clear.setMnemonic(KeyEvent.VK_C);
		clear.addActionListener(this);
	}

	protected void initRandom() {
		random = new IButton("button-random");
		random.setMnemonic(KeyEvent.VK_R);
		random.addActionListener(this);
	}

	// TODO
//	public void initSave() {
//		save = new IButton("button-save");
//		save.setMnemonic(KeyEvent.VK_S);
//		save.setEnabled(panel.scenario.isEnabled());
//		save.addActionListener(this);
//	}

	private JButton createButton(String alt, String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new JButton(new ImageIcon(imgURL));
		} else {
			System.err.println("Couldn't find file: " + path);
			return new JButton(alt);
		}
	}

	void initZoom() {
		// zoomLabel = new ILabel("zoomio");
		// zoomIn = createButton("+", "../images/zoom_in.gif");
		zoomIn = createButton("+", "/algvis/images/zoom_in.gif");
		zoomOut = createButton("-", "/algvis/images/zoom_out.gif");
		resetView = createButton("R", "/algvis/images/reset.gif");
		zoomIn.addActionListener(this);
		zoomOut.addActionListener(this);
		resetView.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		I.sb.setText(" ");
		if (evt.getSource() == previous) {
			if (panel.history.canUndo()) {
				if (panel.pauses) {
					panel.history.undo();
				} else {
					panel.history.undoAlgorithm();
				}
				panel.refresh();
			}
		} else if (evt.getSource() == next) {
			Algorithm a = panel.D.getA();
			if (a != null && !a.isDone() && !panel.history.canRedo()) {
				a.resume();
			} else if (panel.history.canRedo()) {
				if (panel.pauses) {
					panel.history.redo();
				} else {
					panel.history.redoAlgorithm();
				}
				panel.refresh();
			}
		} else if (evt.getSource() == clear) {
			if (panel.history.canRedo()) panel.newAlgorithmPool();
			D.start(new AlgorithmAdapter(panel) {
				@Override
				public void runAlgorithm() throws InterruptedException {
					D.clear();
				}
			});
		} else if (evt.getSource() == random) {
			D.random(I.getInt(10));
		} else if (evt.getSource() == pause) {
			panel.pauses = pause.isSelected();
		} else if (evt.getSource() == zoomIn) {
			panel.screen.V.zoomIn();
		} else if (evt.getSource() == zoomOut) {
			panel.screen.V.zoomOut();
		} else if (evt.getSource() == save) {
			// TODO
		} else if (evt.getSource() == resetView) {
			panel.screen.V.resetView();
		}
		I.requestFocusInWindow();
	}
	
	public void setOtherEnabled(boolean enabled) {
		clear.setEnabled(enabled);
		random.setEnabled(enabled);
	}
	
	public void refresh() {
		previous.setEnabled(panel.history.canUndo());
		next.setEnabled(panel.history.canRedo() || (panel.D.getA() != null && !panel.D.getA().isDone()));
		setOtherEnabled(panel.history.isBetweenAlgorithms());
		refreshStats();
	}
	
	public void setStats(String s) {
		statsText = s;
	}
	
	void refreshStats() {
		if (!statsText.equals(stats.getText())) {
			stats.setText(statsText);
			stats.refresh();
		}
	}

	protected void otherButtons(JPanel P) {
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(700, 150);
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(300, 150);
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		HashtableStoreSupport.store(state, hash + "statsText", stats.getText());
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		Object statsText = state.get(hash + "statsText");
		if (statsText != null) this.statsText = (String) HashtableStoreSupport.restore(statsText);
	}
}
