package common;

import components.CanvasEdge;
import components.Entity;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class PhysUtils implements Constants {

    public static void resolveCollision(Entity a, Entity b) {

        Rectangle2D.Float ra = a.getHitBox();
        Rectangle2D.Float rb = b.getHitBox();

        if (!ra.intersects(rb))
            return;

        Rectangle2D.Float overlap = (Rectangle2D.Float) ra.createIntersection(rb);

        // Centers
        double acx = ra.getCenterX();
        double acy = ra.getCenterY();
        double bcx = rb.getCenterX();
        double bcy = rb.getCenterY();

        // Collision normal: from A toward B
        double nx, ny;

        if (overlap.width < overlap.height) {
            nx = Math.signum(bcx - acx);
            ny = 0;
        } else {
            nx = 0;
            ny = Math.signum(bcy - acy);
        }

        separateEntities(a, b, nx, ny, overlap);

        // ----- Collision response -----
        collisionResponse(a, b, nx, ny);
    }

    private static void separateEntities(Entity a, Entity b, double nx, double ny, Rectangle2D.Float overlap) {
        // Penetration depth
        double penetration =
                (nx != 0) ? overlap.width : overlap.height;

        // ----- Separate the objects -----
        if (a.isMovable() && b.isMovable()) {
            a.move((float) (-nx * penetration / 2.0),
                    (float) (-ny * penetration / 2.0));

            b.move((float) (nx * penetration / 2.0),
                    (float) (ny * penetration / 2.0));

        } else if (a.isMovable()) {
            a.move((float) (-nx * penetration),
                    (float) (-ny * penetration));

        } else if (b.isMovable()) {
            b.move((float) (nx * penetration),
                    (float) (ny * penetration));
        }
    }

    private static void collisionResponse(Entity a, Entity b,
                                          double nx, double ny) {
        double rvx = b.getXvel() - a.getXvel();
        double rvy = b.getYvel() - a.getYvel();

        // vn is the scalar component of the relative velocity
        // along the collision normal.
        double vn = rvx * nx + rvy * ny;

        if (vn >= 0)
            return;

        if (a.isMovable() && b.isMovable()) {
            double impulse = -vn;

            a.setXvel((float) (a.getXvel() - impulse * nx));
            a.setYvel((float) (a.getYvel() - impulse * ny));

            b.setXvel((float) (b.getXvel() + impulse * nx));
            b.setYvel((float) (b.getYvel() + impulse * ny));
        } else if (a.isMovable()) {
            // B is immovable
            a.setXvel((float) (a.getXvel() - vn * nx));
            a.setYvel((float) (a.getYvel() - vn * ny));

        } else if (b.isMovable()) {

            // A is immovable
            b.setXvel((float) (b.getXvel() + vn * nx));
            b.setYvel((float) (b.getYvel() + vn * ny));
        }
    }

    public static void resolveOutOfBounds(Entity entity, CanvasEdge edge) {
        Point2D.Float result =
                PhysUtils.reflectVelocity(entity.getXvel(),
                        entity.getYvel(), edge.getLine());
        entity.setXvel(result.x);
        entity.setYvel(result.y);

        // Separate the entity from the edge
        //(nx, ny) is one of the two possible unit normals.
        Point2D.Float norms = edge.getNormals();
        float penetration = edge.getPenetration(entity.getHitBox());
        entity.move(norms.x * penetration,
                norms.y * penetration);
    }

    private static Point2D.Float reflectVelocity(
            float vx, float vy, Line2D.Float line) {

        double dx = line.x2 - line.x1;
        double dy = line.y2 - line.y1;

        // Perpendicular to line
        double nx = -dy;
        double ny = dx;

        // Normalize - get unit vectors
        double length = Math.sqrt(nx * nx + ny * ny);
        nx /= length;
        ny /= length;

        // Reflect velocity
        double dot = vx * nx + vy * ny;

        return new Point2D.Float(
                (float) (vx - 2 * dot * nx),
                (float) (vy - 2 * dot * ny)
        );
    }
}
