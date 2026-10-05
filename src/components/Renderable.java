package components;

import java.util.Objects;

public class Renderable {
    private final Entity model;
    private final EntityView view;

    public Renderable(Entity m, EntityView v){
        this.model = m;
        this.view = v;
    }

    public EntityView getView() {
        return view;
    }

    public Entity getModel() {
        return model;
    }

    public boolean shouldRemove() {
        return model.shouldRemove();
    }

    public long getID(){
        return model.getID();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Renderable that = (Renderable) o;
        return Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model);
    }
}
