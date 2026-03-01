/*******************************************************************************
 * Copyright (c) 2012-present Jakub Kováč, Jozef Brandýs, Katarína Kotrlová,
 * Pavol Lukča, Ladislav Pápay, Viktor Tomkovič, Tatiana Tóthová
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
package algvis.ui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;

import algvis.ds.DS;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

public class AlgVisStandalone {
    public static void main(String[] args) {
        try {
            for (final LookAndFeelInfo info : UIManager
                .getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.put("nimbusBase", new Color(0xBB, 0xC3, 0xFF));
                    UIManager.put("TitledBorder.position", TitledBorder.CENTER);
                    UIManager.put("nimbusBlueGrey",
                        new Color(0xD1, 0xD1, 0xD1));
                    UIManager.put("control", new Color(0xFA, 0xFA, 0xFA));
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (final Throwable t) {
            // Nimbus not available, or native calls fail (e.g. under CheerpJ).
            // Fall through with the default look-and-feel.
        }

        if (args.length > 0 && "help".equalsIgnoreCase(args[0])) {
            System.out.println("Available data structures:");
            for (DS d : DS.values()) {
                String name = d.getName();
                if (name == null) {
                    name = d.name().toLowerCase();
                }
                System.out.println(" - " + name);
            }
            return;
        }

        boolean undecorated = false;
        for (String arg : args) {
            if ("--undecorated".equalsIgnoreCase(arg)) {
                undecorated = true;
            }
        }

        final boolean undec = undecorated;
        final Runnable showFrame = () -> {
            DS ds = null;
            if (args.length > 0) {
                for (DS d : DS.values()) {
                    if (d.getName().equals(args[0])) {
                        ds = d;
                        break;
                    }
                }
            }
            final JFrame f = new MainFrame(ds, undec);
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.setVisible(true);
        };

        try {
            EventQueue.invokeLater(showFrame);
        } catch (final Throwable t) {
            // EventQueue may fail under CheerpJ; try direct invocation.
            showFrame.run();
        }
    }
}

class MainFrame extends JFrame {
    private static final long serialVersionUID = -1045189076645432320L;

    public MainFrame(DS ds, boolean undecorated) {
        if (undecorated) {
            setUndecorated(true);
        } else {
            setTitle("Gnarley Trees");
        }

        if (ds != null) {
            final algvis.ui.VisPanel P = ds.createPanel();
            add(P);
            pack();
            Fonts.init(getGraphics());
            P.setOnAir(true);
        } else {
            final AlgVis A = new AlgVis(getContentPane());
            add(A);
            pack();
            A.init();
        }
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
