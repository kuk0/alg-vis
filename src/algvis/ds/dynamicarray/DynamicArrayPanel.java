package algvis.ds.dynamicarray;

import algvis.ui.VisPanel;

public class DynamicArrayPanel extends VisPanel{
  private static final long serialVersionUID = -4098377740442930253L;

  @Override
  protected void initDS() {
    D = new DynamicArray(this);
    scene.add(D);
    buttons = new DynamicArrayButtons(this);
  }
}
