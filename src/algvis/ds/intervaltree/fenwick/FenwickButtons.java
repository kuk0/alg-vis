package algvis.ds.intervaltree.fenwick;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import algvis.internationalization.IButton;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

public class FenwickButtons extends Buttons {

	private static final long serialVersionUID = 1141788026164919081L;

	private IButton insertB;

	protected FenwickButtons(VisPanel panel) {
		super(panel);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void actionButtons(JPanel P) {
		insertB = new IButton("button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		P.add(insertB);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);

		if (evt.getSource() == insertB) {
			for (int x : I.getNonEmptyVI()) {
				D.insert(x);
			}
		}
	}

}
