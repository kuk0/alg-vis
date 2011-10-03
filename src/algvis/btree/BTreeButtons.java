package algvis.btree;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import algvis.core.DictButtons;
import algvis.core.VisPanel;
import algvis.internationalization.ILabel;

public class BTreeButtons extends DictButtons implements ChangeListener {
	private static final long serialVersionUID = -4573594717377516312L;
	JSpinner OS;
	ILabel orderLabel;

	public BTreeButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void otherButtons(JPanel P) {
		OS = new JSpinner(new SpinnerNumberModel(5, 3, 20, 1));
		OS.addChangeListener(this);
		orderLabel = new ILabel(M.a, "btreeorder");
		P.add(orderLabel);
		P.add(OS);
	}

	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == OS) {
			((BTree) D).order = (Integer) OS.getValue();
			((BTree) D).clear();
			((BTree) D).reposition();
		}
	}

	@Override
	public void refresh() {
		super.refresh();
		orderLabel.refresh();
	}
}
