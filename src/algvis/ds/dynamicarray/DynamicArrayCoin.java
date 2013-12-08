package algvis.ds.dynamicarray;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.RelativeNode;
import algvis.ui.Fonts;
import algvis.ui.view.View;

public class DynamicArrayCoin extends RelativeNode {
  public DynamicArrayCoin(Node relative, DataStructure D, int key, int zDepth) {
    super(relative, D, key, zDepth);
  }

  public DynamicArrayCoin(Node relative, DataStructure D, int zDepth) {
    super(relative, D, 47, zDepth);
  }

  @Override
  public String toString() {
    return "$";
  }
}
