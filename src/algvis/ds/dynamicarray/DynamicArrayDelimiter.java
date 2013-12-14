package algvis.ds.dynamicarray;

import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.RelativeNode;
import algvis.ui.view.View;

import java.awt.*;

public class DynamicArrayDelimiter extends RelativeNode {
  Color C;
  public DynamicArrayDelimiter(Node relative, DataStructure D, Color C) {
    super(relative, D, Node.NOKEY, -Node.RADIUS, 0);
    this.C = C;
    System.out.println(relative.x + " "  + relative.y);
  }

  @Override
  public void draw(View v) {
    v.drawLine(x-1.5, y - Node.RADIUS*2, x-1.5, y + Node.RADIUS*2, 3, C);
  }
}
