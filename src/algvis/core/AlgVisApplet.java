package algvis.core;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import algvis.internationalization.Languages;

public class AlgVisApplet extends JApplet {
	private static final long serialVersionUID = -76009301274562874L;
	static final int WIDTH = 1080, HEIGHT = 680; 

	@Override
	public void init() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}
		
		/**
		 * choose data structure depending on the DS parameter in the <applet> tag
		 * if DS is a number from 0 to N-1, we will have an applet with just a single
		 * data structure, otherwise, include all of them  
		 */
		int ds = -1;
		try {
			ds = Integer.parseInt(getParameter("DS"));
			if (ds < 0 || ds >= DataStructures.N) ds = -1;
		} catch (NumberFormatException e) {
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
			VisPanel P = DataStructures.getPanel(ds, L);
			P.setSize(WIDTH, HEIGHT); // same size as defined in the HTML APPLET
			if (P != null) add(P);
			Fonts.init(getGraphics());
		}
	}
}