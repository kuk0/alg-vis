package algvis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

public class AAButtons extends DictButtons implements ActionListener {
	private static final long serialVersionUID = 5326663225787843118L;
	ICheckBox B23;

	public AAButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void otherButtons(JPanel P) {
		B23 = new ICheckBox(M.a, "mode23", false);
		B23.setMnemonic(KeyEvent.VK_2);
		B23.addActionListener(this);
		P.add(B23);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == B23) {
			((AA) M.D).mode23 = B23.isSelected();
			((AA) M.D).reposition();
		}
	}

	@Override
	public void refresh() {
		super.refresh();
		B23.refresh();
	}
}
