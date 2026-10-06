package game0;

import common.PhysUtils;
import components.*;
import events.CollisionEvent;
import events.OutOfBoundsEvent;

import java.awt.event.MouseEvent;
import java.util.Random;

public class GameLogic extends GameController {

    private final Random random;

    public GameLogic(){
        random = new Random();
    }

    private void createDot(int x, int y, float vx, float vy){
        Entity dot = new Entity();
        dot.setXloc(x);
        dot.setYloc(y);

        dot.setXvel(vx);
        dot.setYvel(vy);
        EntityView dotView = new EntityView(dot);

        Renderable r = new Renderable(dot, dotView);
        addRenderable(r);
    }

    @Override
    public void onLoopDone() {

    }

    @Override
    public void onCollision(CollisionEvent e) {

    }

    @Override
    public void onOutOfBounds(OutOfBoundsEvent e) {
        CanvasEdge edge = e.getEdge();
        Entity entity = e.getEntity();
        PhysUtils.resolveOutOfBounds(entity, edge);
    }

    @Override
    public void onKeyActivity(int[] keyCodes) {

    }

    @Override
    public void onMousePressed(MouseEvent e) {

    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        float vx = random.nextFloat(4, 20);
        float vy = random.nextFloat(4, 20);
        createDot(x, y, vx, vy);
    }
}
