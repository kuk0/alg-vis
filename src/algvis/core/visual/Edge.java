package algvis.core.visual;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;

import algvis.core.Node;
import algvis.ui.view.View;

public class Edge extends VisualElement {
    int x1, y1, x2, y2; // TODO: generalize
    int mx, my;

    public Edge(int x1, int y1, int x2, int y2) {
        super(Scene.MIDZ - 1);
        set(x1, y1, x2, y2);
    }

    private void set(int x1, int y1, int x2, int y2) {
        double vecX = x2 - x1, vecY = y2 - y1, d = Math.sqrt(vecX * vecX + vecY
            * vecY);
        vecX *= Node.RADIUS * 1.5 / d;
        vecY *= Node.RADIUS * 1.5 / d;

        this.x1 = x1 + (int) vecX;
        this.y1 = y1 + (int) vecY;
        this.x2 = x2 - (int) vecX;
        this.y2 = y2 - (int) vecY;
        mx = this.x1;
    }

    public Edge(Node u, Node v) {
        this(u.x, u.y, v.x, v.y);
    }

    public Edge(int x1, int y1, int mx, int my, int x2, int y2) {
        super(Scene.MIDZ - 1);
        set(x1, y1, mx, my, x2, y2);
    }

    private void set(int x1, int y1, int mx, int my, int x2, int y2) {
        double vecX = mx - x1, vecY = my - y1, d = Math.sqrt(vecX * vecX + vecY
            * vecY);
        vecX *= Node.RADIUS * 1.5 / d;
        vecY *= Node.RADIUS * 1.5 / d;
        this.x1 = x1 + (int) vecX;
        this.y1 = y1 + (int) vecY;
        vecX = x2 - mx;
        vecY = y2 - my;
        d = Math.sqrt(vecX * vecX + vecY * vecY);
        vecX *= Node.RADIUS * 1.5 / d;
        vecY *= Node.RADIUS * 1.5 / d;
        this.x2 = x2 - (int) vecX;
        this.y2 = y2 - (int) vecY;
        this.mx = mx;
        this.my = my;
    }

    public Edge(Node u, Node m, Node v) {
        super(Scene.MIDZ - 1);
        if ((u.x < m.x) != (m.x < v.x)) {
            set(u.x, u.y, v.x, v.y);
        } else {
            double vecX = Math.abs(u.x - v.x), vecY = Math.abs(u.y - v.y), d = Math
                .sqrt(vecX * vecX + vecY * vecY), mx = m.x, my = m.y;
            mx += (u.x < v.x ? 1 : -1) * vecY * 2 * Node.RADIUS / d;
            my -= vecX * 2 * Node.RADIUS / d;
            set(u.x, u.y, (int) mx, (int) my, v.x, v.y);
        }
    }

    @Override
    protected void draw(View v) throws ConcurrentModificationException {
        if (mx == x1) {
            v.drawArrow(x1, y1, x2, y2, 2.0f, Color.RED);
        } else {
            v.drawCurveArrow(x1, y1, mx, my, mx, my, x2, y2, 2.0f, Color.RED);
        }
    }

    @Override
    protected void move() throws ConcurrentModificationException {
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return new Rectangle2D.Double(x1, y1, x2, y2);
    }
}