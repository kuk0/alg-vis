package algvis.ds.dynamicarray;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.RelativeNode;
import algvis.core.history.HashtableStoreSupport;
import algvis.ui.view.View;

import java.awt.*;
import java.util.Hashtable;

public class DynamicArrayDelimiter extends RelativeNode {
  Color C;
  public DynamicArrayDelimiter(DataStructure D, Node relative, Color C) {
    super(D, relative, Node.NOKEY, -Node.RADIUS, 0, 0);
    this.C = C;
  }

  @Override
  public void draw(View v) {
    v.drawLine(x-1.5, y - Node.RADIUS*2, x-1.5, y + Node.RADIUS*2, 3, C);
  }
}
