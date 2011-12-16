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
 * The Class Buttons.
 * This is a panel with standard buttons such as input field, the "Next", "Clear", and "Random" buttons,
 * "Pause" checkbox, label with statistics, and "Zoom in/Zoom out" buttons.
 * Panels with data structure-specific buttons (such "Insert" or "Delete") are created by
 * extending this class (see for example classes DictButtons, PQButtons). 
 */
abstract public class Buttons extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1417759004124906334L;
	public VisPanel M;
	public DataStructure D;
	public InputField I;
	IButton next, clear, random;
	ICheckBox pause;
	ChLabel stats;
	JButton zoomIn, zoomOut;

	// ILabel zoomLabel;
	abstract public void actionButtons(JPanel P);

	public Buttons(VisPanel M) {
		this.M = M;
		D = M.D;
		assert D != null : "data structure not initialized yet";

		// first
		JPanel first = new JPanel();
		first.setLayout(new FlowLayout());

		I = new InputField(5, M.statusBar);
		first.add(I);
		actionButtons(first);
		initNext();
		first.add(next);

		// second
		initPause();
		initClear();
		initRandom();
		initZoom();
		JPanel second = new JPanel();
		second.setLayout(new FlowLayout());
		second.add(pause);
		second.add(clear);
		second.add(random);
		// second.add(zoomLabel);
		second.add(zoomIn);
		second.add(zoomOut);

		otherButtons(second);

		// third
		JPanel third = new JPanel();
		third.setLayout(new FlowLayout());

		stats = new ChLabel(D.stats());
		third.add(stats);

		// put everything together
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(first);
		add(second);
		add(third);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(M.L.getString("control")), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
	}

	public void initNext() {
		next = new IButton(M.L, "next");
		next.setMnemonic(KeyEvent.VK_N);
		next.setEnabled(false);
		next.addActionListener(this);
	}

	public void initPause() {
		pause = new ICheckBox(M.L, "button-pause", true);
		pause.setMnemonic(KeyEvent.VK_P);
		pause.addActionListener(this);
	}

	public void initClear() {
		clear = new IButton(M.L, "button-clear");
		clear.setMnemonic(KeyEvent.VK_C);
		clear.addActionListener(this);
	}

	public void initRandom() {
		random = new IButton(M.L, "button-random");
		random.setMnemonic(KeyEvent.VK_R);
		random.addActionListener(this);
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
		// zoomLabel = new ILabel(M.L, "zoomio");
		zoomIn = createButton("+", "/algvis/images/zoom_in.gif");
		zoomOut = createButton("-", "/algvis/images/zoom_out.gif");
		zoomIn.addActionListener(this);
		zoomOut.addActionListener(this);
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == next) {
			D.next();
			// System.out.println("next");
			// repaint();
		} else if (evt.getSource() == clear) {
			D.clear();
			// repaint();
		} else if (evt.getSource() == random) {
			D.random(I.getInt(10));
		} else if (evt.getSource() == pause) {
			M.pause = pause.isSelected();
		} else if (evt.getSource() == zoomIn) {
			M.S.V.zoomIn();
		} else if (evt.getSource() == zoomOut) {
			M.S.V.zoomOut();
		}
	}

	public void enableNext() {
		clear.setEnabled(false);
		random.setEnabled(false);
		next.setEnabled(true);
	}

	public void disableNext() {
		clear.setEnabled(true);
		random.setEnabled(true);
		next.setEnabled(false);
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
