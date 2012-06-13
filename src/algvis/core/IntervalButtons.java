package algvis.core;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import algvis.bst.BSTNode;
import algvis.core.IntervalTrees.mimasuType;
import algvis.internationalization.IButton;
import algvis.internationalization.IRadioButton;

public class IntervalButtons extends Buttons{

	private static final long serialVersionUID = 6383200811481633404L;
	IButton insertB, findsumB, changeKeyB;
	IRadioButton minB, maxB, sumB;
	ButtonGroup minMaxSumGroup;

	public IntervalButtons(VisPanel M) {
		super(M);
	}
	
	@Override
	public void actionButtons(JPanel P) {
		insertB = new IButton(M.S.L, "button-insert");
		insertB.setMnemonic(KeyEvent.VK_I);
		insertB.addActionListener(this);

		/*
		deleteB = new IButton(M.S.L, "button-deletemax");
		deleteB.setMnemonic(KeyEvent.VK_D);
		deleteB.addActionListener(this);
		*/
		
		if (((IntervalTrees) D).minTree == mimasuType.MIN) {
			findsumB = new IButton(M.S.L, "button-findmin");
		} else if (((IntervalTrees) D).minTree == mimasuType.MAX){
			findsumB = new IButton(M.S.L, "button-findmax");
		} else {
			findsumB = new IButton(M.S.L, "button-findsum");
		}
		findsumB.setMnemonic(KeyEvent.VK_I);
		findsumB.addActionListener(this);
		
		changeKeyB = new IButton(M.S.L, "button-changekey");
		changeKeyB.setMnemonic(KeyEvent.VK_K);
		changeKeyB.addActionListener(this);

		P.add(insertB);
		//P.add(deleteB);
		P.add(findsumB);
		P.add(changeKeyB);
		
	}
	
	@Override
	public void otherButtons(JPanel P) {
		minB = new IRadioButton(M.S.L, "min");
		minB.setSelected(false);
		minB.addActionListener(this);
		maxB = new IRadioButton(M.S.L, "max");
		maxB.setSelected(true);
		maxB.addActionListener(this);
		sumB = new IRadioButton(M.S.L, "sum");
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
			final Vector<Integer> args = I.getNonEmptyVI();
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int x : args) {
						((IntervalTrees) D).insert(x);
					}
				}
			});
			t.start();
		} else if (evt.getSource() == findsumB) {
			final Vector<Integer> args = I.getVI();
			if (args.size() > 1) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						((IntervalTrees) D).ofinterval(args.elementAt(0), args.elementAt(1));
					}
				});
				t.start();
			}
		} else if (evt.getSource() == changeKeyB) {
			final int delta = Math.abs(I.getInt(1));
			final BSTNode w = ((BSTNode) ((IntervalTrees) D).chosen);
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					((IntervalTrees) D).changeKey(w, delta);
				}
			});
			t.start();
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
	public void enableNext() {
		super.enableNext();
		insertB.setEnabled(false);
		findsumB.setEnabled(false);
		//deleteB.setEnabled(false);
		changeKeyB.setEnabled(false);
	}

	@Override
	public void disableNext() {
		super.disableNext();
		insertB.setEnabled(true);
		findsumB.setEnabled(true);
		//deleteB.setEnabled(true);
		changeKeyB.setEnabled(true);
		next.setEnabled(false);
	}

}
