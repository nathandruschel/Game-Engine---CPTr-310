package components;

import java.util.Objects;

public class CollisionPair {
    private final Entity a, b;

    public CollisionPair(Entity a, Entity b){
        this.a = a;
        this.b = b;
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
        CollisionPair that = (CollisionPair) o;
        boolean ab = Objects.equals(a, that.a) && Objects.equals(b, that.b);
        boolean ba = Objects.equals(a, that.b) && Objects.equals(b, that.a);
        return ab || ba;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b);
    }
}
