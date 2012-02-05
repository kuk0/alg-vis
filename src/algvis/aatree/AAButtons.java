package algvis.aatree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import algvis.core.DictButtons;
import algvis.core.VisPanel;
import algvis.internationalization.ICheckBox;

public class AAButtons extends DictButtons implements ActionListener {
	private static final long serialVersionUID = 5326663225787843118L;
	ICheckBox B23;

	public AAButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void otherButtons(JPanel P) {
		B23 = new ICheckBox(M.S.L, "mode23", false);
		B23.setMnemonic(KeyEvent.VK_2);
		B23.addActionListener(this);
		P.add(B23);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == B23) {
			((AA) M.D).setMode23(B23.isSelected());
		}
	}
}
