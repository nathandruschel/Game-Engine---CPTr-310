package game2;

import common.PhysUtils;
import components.*;
import events.CollisionEvent;
import events.OutOfBoundsEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class GameLogic extends GameController {
    private Entity alien = new Entity();
    private Entity ship = new Entity();
    private boolean leftEdge = false;
    private boolean rightEdge = false;
    private int frameCount = 0;

    @Override
    public void startGame() {
        Dimension screen = getGameView().getDimension();
        EntityView alienView = new SpriteView(alien, "alien-3");
        alien.setXloc(20);
        alien.setYloc(10);
        alien.setXvel(20.0f);
        alien.setName("alien");
        addRenderable(new Renderable(alien, alienView));
        EntityView shipView = new SpriteView(ship, "ship-3");
        ship.setXloc(screen.width/2.0f);
        ship.setYloc(screen.height - 20);
        ship.setName("ship");
        addRenderable(new Renderable(ship, shipView));
        super.startGame();
    }

    @Override
    public void onLoopDone() {
        frameCount++;
    }

    @Override
    public void onCollision(CollisionEvent e) {
        Entity a = e.getA();
        Entity b = e.getB();
        if (a.getName().equals("ship")){
            SpriteView v = (SpriteView) getView(a);
            v.modifyView("explosion-5");
            a.disposeAfter(10);
        } else if (b.getName().equals("ship")){
            SpriteView v = (SpriteView) getView(b);
            v.modifyView("explosion-5");
            b.disposeAfter(10);
        } else if(a.getName().equals("missile") || b.getName().equals("missile")) {
            if (a.getName().equals(b.getName())){
                return;
            } else {
                if (a.getName().equals("alien")){
                    SpriteView v = (SpriteView) getView(a);
                    v.modifyView("explosion-5");
                    a.disposeAfter(10);
                } else {
                    SpriteView v = (SpriteView) getView(b);
                    v.modifyView("explosion-5");
                    b.disposeAfter(10);
                }
            }
        } else {
            PhysUtils.resolveCollision(a,b);
        }
    }

    @Override
    public void onOutOfBounds(OutOfBoundsEvent e) {
        CanvasEdge edge = e.getEdge();
        Entity entity = e.getEntity();
        GameView gv = getGameView();
        if (entity.getName().equals("ship")){
            if (edge.equals(getGameView().getLeftEdge())){
                leftEdge = true;
            }
            if (edge.equals(getGameView().getRightEdge())){
                rightEdge = true;
            }
        } else if(entity.getName().equals("missile")){
            entity.disposeAfter(3);
            return;
        }
        if (edge.equals(gv.getBottomEdge()) || edge.equals(gv.getTopEdge()) && !entity.getName().equals("alien")){
            entity.dispose();
        } else {
            if (entity.getName().equals("ship")){
                return;
            }
            PhysUtils.resolveOutOfBounds(entity, edge);
            entity.move(0, 10);
        }
    }

    @Override
    public void onKeyActivity(int[] keyCodes) {
        System.out.println(Arrays.toString(keyCodes));
        for (int keyCode: keyCodes){
            switch (keyCode){
                case KeyEvent.VK_A, KeyEvent.VK_LEFT:
                    if (!leftEdge){
                        ship.move(-5, 0);
                        rightEdge = false;
                    }
                    break;
                case KeyEvent.VK_D, KeyEvent.VK_RIGHT:
                    if (!rightEdge){
                        ship.move(5, 0);
                        leftEdge = false;
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    createMissile(ship.getXloc(), ship.getYloc());
                    break;
            }
        }
    }

    @Override
    public void onKeyTyped(int extendedKeyCode) {

    }


    @Override
    public void onMousePressed(MouseEvent e) {

    }

    @Override
    public void onMouseReleased(MouseEvent e) {

    }

    private void createMissile(float x, float y){
        int frameDelay = 10;
        if (frameCount < frameDelay){
            return;
        }
        frameCount = 0;
        Entity missile = new Entity();
        missile.setName("missile");
        SpriteView missileView = new SpriteView(missile, "missile-3");
        missile.setYvel(-40.0f);
        missile.setXloc(x);
        missile.setYloc(y-50);
        addRenderable(new Renderable(missile, missileView));
    }

}
