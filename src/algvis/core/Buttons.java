package algvis.core;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import algvis.internationalization.ChLabel;
import algvis.internationalization.IButton;
import algvis.internationalization.ICheckBox;
import algvis.scenario.commands.SetStatsCommand;

/**
 * The Class Buttons. This is a panel with standard buttons such as input field,
 * the "Next", "Clear", and "Random" buttons, "Pause" checkbox, label with
 * statistics, and "Zoom in/Zoom out" buttons. Panels with data
 * structure-specific buttons (such "Insert" or "Delete") are created by
 * extending this class (see for example classes DictButtons, PQButtons).
 */
abstract public class Buttons extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1417759004124906334L;
	public VisPanel M;
	public DataStructure D;
	public InputField I;
	public IButton previous, next, clear, random, save;
	ICheckBox pause;
	ChLabel stats;
	JButton zoomIn, zoomOut, resetView;

	abstract public void actionButtons(JPanel P);

	public void actionButtons2(JPanel P) {
	}

	public Buttons(VisPanel M) {
		this.M = M;
		D = M.D;
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
	public JPanel initFirstRow() {
		JPanel first = new JPanel();
		first.setLayout(new FlowLayout());

		I = new InputField(5, M.statusBar);
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
	public JPanel initSecondRow() {
		JPanel second = new JPanel();
		initPause();
		initClear();
		initRandom();
		initSave();
		initZoom();
		second.setLayout(new FlowLayout());
		second.add(pause);
		second.add(clear);
		second.add(random);
		second.add(save);
		// second.add(zoomLabel);
		// second.add(zoomIn);
		// second.add(zoomOut);
		second.add(resetView);
		otherButtons(second);
		return second;
	}

	public JPanel initThirdRow() {
		return null;
	}

	// statistics
	public JPanel initStats() {
		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new FlowLayout());
		stats = new ChLabel(D.stats());
		statsPanel.add(stats);
		return statsPanel;
	}

	public void initPrevious() {
		previous = new IButton(M.S.L, "previous");
		previous.setMnemonic(KeyEvent.VK_O);
		previous.setEnabled(false);
		previous.addActionListener(this);
		previous.setVisible(D.scenario.isEnabled());
	}

	public void initNext() {
		next = new IButton(M.S.L, "next");
		next.setMnemonic(KeyEvent.VK_N);
		next.setEnabled(false);
		next.addActionListener(this);
	}

	public void initPause() {
		pause = new ICheckBox(M.S.L, "button-pause", true);
		pause.setMnemonic(KeyEvent.VK_P);
		pause.addActionListener(this);
	}

	public void initClear() {
		clear = new IButton(M.S.L, "button-clear");
		clear.setMnemonic(KeyEvent.VK_C);
		clear.addActionListener(this);
	}

	public void initRandom() {
		random = new IButton(M.S.L, "button-random");
		random.setMnemonic(KeyEvent.VK_R);
		random.addActionListener(this);
	}

	public void initSave() {
		save = new IButton(M.S.L, "button-save");
		save.setMnemonic(KeyEvent.VK_S);
		save.setEnabled(D.scenario.isEnabled());
		save.addActionListener(this);
	}

	private JButton createButton(String alt, String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new JButton(new ImageIcon(imgURL));
		} else {
			System.err.println("Couldn't find file: " + path);
			return new JButton(alt);
		}
	}

	public void initZoom() {
		// zoomLabel = new ILabel(M.S.L, "zoomio");
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
		if (evt.getSource() == previous) {
			disablePrevious();
			D.scenario.previous(M.pause, true);
		} else if (evt.getSource() == next) {
			if (!D.scenario.isEnabled() || !D.scenario.hasNext()) {
				D.next();
			} else {
				disableNext();
				D.scenario.next(M.pause, true);
			}
			// repaint();
		} else if (evt.getSource() == clear) {
			D.clear();
			update();
			// repaint();
		} else if (evt.getSource() == random) {
			D.random(I.getInt(10));
		} else if (evt.getSource() == pause) {
			M.pause = pause.isSelected();
		} else if (evt.getSource() == zoomIn) {
			M.screen.V.zoomIn();
		} else if (evt.getSource() == zoomOut) {
			M.screen.V.zoomOut();
		} else if (evt.getSource() == save) {
			D.scenario.saveXML("test.xml");
		} else if (evt.getSource() == resetView) {
			M.screen.V.resetView();
		}
	}

	public void update() {
		if (D.scenario.isAlgorithmRunning() || (D.A != null && D.A.suspended)) {
			disableAll();
		} else {
			enableAll();
		}
		if (D.scenario.hasNext() || (D.A != null && D.A.suspended)) {
			enableNext();
		} else {
			disableNext();
		}
		if (D.scenario.hasPrevious()) {
			enablePrevious();
		} else {
			disablePrevious();
		}
	}

	public void enableNext() {
		next.setEnabled(true);
	}

	public void disableNext() {
		next.setEnabled(false);
	}

	public void enablePrevious() {
		if (D.scenario.isEnabled()) {
			previous.setEnabled(true);
		}
	}

	public void disablePrevious() {
		previous.setEnabled(false);
	}

	/**
	 * enables all buttons except previous and next
	 */
	public void enableAll() {
		clear.setEnabled(true);
		random.setEnabled(true);
	}

	/**
	 * disables all buttons except previous and next
	 */
	public void disableAll() {
		clear.setEnabled(false);
		random.setEnabled(false);
	}

	public void setStats(String s) {
		String oldText = stats.getText();
		if (oldText != s) {
			if (D.scenario.isAddingEnabled()) {
				D.scenario.add(new SetStatsCommand(this, oldText, s));
			}
			stats.setText(s);
			stats.refresh();
		}
	}

	public void otherButtons(JPanel P) {
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
}
