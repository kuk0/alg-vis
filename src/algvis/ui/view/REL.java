package algvis.ui.view;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public enum REL {
    TOPLEFT(0, 0), TOP(0.5, 0), TOPRIGHT(1, 0), LEFT(0, 0.5), CENTER(0.5, 0.5), RIGHT(
        1, 0.5), BOTTOMLEFT(0, 1), BOTTOM(0.5, 1), BOTTOMRIGHT(1, 1);

    private final double left;
    private final double down;

    REL(double left, double down) {
        this.left = left;
        this.down = down;
    }

    public Point2D point(Point2D p, double d) {
        return new Point2D.Double(p.getX() + (2 * left - 1) * d, p.getY()
            + (2 * down - 1) * d);
    }

    public double boundaryX(Rectangle2D v) {
        return v.getMinX() * (1 - left) + v.getMaxX() * left;
    }

    public double boundaryY(Rectangle2D v) {
        return v.getMinY() * (1 - down) + v.getMaxY() * down;
    }

    public Point2D boundaryPoint(Rectangle2D v) {
        return new Point2D.Double(boundaryX(v), boundaryY(v));
    }

}
