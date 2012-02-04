package algvis.bst;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import algvis.core.DictButtons;
import algvis.core.VisPanel;
import algvis.internationalization.ICheckBox;

public class BSTButtons extends DictButtons {
	private static final long serialVersionUID = -6884955717665753504L;
	ICheckBox order;

	public BSTButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void otherButtons(JPanel P) {
		order = new ICheckBox(M.S.L, "show-order", false);
		order.setMnemonic(KeyEvent.VK_2);
		order.addActionListener(this);
		P.add(order);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == order) {
			((BST) M.D).order = order.isSelected();
		}
	}
}
