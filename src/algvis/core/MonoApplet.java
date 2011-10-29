package algvis.core;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import algvis.bst.BSTPanel;
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

		Languages L = new Languages();
		L.selectLanguage(0);
		Fonts.init(getGraphics());
		VisPanel A = new BSTPanel(L);
		A.setSize(WIDTH, HEIGHT); // same size as defined in the HTML APPLET
		this.getRootPane().add(A);		
		A.init();
	}
}
