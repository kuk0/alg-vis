package algvis.unionfind;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.core.Buttons;
import algvis.core.Dictionary;
import algvis.core.UnionFind;
import algvis.core.VisPanel;
import algvis.internationalization.IButton;

public class UnionFindButtons extends Buttons {
	private static final long serialVersionUID = 2683381160819263717L;
	IButton makesetB, unionB;
	
	
	public UnionFindButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		makesetB = new IButton(M.L, "button-makeset");
		makesetB.setMnemonic(KeyEvent.VK_M);
		makesetB.addActionListener(this);

		unionB = new IButton(M.L, "button-union");
		unionB.setMnemonic(KeyEvent.VK_U);
		unionB.addActionListener(this);

		P.add(makesetB);
		P.add(unionB);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == makesetB) {
			final Vector<Integer> args = I.getNonEmptyVI();
			// is this thread necessary?
			Thread t = new Thread(new Runnable() {
				public void run() {
					for (int x : args) {
						for (int j = 0; j < x; j++) {
							((UnionFind) D).makeSet();
						}
					}
				}
			});
			t.start();
		} else if (evt.getSource() == unionB) {
			final Vector<Integer> args = I.getNonEmptyVI();
			if (args.size() < 2) { return; }
			// is this thread necessary?
			Thread t = new Thread(new Runnable() {
				public void run() {
					((UnionFind) D).union(args.elementAt(0), args.elementAt(1));
				}
			});
			t.start();
		}

	}
}
