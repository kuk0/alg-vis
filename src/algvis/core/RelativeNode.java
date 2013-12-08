package algvis.core;

import algvis.core.visual.ZDepth;
import algvis.ui.view.View;

public class RelativeNode extends Node {
  Node relative;

  public int sepx=0, sepy = 30;
  public int sepTox=0, sepToy = 30;

  protected RelativeNode(Node relative, DataStructure D, int key, int x, int y) {
    super(D, key, x, y);
    this.relative = relative;
    moveToRelative();
  }

  protected RelativeNode(Node relative, DataStructure D, int key, int x, int y, int zDepth) {
    super(D, key, x, y, zDepth);
    this.relative = relative;
    moveToRelative();
  }

  protected RelativeNode(Node relative, DataStructure D, int key, int zDepth) {
    super(D, key, zDepth);
    this.relative = relative;
    moveToRelative();
  }

  public RelativeNode(Node relative, Node v) {
    super(v);
    this.relative = relative;
    this.x = relative.x;
    this.y = relative.y;
  }

  @Override
  public void move() {
    tox = relative.x + sepx;
    toy = relative.y + sepy;
    System.out.println("Suniem sa");
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
  }

  public void draw(View v) {
    super.draw(v);
  }

  private void updatePosition() {
    this.x = relative.x + sepx;
    this.y = relative.y + sepy;
  }
}
