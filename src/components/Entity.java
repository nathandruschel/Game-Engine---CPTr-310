package components;

import common.Constants;

import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Entity implements Constants {
    private static final AtomicLong systemID = new AtomicLong(0);//prevents race conditions - nothing can preempt this object
    private final long id;
    private float xloc, yloc; //coordinates
    private float xvel, yvel; //velocity
    private boolean movable = true;
    private int disposalFrames = -1;
    private String name = "Entity";


    public Entity() {
        this.id = systemID.incrementAndGet();
    }

    //Getters, Setters, Equals, and Hashcode - oh my!
    public Long getID() {
        return id;
    }

    public boolean isMovable() {
        return movable;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    public float getXvel() {
        return xvel;
    }

    public void setXvel(float v) {
        this.xvel = v;
    }

    public float getYvel() {
        return yvel;
    }

    public void setYvel(float v) {
        this.yvel = v;
    }

    public float getXloc() {
        return xloc;
    }

    public void setXloc(float xloc) {
        this.xloc = xloc;
    }

    public float getYloc() {
        return yloc;
    }

    public void setYloc(float yloc) {
        this.yloc = yloc;
    }

    public int getDisposalFrames() {
        return disposalFrames;
    }

    public void setDisposalFrames(int disposalFrames) {
        this.disposalFrames = disposalFrames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Rectangle2D.Float getHitBox() {
        return new Rectangle2D.Float(xloc - HITBOX /2.0f, yloc - HITBOX /2.0f, HITBOX, HITBOX); //moves the rectangle left and up so the Entity is in the center of the hitbox
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return id == entity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    //Methods - everything else besides getters and setters
    public void move(float dx, float dy) {
        if (movable) {
            xloc += dx;
            yloc += dy;
        }
    }

    public void update(float elapsedTime) {
        xloc = xloc + xvel * elapsedTime/100;
        yloc = yloc + yvel * elapsedTime/100;
    }

    public void dispose() {
        disposalFrames = 0;
        LOGGER.info("Dispose called on entity " + this);
    }

    public void disposeAfter(int frames) {
        disposalFrames = frames;
        LOGGER.info("Dispose after " + frames + " frames called on " + this);
    }

    public void tickDispose() {
        if (disposalFrames > 0)
            disposalFrames--;
    }

    public boolean isDisposing() {
        return disposalFrames >= 0;
    }

    public boolean shouldRemove() {
        return disposalFrames == 0;
    }

    @Override
    public String toString() {
        return "Entity{ id = " + id + "}";
    }
}