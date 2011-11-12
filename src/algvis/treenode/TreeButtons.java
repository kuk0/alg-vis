package algvis.treenode;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.core.Buttons;
import algvis.core.VisPanel;
import algvis.internationalization.IButton;

public class TreeButtons extends Buttons {
	private static final long serialVersionUID = -4975466786632165055L;
	IButton appendB;
	
	public TreeButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		appendB = new IButton(M.L, "button-insert");
		appendB.setMnemonic(KeyEvent.VK_I);
		appendB.addActionListener(this);
		
		P.add(appendB);
	}

	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == appendB) {
			final Vector<Integer> args = I.getNonEmptyVI();
			Thread t = new Thread(new Runnable() {
				public void run() {
					for (int x : args) {
						((TreeDS) D).insert(x);
						// System.out.print(x+"\n");
					}
				}
			});
			t.start();
		}
	}
		
	@Override
	public void enableNext() {
		super.enableNext();
		appendB.setEnabled(false);
	}

	@Override
	public void disableNext() {
		super.disableNext();
		appendB.setEnabled(true);
	}

	@Override
	public void refresh() {
		super.refresh();
		appendB.refresh();
	}

}
