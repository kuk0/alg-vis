/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package algvis.gui;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
import java.awt.*;


public class AlgVisStandalone {
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
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new MainFrame();
				f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				f.setVisible(true);
			}
		});
	}
}

class MainFrame extends JFrame {
	private static final int WIDTH = 1080;
	private static final int HEIGHT = 680;

	public MainFrame() {
		setTitle("Gnarley Trees");
		AlgVis A = new AlgVis(getRootPane());
		A.setSize(WIDTH, HEIGHT);
		add(A);
		pack();
		A.init();
		setSize(WIDTH, HEIGHT + 20); // add 20 for the frame title
	}
}
