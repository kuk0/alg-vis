package algvis.core;

import algvis.core.visual.ZDepth;
import algvis.ui.view.View;

public class RelativeNode extends Node {
  private Node relative;

  public int sepx=0, sepy = 30;
  public int sepTox=0, sepToy = 30;

  protected RelativeNode(Node relative, DataStructure D, int key, int sepx, int sepy) {
    super(D, key, relative.x + sepx, relative.y + sepy);
    changeRelative(relative);
    this.sepx = this.sepTox = sepx;
    this.sepy = this.sepToy = sepy;
  }

  protected RelativeNode(Node relative, DataStructure D, int key, int zDepth) {
    super(D, key, relative.x, relative.y, zDepth);
    changeRelative(relative);
  }

  public RelativeNode(Node relative, Node v) {
    super(v);
    changeRelative(relative);
    moveToRelative();
  }

  @Override
  public void move() {
    x = relative.x + sepx;
    y = relative.y + sepy;
    if(steps > 0) {
      sepx += (sepTox - sepx) / steps;
      sepy += (sepToy - sepy) / steps;
    }
  }

  public void moveToRelative() {
    this.x = relative.x + sepx;
    this.y = relative.y + sepy;
  }

  @Override
  public void goTo(int tox, int toy) {
    this.sepTox = tox;
    this.sepToy = toy;
    this.steps = 10;
  }

  private void updatePosition() {
    this.x = relative.x + sepx;
    this.y = relative.y + sepy;
  }

  public void changeRelative(Node v) {
    relative = v;
  }
}
