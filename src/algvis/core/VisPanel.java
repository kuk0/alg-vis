package algvis.core;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import algvis.internationalization.ILabel;
import algvis.internationalization.Languages;

public abstract class VisPanel extends JPanel implements ChangeListener {
	private static final long serialVersionUID = 5104769085118210624L;
	public static Class<? extends DataStructure> DS;

	// aplet pozostava z piatich zakladnych veci:
	public Buttons B; // gombikov (dolu)
	public Commentary C; // komentara (vpravo)
	public DataStructure D; // datovej struktury
	public Screen S; // obrazovky v strede
	public ILabel statusBar; // a status baru

	JSlider vSlider, hSlider;

	public Languages L;

	int STEPS = 10;
	public boolean pause = true, small = false;

	public VisPanel(Languages L) {
		this.L = L;
		init();
	}

	public String getTitle() {
		try {
			return (String)(DS.getDeclaredField("dsName").get(null));
		} catch (Exception e) {
			System.out.println ("VisPanel is unable to get field dsName - name of data structure: " + DS);
		}
		return "";
	}

	public void init() {
		this.setLayout(new GridBagLayout());
		JPanel screen = initScreen();
		JScrollPane commentary = initCommentary();
		statusBar = new ILabel(L, "EMPTYSTR");
		initDS();
		
		GridBagConstraints cs = new GridBagConstraints();
		cs.gridx = 0;
		cs.gridy = 0;
		cs.fill = GridBagConstraints.BOTH;
		add(screen, cs);
		
		GridBagConstraints cc = new GridBagConstraints();
		cc.gridx = 1;
		cc.gridy = 0;
		cc.gridheight = 2;
		cc.fill = GridBagConstraints.VERTICAL;
		add(commentary, cc);

		GridBagConstraints cb = new GridBagConstraints();
		cb.gridx = 0;
		cb.gridy = 1;
		cb.fill = GridBagConstraints.HORIZONTAL;
		add(B, cb);

		GridBagConstraints csb = new GridBagConstraints();
		csb.gridx = 0;
		csb.gridy = 2;
		csb.fill = GridBagConstraints.HORIZONTAL;
		add(statusBar, csb);
		
		S.setDS(D);
		S.start();
	}

	public JPanel initScreen() {
		JPanel screen = new JPanel();
		screen.setLayout(new BorderLayout());
		S = new Screen(this) {
			private static final long serialVersionUID = 2196788670749006364L;

			@Override
			public Dimension getMaximumSize() {
				return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(700, 400);
			}

			@Override
			public Dimension getMinimumSize() {
				return new Dimension(700, 400);
//				return new Dimension(300, 100);
			}
		};
		screen.add(S, BorderLayout.CENTER);

		vSlider = new JSlider(SwingConstants.VERTICAL, 0, 100, 50);
		vSlider.addChangeListener(this);
		vSlider.setInverted(true);
		screen.add(vSlider, BorderLayout.WEST);

		hSlider = new JSlider(SwingConstants.HORIZONTAL, 0, 100, 50);
		hSlider.addChangeListener(this);
		screen.add(hSlider, BorderLayout.SOUTH);

		screen.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(L.getString("display")), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
		return screen;
		// left.add(screen, BorderLayout.CENTER);
	}

	public JScrollPane initCommentary() {
		// Commentary
		JScrollPane SP = new JScrollPane() {
			private static final long serialVersionUID = -8618469733328277117L;

			@Override
			public Dimension getMaximumSize() {
				return new Dimension(400, 680);
			}

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(300, 600);
			}

			@Override
			public Dimension getMinimumSize() {
				return new Dimension(300, 600);
				//return new Dimension(200, 530);
			}
		};
		C = new Commentary(L, SP);
		SP.setViewportView(C);
		JPanel CP = new JPanel();
		CP.add(SP);
		SP.setBorder(BorderFactory.createCompoundBorder(BorderFactory
				.createTitledBorder(L.getString("text")), BorderFactory
				.createEmptyBorder(5, 5, 5, 5)));
		return SP;
	}

	abstract public void initDS();

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (true) { // !source.getValueIsAdjusting()) {
			int val = source.getValue();
			int or = source.getOrientation();
			if (or == SwingConstants.VERTICAL) {
				S.V.setY(val);
			} else if (or == SwingConstants.HORIZONTAL) {
				S.V.setX(val);
			}
		}
	}
	
	/*public void showStatus (String t) {
		statusBar.setT(t);
	}*/
}
