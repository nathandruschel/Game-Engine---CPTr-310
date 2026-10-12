package common;

import components.CanvasEdge;
import components.Entity;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class PhysUtils implements Constants{

    public static void resolveCollision(Entity a, Entity b) {

        Rectangle2D.Float ra = a.getHitBox();
        Rectangle2D.Float rb = b.getHitBox();
        float timeToHit = timeToHit(a, b);
        if (!ra.intersects(rb) || Float.compare(timeToHit, PERIOD) > 0)
            return;

        Point2D.Float normals = computeCollisionNormals(a, b);
        Rectangle2D.Float overlap = (Rectangle2D.Float) ra.createIntersection(rb);

        separateEntities(a, b, normals.getX(), normals.getY(), overlap);

        // ----- Collision response -----
        collisionResponse(a, b, normals.getX(), normals.getY());
    }

    private static Point2D.Float computeCollisionNormals(Entity a, Entity b) {

        Rectangle2D.Float ra = a.getHitBox();
        Rectangle2D.Float rb = b.getHitBox();

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
        return new Point2D.Float((float) nx, (float) ny);
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

    public static double relativeNormalVelocity(Entity a, Entity b) {

        Point2D.Float normals = computeCollisionNormals(a, b);
        double rvx = b.getXvel() - a.getXvel();
        double rvy = b.getYvel() - a.getYvel();

        return rvx * normals.getX() + rvy * normals.getY();
    }

    private static void collisionResponse(Entity a,
                                          Entity b,
                                          double nx, double ny) {
        double rvx = b.getXvel() - a.getXvel();
        double rvy = b.getYvel() - a.getYvel();

        // vn is the scalar component of the relative velocity
        // along the collision normal.
        double vn = rvx * nx + rvy * ny;

        if (vn >= 0)
            return;

        double impulse = -vn/2.0;

        if (a.isMovable() && b.isMovable()) {

            a.setXvel((float) (a.getXvel() + vn * nx));
            a.setYvel((float) (a.getYvel() + vn * ny));

            b.setXvel((float) (b.getXvel() - vn * nx));
            b.setYvel((float) (b.getYvel() - vn * ny));
        } else if (a.isMovable()) {
            // B is immovable
            a.setXvel((float) (a.getXvel() + impulse * nx));
            a.setYvel((float) (a.getYvel() + impulse * ny));

        } else if (b.isMovable()) {
            // A is immovable
            b.setXvel((float) (b.getXvel() - impulse * nx));
            b.setYvel((float) (b.getYvel() - impulse * ny));
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
        Rectangle2D.Float box = entity.getHitBox();
        Point2D.Float norms = edge.getNormals();

        float penetration = edge.getPenetration(entity.getHitBox());
        entity.move(norms.x * penetration, norms.y * penetration);
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

    public static float timeToHit(Entity e1, Entity e2)
    {
        if (e1 == e2) return Float.POSITIVE_INFINITY;
        double dx = e2.getXloc() - e1.getXloc();
        double dy = e2.getYloc() - e1.getYloc();
        double dvx = e2.getXvel() - e1.getXvel();
        double dvy = e2.getYvel() - e1.getYvel();
        double dvdr = dx*dvx + dy*dvy;
        if( dvdr > 0) return Float.POSITIVE_INFINITY;
        double dvdv = dvx*dvx + dvy*dvy;
        double drdr = dx*dx + dy*dy;
        Rectangle2D.Float hitbox1 = e1.getHitBox();
        Rectangle2D.Float hitbox2 = e2.getHitBox();
        double radius1 = Math.hypot(hitbox1.width, hitbox1.height);
        double radius2 = Math.hypot(hitbox2.width, hitbox2.height);
        double sigma = radius1 + radius2;
        double d = (dvdr*dvdr) - dvdv * (drdr - sigma*sigma);
        if (d < 0) return Float.POSITIVE_INFINITY;
        return (float) (-(dvdr + Math.sqrt(d)) / dvdv);
    }

}
