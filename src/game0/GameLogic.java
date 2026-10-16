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

    @Override
    public void startGame() {
        int w = getGameView().getWidth();
        int h = getGameView().getHeight();
        SceneryEntity scenery = new SceneryEntity(w, h);
        scenery.setXvel(-10.0f);
        SceneryView sceneryView = new SceneryView(scenery, w, h, "scenery-1");
        Renderable r = new Renderable(scenery, sceneryView);
        addRenderable(r);
        r = createDot(w/2, h/2, -10, 0);
        r.getModel().setWrapAround(true);
        addRenderable(r);
        super.startGame();
    }

    private Renderable createDot(int x, int y, float vx, float vy){
        Entity dot = new Entity();
        dot.setXloc(x);
        dot.setYloc(y);

        dot.setXvel(vx);
        dot.setYvel(vy);
        EntityView dotView = new EntityView(dot);
        dotView.setScaling(2.0f);
        //dotView.setShowHitbox(true);
        Renderable r = new Renderable(dot, dotView);
        addRenderable(r);
        return r;
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
        if (getView(entity).getModel().isWrapAround()){
            return;
        }
        PhysUtils.resolveOutOfBounds(entity, edge);
    }

    @Override
    public void onKeyActivity(int[] keyCodes) {

    }

    @Override
    public void onKeyTyped(int extendedKeyCode) {

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
