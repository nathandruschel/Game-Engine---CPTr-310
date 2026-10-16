package game1;

import common.PhysUtils;
import components.*;
import events.CollisionEvent;
import events.OutOfBoundsEvent;

import java.awt.*;
import java.awt.event.MouseEvent;

public class GameLogic extends GameController {
    private Entity taco;

    @Override
    public void startGame() {
        Dimension screen = getGameView().getDimension();
        taco = new Entity();
        taco.setXloc(screen.width/2.0f);
        taco.setYloc(screen.height/2.0f);
        taco.setName("taco");
        SpriteView tacoView = new SpriteView(taco, "taco-1");
        Renderable renderable = new Renderable(taco, tacoView);
        tacoView.setScaling(2.0f);
        tacoView.setShowHitbox(true);
        addRenderable(renderable);
        super.startGame();
    }

    @Override
    public void onLoopDone() {

    }

    @Override
    public void onCollision(CollisionEvent e) {
        Entity a = e.getA();
        Entity b = e.getB();
        if (a.getName().equals("taco")){
           SpriteView v = (SpriteView) getView(a);
           v.modifyView("explosion-5");
           a.disposeAfter(10);
        } else if (b.getName().equals("taco")){
            SpriteView v = (SpriteView) getView(b);
            v.modifyView("explosion-5");
            b.disposeAfter(10);
        } else {
            PhysUtils.resolveCollision(a,b);
        }
    }

    @Override
    public void onOutOfBounds(OutOfBoundsEvent e) {
        CanvasEdge edge = e.getEdge();
        Entity entity = e.getEntity();
        GameView gv = getGameView();
        if (edge.equals(gv.getBottomEdge())){
            entity.dispose();
        } else {
            PhysUtils.resolveOutOfBounds(entity, edge);
            entity.move(0, 10);
        }
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
        Entity alien = new Entity();
        EntityView view = new SpriteView(alien, "alien-3");
        alien.setXloc(e.getX());
        alien.setYloc(e.getY());
        alien.setXvel(20.0f);
        alien.setName("alien");
        addRenderable(new Renderable(alien, view));
    }
}
