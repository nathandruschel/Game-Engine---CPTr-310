package events;

import components.CanvasEdge;
import components.Entity;

import java.util.Objects;

public class OutOfBoundsEvent {
    private final CanvasEdge edge;
    private final Entity entity;

    public OutOfBoundsEvent(Entity entity, CanvasEdge edge){
        this.entity = entity;
        this.edge = edge;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OutOfBoundsEvent that = (OutOfBoundsEvent) o;
        return Objects.equals(edge, that.edge) && Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edge, entity);
    }
}
