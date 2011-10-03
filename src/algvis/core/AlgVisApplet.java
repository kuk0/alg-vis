package algvis.core;

import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

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

		AlgVis A = new AlgVis(this.getRootPane());
		A.setSize(WIDTH, HEIGHT); // same size as defined in the HTML APPLET
		add(A);
		A.init();
	}
}
