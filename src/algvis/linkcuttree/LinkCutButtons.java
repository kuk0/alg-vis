package algvis.linkcuttree;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.JPanel;
import algvis.gui.Buttons;
import algvis.gui.VisPanel;
import algvis.internationalization.IButton;

public class LinkCutButtons extends Buttons {
	private static final long serialVersionUID = -1694041230198532334L;
	IButton addB, linkB, cutB;

	public LinkCutButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		linkB = new IButton(M.S.L, "button-link");
		linkB.setMnemonic(KeyEvent.VK_L);
		linkB.addActionListener(this);

		cutB = new IButton(M.S.L, "button-cut");
		cutB.setMnemonic(KeyEvent.VK_C);
		cutB.addActionListener(this);
		
		addB = new IButton(M.S.L, "button-makeset");
		addB.setMnemonic(KeyEvent.VK_A);
		addB.addActionListener(this);

		P.add(linkB);
		P.add(cutB);
		P.add(addB);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		final LinkCutDS D = (LinkCutDS) this.D;
		if (evt.getSource() == linkB) {
			final Vector<Integer> args = I.getVI(1, D.max-1);
			if (args.size() > 1) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						((LinkCutDS) D).link(args.elementAt(0) ,args.elementAt(1) );					
					}
				});
				t.start();
			}
		} else if (evt.getSource() == cutB) {
			final Vector<Integer> args = I.getVI();
			if (args.size() > 0) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						((LinkCutDS) D).cut(args.elementAt(0));
					}
				});
				t.start();
			}
		} else if (evt.getSource() == addB) {
			final int N = I.getInt(10, 1, 1000);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					((LinkCutDS) D).addElements(N);
				}
			});
			t.start();
		}
	}
	
	@Override
	public void disableAll() {
		super.disableAll();
		addB.setEnabled(false);
		linkB.setEnabled(false);
		cutB.setEnabled(false);
	}

	@Override
	public void enableAll() {
		super.enableAll();
		addB.setEnabled(true);
		linkB.setEnabled(true);
		cutB.setEnabled(true);
	}

}
