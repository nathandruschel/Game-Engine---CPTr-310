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
        dotView.setShowHitbox(true);
        Renderable r = new Renderable(dot, dotView);
        addRenderable(r);
    }

    @Override
    public void onLoopDone() {

    }

    @Override
    public void onCollision(CollisionEvent e) {
        Entity a = e.getA();
        Entity b = e.getB();
        PhysUtils.resolveCollision(a, b);
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

        float vx = random.nextFloat(-39, 40);
        float vy = random.nextFloat(-39, 40);
        createDot(x, y, vx, vy);
    }

    //Use this code in collision or out of bounds to make your machine explode
//    int x = random.nextInt(800);
//    int y = random.nextInt(600);
//    float vx = random.nextFloat(-39, 40);
//    float vy = random.nextFloat(-39, 40);
//    createDot(x, y, vx, vy);
}
