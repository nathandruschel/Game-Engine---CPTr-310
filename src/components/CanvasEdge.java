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
        double dx = line.x2 - line.x1;
        double dy = line.y2 - line.y1;
        double length = Math.hypot(dx, dy);
        float nx = (float) (-dy / length);//gets normals
        float ny = (float) (dx / length);
        return  new Point2D.Float(nx, ny);
    }

    public float getPenetration(Rectangle2D.Float hitBox) {
        //find corners of box
        Point2D.Float[] corners = {
          new Point2D.Float(hitBox.x, hitBox.y),
          new Point2D.Float(hitBox.x + hitBox.width, hitBox.y),
          new Point2D.Float(hitBox.x + hitBox.width, hitBox.y + hitBox.height),
          new Point2D.Float(hitBox.x, hitBox.y + hitBox.height)
        };
        float minDist = Float.POSITIVE_INFINITY;
        for (Point2D.Float c : corners){
            float dist = (float) line.ptLineDist(c);
            minDist = Math.min(minDist, dist);
        }
        return  -minDist;
    }

    public boolean equalLines(Line2D.Float a, Line2D.Float b){
        if (a == null || b == null){
            return false;
        }
        boolean ab = a.x1 == b.x1 && a.y1 == b.y1 && a.x2 == b.x2 && a.y2 == b.y2;
        boolean ba = a.x1 == b.x2 && a.y1 == b.y2 && a.x2 == b.x1 && a.y2 == b.y1;
        return ab || ba;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CanvasEdge that = (CanvasEdge) o;
        return equalLines(line, that.line);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(line);
    }
}
