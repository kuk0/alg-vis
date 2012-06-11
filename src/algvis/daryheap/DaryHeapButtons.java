package algvis.daryheap;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import algvis.core.PQButtons;
import algvis.core.VisPanel;
import algvis.internationalization.ILabel;

public class DaryHeapButtons extends PQButtons implements ChangeListener {
	private static final long serialVersionUID = -2783154701649865993L;
	JSpinner OS;
	ILabel orderLabel;

	

	public DaryHeapButtons(VisPanel M) {
		super(M);
	}

	@Override
	public void otherButtons(JPanel P) {
		OS = new JSpinner(new SpinnerNumberModel(5, 2, 20, 1));
		OS.addChangeListener(this);
		orderLabel = new ILabel(M.S.L, "daryheaporder");
		P.add(orderLabel);
		P.add(OS);	

		super.otherButtons(P);
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() == OS) {
			((DaryHeap) D).order = (Integer) OS.getValue();
			((DaryHeap) D).clear();
			((DaryHeap) D).reposition();
		}
	}

}
