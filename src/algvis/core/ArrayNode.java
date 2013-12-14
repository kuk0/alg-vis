package algvis.core;

import algvis.core.history.HashtableStoreSupport;
import algvis.core.visual.VisualElement;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

public class ArrayNode extends Node {

  public ArrayNode(DataStructure D, int key,int zDepth) {
    super(D, key, zDepth);
  }

  public ArrayNode(DataStructure D, int zDepth) {
    super(D, Node.NOKEY, zDepth);
  }

  @Override
  public void draw(View v) throws ConcurrentModificationException {
    if (state == Node.INVISIBLE || getKey() == NULL) {
      return;
    }
    drawBg(v);
    drawKey(v);
  }

  protected void drawBg(View v) {
    v.setColor(getBgColor());
    v.fillSqr(x, y, Node.RADIUS);
    v.setColor(Color.BLACK); // fgcolor);
    v.drawSqr(x, y, Node.RADIUS);
    if (marked) {
      v.drawSqr(x, y, Node.RADIUS + 2);
    }
  }

  @Override
  public Color getBgColor() {
    if(key != NOKEY) return super.getBgColor();
    return Color.lightGray;
  }

  protected boolean inside(int x, int y) {
    return this.x <= x && this.y <= y && this.x + Node.RADIUS >= x && this.y + Node.RADIUS >= x;
  }

  @Override
  public Rectangle2D getBoundingBox() {
    return new Rectangle2D.Double(x, y, Node.RADIUS, Node.RADIUS);
  }

}
