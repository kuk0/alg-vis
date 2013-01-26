package algvis.ds.intervaltree.fenwick;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.internationalization.IButton;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

public class FenwickButtons extends Buttons {

	private static final long serialVersionUID = 1141788026164919081L;

	private IButton insertB;
	private IButton sumB;

	protected FenwickButtons(VisPanel panel) {
		super(panel); 
	}

	@Override
	protected void actionButtons(JPanel P) {
		insertB = new IButton("button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);
		
		sumB = new IButton("button-sum");
		sumB.setMnemonic(KeyEvent.VK_S);
		sumB.addActionListener(this);

		P.add(insertB);
		P.add(sumB);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);

		if (evt.getSource() == insertB) {
			for (int x : I.getNonEmptyVI()) {
				D.insert(x);
			}
		} else if (evt.getSource() == sumB) {
			Vector<Integer> args = I.getVI();
			if (args.size() > 0) {
				((FenwickTree)D).prefixSum(args.elementAt(0));
			} else {
				// Sum entire tree by default
				((FenwickTree)D).prefixSum(((FenwickTree)D).root.countLeaves());
			}
		}
	}
	
	@Override
	public void setOtherEnabled(boolean enabled) {
		super.setOtherEnabled(enabled);
		insertB.setEnabled(enabled);
		sumB.setEnabled(enabled);
	}

}
