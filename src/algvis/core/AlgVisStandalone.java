package algvis.core;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class AlgVisStandalone {
	static final int WIDTH = 1080, HEIGHT = 680; 
	
	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					//UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}

		JFrame f = new JFrame("Gnarled trees");
		f.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});

		AlgVis A = new AlgVis(f.getRootPane());
		A.setSize(WIDTH, HEIGHT);
		f.getContentPane().add(A);
		f.pack();
		A.init();
		f.setSize(WIDTH, HEIGHT + 20); // add 20 for the frame title
		f.setVisible(true);
	}
}
