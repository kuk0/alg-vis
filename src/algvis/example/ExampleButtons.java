package algvis.example;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;

import algvis.core.Buttons;
import algvis.core.VisPanel;
import algvis.internationalization.IButton;

// the only button in our example is the "Move" button
public class ExampleButtons extends Buttons {
	private static final long serialVersionUID = 8846562679618844866L;
	IButton moveB;

	public ExampleButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void actionButtons(JPanel P) {
		moveB = new IButton(M.a, "button-move");
		moveB.setMnemonic(KeyEvent.VK_M);
		moveB.addActionListener(this);
		P.add(moveB);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == moveB) {
			// we read the numbers from the input field
			Vector<Integer> args = I.getVI();
			// we add two zeros (in case that nothing was typed in) 
			args.add(0); args.add(0);
			// and take just the first two numbers -- these will form the input 
			final int x = args.get(0);
			final int y = args.get(1);
			Thread t = new Thread(new Runnable() {
				public void run() {
					((ExampleDS) D).move(x,y);
				}
			});
			t.start();
		}
	}

	@Override
	public void enableNext() {
		super.enableNext();
		moveB.setEnabled(false);
	}

	@Override
	public void disableNext() {
		super.disableNext();
		moveB.setEnabled(true);
	}

	@Override
	public void refresh() {
		super.refresh();
		moveB.refresh();
	}
}
