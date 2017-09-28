package algvis.ds.dynamicarray;

import algvis.internationalization.IButton;
import algvis.ui.Buttons;
import algvis.ui.VisPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class DynamicArrayButtons extends Buttons {
  private static final long serialVersionUID = -4820812162669072989L;
  IButton insertB;
  IButton popB;
  public DynamicArrayButtons(VisPanel M) {
    super(M);
  }
  @Override
  protected void actionButtons(JPanel P) {
    insertB = new IButton("button-insert");
    insertB.setMnemonic(KeyEvent.VK_I);
    insertB.addActionListener(this);

    popB = new IButton("button-pop");
    popB.setMnemonic(KeyEvent.VK_P);
    popB.addActionListener(this);

    P.add(insertB);
    P.add(popB);
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    super.actionPerformed(evt);
    if( evt.getSource() == insertB) {
      final Vector<Integer> args = I.getNonEmptyVI();
      for (final int x : args) {
        D.insert(x);
      }
    }
    else if(evt.getSource() == popB) {
      ((DynamicArray)D).pop();
    }
  }

  @Override
  public void setOtherEnabled(boolean enabled) {
    super.setOtherEnabled(enabled);
    insertB.setEnabled(enabled);
    popB.setEnabled(enabled);
  }

}
