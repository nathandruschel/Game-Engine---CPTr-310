package events;

import components.CollisionPair;
import components.Entity;

import java.util.Objects;

public class CollisionEvent {
    private final Entity a, b;

    public CollisionEvent(CollisionPair pair){
        a = pair.getA();
        b = pair.getB();
    }

    public Entity getA() {
        return a;
    }

    public Entity getB() {
        return b;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CollisionEvent that = (CollisionEvent) o;
        boolean ab = Objects.equals(a, that.a) && Objects.equals(b, that.b);
        boolean ba = Objects.equals(a, that.b) && Objects.equals(b, that.a);
        return ab || ba;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
