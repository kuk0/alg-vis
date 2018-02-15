package algvis.core;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import algvis.ui.view.View;

public class ArrayNode extends Node {

    public ArrayNode(DataStructure D, int key, int zDepth) {
        super(D, key, zDepth);
    }

    public ArrayNode(DataStructure D, int zDepth) {
        super(D, Node.NOKEY, zDepth);
    }

    @Override
    protected void drawBg(View v) {
        v.setColor(getBgColor());
        v.fillSqr(x, y, Node.RADIUS);
        v.setColor(Color.BLACK);
        v.drawSqr(x, y, Node.RADIUS);
        if (marked) {
            v.drawSqr(x, y, Node.RADIUS + 2);
        }
    }

    @Override
    public Color getBgColor() {
        if (key != NOKEY) {
            return super.getBgColor();
        }
        return Color.lightGray;
    }

    @Override
    protected boolean inside(int x, int y) {
        return this.x - Node.RADIUS <= x && this.y - Node.RADIUS <= y
            && this.x + Node.RADIUS >= x && this.y + Node.RADIUS >= x;
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D.Double(x - Node.RADIUS, y - Node.RADIUS,
            2 * Node.RADIUS, 2 * Node.RADIUS);
    }

}
