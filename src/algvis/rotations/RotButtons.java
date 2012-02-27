package algvis.rotations;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.core.Buttons;
import algvis.core.VisPanel;
import algvis.internationalization.IButton;
import algvis.internationalization.ICheckBox;

public class RotButtons extends Buttons {
	private static final long serialVersionUID = 3851020370059429766L;
	IButton rotB;
	ICheckBox order, subtrees;

	public RotButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		rotB = new IButton(M.S.L, "button-rotate");
		rotB.setMnemonic(KeyEvent.VK_T);
		rotB.addActionListener(this);
		P.add(rotB);
	}


	@Override
	public void otherButtons(JPanel P) {
		order = new ICheckBox(M.S.L, "show-order", false);
		order.setMnemonic(KeyEvent.VK_O);
		order.addActionListener(this);
		P.add(order);

		subtrees = new ICheckBox(M.S.L, "show-subtrees", false);
		subtrees.setMnemonic(KeyEvent.VK_S);
		subtrees.addActionListener(this);
		P.add(subtrees);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		final Rotations R = (Rotations) D;
		if (evt.getSource() == rotB) {
			final Vector<Integer> args = I.getNonEmptyVI();
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int x : args) {
						R.rotate(x);
					}
				}
			});
			t.start();
		} else if (evt.getSource() == order) {
			R.T.order = order.isSelected();
		} else if (evt.getSource() == subtrees) {
			R.subtrees = subtrees.isSelected(); 
		}
	}

	@Override
	public void enableNext() {
		super.enableNext();
		rotB.setEnabled(false);
	}

	@Override
	public void disableNext() {
		super.disableNext();
		rotB.setEnabled(true);
	}
}
