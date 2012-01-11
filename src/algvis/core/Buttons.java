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
	IButton next, clear, random, snapshot;
	ICheckBox pause;
	ChLabel stats;
	JButton zoomIn, zoomOut, resetView;
	int snap=0;

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
		initSnapshot();
		initZoom();
		JPanel second = new JPanel();
		second.setLayout(new FlowLayout());
		second.add(pause);
		second.add(clear);
		second.add(random);
		second.add(snapshot);
		// second.add(zoomLabel);
		second.add(zoomIn);
		second.add(zoomOut);
		second.add(resetView);

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
				.createTitledBorder(M.S.L.getString("control")), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
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

	public void initSnapshot() {
		snapshot = new IButton(M.S.L, "button-snapshot");
		snapshot.setMnemonic(KeyEvent.VK_S);
		snapshot.addActionListener(this);
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
//		zoomIn = createButton("+", "../images/zoom_in.gif");
		zoomIn = createButton("+", "/algvis/images/zoom_in.gif");
		zoomOut = createButton("-", "/algvis/images/zoom_out.gif");
		resetView = createButton("R", "/algvis/images/reset.gif");
		zoomIn.addActionListener(this);
		zoomOut.addActionListener(this);
		resetView.addActionListener(this);
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
			M.screen.V.zoomIn();
		} else if (evt.getSource() == zoomOut) {
			M.screen.V.zoomOut();
		} else if (evt.getSource() == resetView) {
			M.screen.V.resetView();
		} else if (evt.getSource() == snapshot) {
			/*try {
			     // bi; // = (BufferedImage) M.screen.I; // retrieve image
			    View V = M.screen.V;
			    Point2D p = V.v2r(V.minx, V.miny), q = V.v2r(V.maxx, V.maxy);
			    int x1 = (int)p.getX(), y1 = (int)p.getY(), x2 = (int)q.getX(), y2 = (int)q.getY(),
			    	w = x2 - x1, h = y2 - y1; 
			    BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			    bi.getGraphics().drawImage(M.screen.I, 0, 0, w, h, x1, y1, x2, y2, null);
			    int i = 0;
			    File outputfile;
			    while ((outputfile = new File(String.format("%3d", i)+".png")).exists()) ++i;
			    ImageIO.write(bi, "png", outputfile);
			} catch (IOException e) {
			}*/

			if (snap == 0) {
				System.out.println("#!/usr/bin/python\n" +
						"from pyks import *\n" +
						"from trees import *");
				System.out.println();
			}
			System.out.println("\n" +
					"def s"+snap+"():\n" +
					"  clear()\n" +
					"  setuv(0.032, -0.032)");
			M.screen.V.output = true;
			D.draw(M.screen.V);
			M.screen.V.output = false;
			System.out.println("\n" +
					"new()\n" +
					"s"+snap+"()\n" +
					"drawedges()\n" +
					"drawnodes()\n" +
					"save(\"s"+snap+".pdf\")");
			++snap;
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
