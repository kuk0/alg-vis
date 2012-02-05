package algvis.scapegoattree;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import algvis.core.DictButtons;
import algvis.core.VisPanel;

public class GBButtons extends DictButtons implements ChangeListener {
	private static final long serialVersionUID = -4200856610929537432L;
	JSpinner AS;
	JLabel alpha;

	public GBButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void otherButtons(JPanel P) {
		alpha = new JLabel("\u03B1:");
		AS = new JSpinner(new SpinnerNumberModel(1.3, 1.01, 5, 0.1));
		AS.addChangeListener(this);
		P.add(alpha);
		P.add(AS);
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == AS) {
			((GBTree) D).alpha = (Double) AS.getValue();
		}
	}
}
