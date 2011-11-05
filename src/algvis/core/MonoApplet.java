package algvis.core;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import algvis.internationalization.Languages;

public class MonoApplet extends JApplet {
	private static final long serialVersionUID = -1266240067665543602L;
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
		setSize(WIDTH, HEIGHT+20);
		
		Languages L = new Languages();
		// getParameter("lang");
		L.selectLanguage(0);
		
		// getParameter("ds");
		int i = 0;
		VisPanel P = DataStructures.getPanel(i, L);
		P.setSize(WIDTH, HEIGHT); // same size as defined in the HTML APPLET
		if (P != null) add(P);

		Fonts.init(getGraphics());
	}
}
