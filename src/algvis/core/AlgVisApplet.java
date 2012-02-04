package algvis.core;

import java.awt.Color;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;

import algvis.internationalization.Languages;

public class AlgVisApplet extends JApplet {
	private static final long serialVersionUID = -76009301274562874L;
	static final int WIDTH = 900, HEIGHT = 600;

	@Override
	public void init() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.put("nimbusBase", new Color(0xBB, 0xC3, 0xFF));
					UIManager.put("TitledBorder.position", TitledBorder.CENTER);
					UIManager
							.put("nimbusBlueGrey", new Color(0xD1, 0xD1, 0xD1));
					UIManager.put("control", new Color(0xFA, 0xFA, 0xFA));
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}

		/**
		 * choose data structure depending on the "ds" parameter in the <applet>
		 * tag if ds is a number from 0 to N-1, we will have an applet with just
		 * a single data structure, otherwise, include all of them
		 */
		int ds = -1;
		String dsp = getParameter("ds");
		try {
			ds = Integer.parseInt(dsp);
			if (ds < 0 || ds >= DataStructures.N)
				ds = -1;
		} catch (NumberFormatException e) {
			ds = DataStructures.getIndex(dsp);
		}
		if (ds == -1) {
			// all data structures
			AlgVis A = new AlgVis(this.getRootPane(), getParameter("lang"));
			A.setSize(WIDTH, HEIGHT); // same size as defined in the HTML APPLET
			add(A);
			A.init();
		} else {
			// data structure ds
			Languages L = new Languages(getParameter("lang"));
			Settings S = new Settings(L);
			VisPanel P = DataStructures.getPanel(ds, S);
			P.setSize(WIDTH, HEIGHT); // same size as defined in the HTML APPLET
			if (P != null)
				add(P);
			Fonts.init(getGraphics());
		}
	}
}
