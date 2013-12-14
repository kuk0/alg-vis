package algvis.core;

import algvis.core.visual.ZDepth;
import algvis.ui.view.View;

public class RelativeNode extends Node {
  private Node relative;

  public int sepx=0, sepy = 30;
  public int sepTox=0, sepToy = 30;

  protected RelativeNode(Node relative, DataStructure D, int key) {
    super(D, key, relative.x, relative.y);
    changeRelative(relative);
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
    tox = relative.tox + sepx;
    toy = relative.toy + sepy;
    if(steps == 0 && ( tox != x || toy != y )) steps = 10;
    if(steps > 0) {
      sepx += (sepTox - sepx) / steps;
      sepy += (sepToy - sepy) / steps;
    }
    super.move();
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

  public void draw(View v) {
    super.draw(v);
  }

  private void updatePosition() {
    this.x = relative.x + sepx;
    this.y = relative.y + sepy;
  }

  public void changeRelative(Node v) {
    relative = v;
  }
}
