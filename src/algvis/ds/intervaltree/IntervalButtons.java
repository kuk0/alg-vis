package algvis.ds.intervaltree;

import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ds.intervaltree.IntervalTrees.mimasuType;
import algvis.gui.Buttons;
import algvis.gui.VisPanel;
import algvis.internationalization.IButton;
import algvis.internationalization.IRadioButton;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class IntervalButtons extends Buttons{

	private static final long serialVersionUID = 6383200811481633404L;
	private IButton insertB;
    private IButton findsumB;
    private IButton changeKeyB;
	private IRadioButton minB;
    private IRadioButton maxB;
    private IRadioButton sumB;
	private ButtonGroup minMaxSumGroup;

	public IntervalButtons(VisPanel M) {
		super(M);
	}
	
	@Override
	public void actionButtons(JPanel P) {
		insertB = new IButton("button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		/*
		deleteB = new IButton("button-deletemax");
		deleteB.setMnemonic(KeyEvent.VK_D);
		deleteB.addActionListener(this);
		*/
		
		if (((IntervalTrees) D).minTree == mimasuType.MIN) {
			findsumB = new IButton("button-findmin");
		} else if (((IntervalTrees) D).minTree == mimasuType.MAX){
			findsumB = new IButton("button-findmax");
		} else {
			findsumB = new IButton("button-findsum");
		}
		findsumB.setMnemonic(KeyEvent.VK_I);
		findsumB.addActionListener(this);
		
		changeKeyB = new IButton("button-changekey");
		changeKeyB.setMnemonic(KeyEvent.VK_K);
		changeKeyB.addActionListener(this);

		P.add(insertB);
		//P.add(deleteB);
		P.add(findsumB);
		P.add(changeKeyB);
		
	}
	
	@Override
	public void otherButtons(JPanel P) {
		minB = new IRadioButton("min");
		minB.setSelected(false);
		minB.addActionListener(this);
		maxB = new IRadioButton("max");
		maxB.setSelected(true);
		maxB.addActionListener(this);
		sumB = new IRadioButton("sum");
		sumB.setSelected(false);
		sumB.addActionListener(this);
		minMaxSumGroup = new ButtonGroup();
		minMaxSumGroup.add(minB);
		minMaxSumGroup.add(maxB);
		minMaxSumGroup.add(sumB);
		P.add(minB);
		P.add(maxB);
		P.add(sumB);		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getSource() == insertB) {
			if (panel.history.canRedo()) panel.newAlgorithmPool();
			Vector<Integer> args = I.getNonEmptyVI();
			for (int x : args) {
				D.insert(x);
			}
		} else if (evt.getSource() == findsumB) {
			if (panel.history.canRedo()) panel.newAlgorithmPool();
			Vector<Integer> args = I.getVI();
			if (args.size() > 1) {
				((IntervalTrees) D).ofinterval(args.elementAt(0), args.elementAt(1));
			} else {
				((IntervalTrees) D).ofinterval(1, ((IntervalTree) D).numLeafs);
			}
		} else if (evt.getSource() == changeKeyB) {
			if (panel.history.canRedo()) panel.newAlgorithmPool();
			int delta = Math.abs(I.getInt(1));
			BSTNode w = ((BSTNode) ((IntervalTrees) D).chosen);
			((IntervalTrees) D).changeKey(w, delta);
		} else if (evt.getSource() == minB && ((IntervalTrees) D).minTree != mimasuType.MIN) {
			D.clear();
			findsumB.setT("button-findmin");
			((IntervalTrees) D).minTree = mimasuType.MIN;
		} else if (evt.getSource() == maxB && ((IntervalTrees) D).minTree != mimasuType.MAX) {
			D.clear();
			findsumB.setT("button-findmax");
			((IntervalTrees) D).minTree = mimasuType.MAX;
		} else if (evt.getSource() == sumB && ((IntervalTrees) D).minTree != mimasuType.SUM) {
			D.clear();
			findsumB.setT("button-findsum");
			((IntervalTrees) D).minTree = mimasuType.SUM;
		}
	}

	@Override
	public void setOtherEnabled(boolean enabled) {
		super.setOtherEnabled(enabled);
		insertB.setEnabled(enabled);
		findsumB.setEnabled(enabled);
		//deleteB.setEnabled(enabled);
		changeKeyB.setEnabled(enabled);
	}
}
