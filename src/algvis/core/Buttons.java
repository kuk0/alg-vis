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
	IButton previous, next, clear, random, save;
	ICheckBox pause;
	ChLabel stats;
	JButton zoomIn, zoomOut, resetView;
	private Thread scenarioTraverser;

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

		// put everything together
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(first);
		add(second);
		add(third);
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(M.S.L.getString("control")),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
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
		// initSave();
		initZoom();
		second.setLayout(new FlowLayout());
		second.add(pause);
		second.add(clear);
		second.add(random);
		// second.add(save);
		// second.add(zoomLabel);
		second.add(zoomIn);
		second.add(zoomOut);
		second.add(resetView);
		otherButtons(second);
		return second;
	}

	// statistics
	public JPanel initThirdRow() {
		JPanel third = new JPanel();
		third.setLayout(new FlowLayout());
		stats = new ChLabel(D.stats());
		third.add(stats);
		return third;
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

	public void actionPerformed(ActionEvent evt) {
		if (scenarioTraverser != null) {
			while (scenarioTraverser.isAlive()) {
				scenarioTraverser.interrupt();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
		if (evt.getSource() == previous) {
			scenarioTraverser = new Thread(new Runnable() {
				public void run() {
					if (D.scenario.hasPrevious()) {
						D.scenario.previous();
					}
					if (!D.scenario.hasPrevious()) {
						disablePrevious();
					}
					enableNext();
				}
			});
			scenarioTraverser.start();
		} else if (evt.getSource() == next) {
			scenarioTraverser = new Thread(new Runnable() {
				public void run() {
					if (D.scenario.hasNext()) {
						D.scenario.next();
						if (!D.scenario.hasNext() && !D.A.suspended) {
							disableNext();
						}
					} else if (D.A.suspended) {
						D.next();
					}
					enablePrevious();
				}
			});
			scenarioTraverser.start();
			// System.out.println("next");
			// repaint();
		} else if (evt.getSource() == clear) {
			D.clear();
			// repaint();
		} else if (evt.getSource() == random) {
			D.random(I.getInt(10));
		} else if (evt.getSource() == pause) {
			M.pause = pause.isSelected();
			D.scenario.setPauses(pause.isSelected());
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
		stats.setText(s);
		stats.refresh();
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
