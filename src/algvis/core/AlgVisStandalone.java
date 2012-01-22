package algvis.core;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;

public class AlgVisStandalone {
	static final int WIDTH = 1080, HEIGHT = 680;

	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.put("nimbusBase", new Color(0xBB, 0xC3, 0xFF));
					UIManager.put("TitledBorder.position", TitledBorder.CENTER);
					UIManager.put("nimbusBlueGrey", new Color(0xD1, 0xD1, 0xD1));
					UIManager.put("control", new Color(0xFA, 0xFA, 0xFA));
					UIManager.setLookAndFeel(info.getClassName());
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
