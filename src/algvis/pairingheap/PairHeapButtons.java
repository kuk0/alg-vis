package algvis.pairingheap;

import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import algvis.core.MeldablePQButtons;
import algvis.core.VisPanel;
import algvis.internationalization.IComboBox;
import algvis.internationalization.ILabel;
//import algvis.unionfind.UnionFindUnion;
//import algvis.pairingheap.PairHeapDelete.Pairing;

public class PairHeapButtons extends MeldablePQButtons implements ChangeListener{
	private static final long serialVersionUID = -5692673269308125662L;
	IComboBox pairVariant;
	
	public PairHeapButtons(VisPanel M) {
		super(M);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionButtons(JPanel P) {
		super.actionButtons(P);
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		super.stateChanged(arg0);
		
		
		
	}
	
	@Override
	public JPanel initThirdRow() {
		JPanel P = new JPanel();

		ILabel fhLabel = new ILabel(M.S.L, "pairing");
		String[] pairh = { "pairingnaive", "pairinglrrl"};//, "pairingfb", "pairingbf", "pairingmultipass", "pairinglazymulti" };

		pairVariant = new IComboBox(M.S.L, pairh);

		pairVariant.addActionListener(this);

		P.add(fhLabel);
		P.add(pairVariant);
		return P;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == pairVariant) {
			int i = pairVariant.getSelectedIndex();

			if ((i >= 0) || (i < 2)){	//(i < 6)){
				((PairingHeap)D).pairState = PairHeapDelete.Pairing.values()[i];
			}
		}
	}
}
