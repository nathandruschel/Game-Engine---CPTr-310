package components;

import common.Constants;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class CanvasEdge implements Constants {
    private final Line2D.Float line;

    public CanvasEdge(Point2D.Float p1, Point2D.Float p2) {
        line = new Line2D.Float(p1, p2);
    }

    public Line2D.Float getLine() {
        return  line;
    }

    public Point2D.Float getNormals() {
        //TODO
        return  null;
    }

    public float getPenetration(Rectangle2D.Float hitBox) {
        //TODO
        return  0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CanvasEdge that = (CanvasEdge) o;
        return Objects.equals(line, that.line);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(line);
    }
}
