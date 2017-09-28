package algvis.ds.dynamicarray;

import algvis.core.DataStructure;
import algvis.core.Settings;
import algvis.ui.NewVisPanel;

public class DynamicArrayPanel extends NewVisPanel{
  public static Class<? extends DataStructure> DS = DynamicArray.class;

  public DynamicArrayPanel(Settings S) {
    super(S);
  }
  @Override
  protected void initDS() {
    D = new DynamicArray(this);
    scene.add(D);
    buttons = new DynamicArrayButtons(this);
  }
}
