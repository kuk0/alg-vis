package algvis.redblacktree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import algvis.core.DictButtons;
import algvis.core.VisPanel;
import algvis.internationalization.ICheckBox;

public class RBButtons extends DictButtons implements ActionListener {
	private static final long serialVersionUID = 5601437441473816995L;
	ICheckBox B24;

	public RBButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void otherButtons(JPanel P) {
/*		B24 = new ICheckBox(M.S.L, "mode234", false);
		B24.setMnemonic(KeyEvent.VK_2);
		B24.addActionListener(this);
		P.add(B24);*/
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == B24) {
			((RB) M.D).mode24 = B24.isSelected();
			((RB) M.D).reposition();
		}
	}
}
