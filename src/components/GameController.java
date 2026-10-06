package components;

import common.Constants;
import events.CollisionEvent;
import events.OutOfBoundsEvent;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GameController implements Constants, KeyActivityListener, MouseActivityListener {

    private GameView gameView;
    private Timer gameClock;
    private final Map<Long, Renderable> renderableMap;
    private final ArrayList<Renderable> pendingAdditions;
    private Set<CollisionPair> activeCollisions;
    private Set<OutOfBoundsEvent> activeOutOfBounds;
    private Long timeStamp;

    public GameController(){
        renderableMap = new ConcurrentHashMap<>();
        pendingAdditions = new ArrayList<>();
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public void viewAdded() {
        gameClock = new Timer();
        timeStamp = System.currentTimeMillis();
    }

    public void viewRemoved() {
        gameClock.cancel();
    }

    private Entity[] getEntities(){
        return renderableMap.values().stream().map(Renderable::getModel).toArray(Entity[]::new);
    }

    private EntityView[] getViews(){
        return renderableMap.values().stream().map(Renderable::getView).toArray(EntityView[]::new);
    }

    public void startGame(){
        TimerTask gameLoop = new GameLoop();
        gameClock.schedule(gameLoop, START_DELAY, PERIOD);
    }

    private class GameLoop extends TimerTask{

        @Override
        public void run() {
            updateEntities();
            checkOutOfBounds();
            checkCollisions();
            render();
            checkUserInteractions();
            processDeletions();
            processAdditions();
            onLoopDone();
        }
    }

    private void updateEntities() {
        long oldTime = timeStamp;
        timeStamp = System.currentTimeMillis();
        float elapsedTime = timeStamp - oldTime;

        renderableMap.values().forEach(r -> processUpdateRenderable(r, elapsedTime));
    }

    private void processUpdateRenderable(Renderable r, float elapsedTime) {
        Entity e = r.getModel();
        e.update(elapsedTime);
        r.getView().update();
        if (e.isDisposing()){
            e.tickDispose();
        }
    }

    private void checkOutOfBounds() {
        Set<OutOfBoundsEvent> currentOutOfBounds = new HashSet<>();
        Entity[] entities = getEntities();

        for (Entity e : entities){
            Rectangle2D.Float hitBox = e.getHitBox();
            CanvasEdge edge = gameView.findEdge(hitBox);
            if (edge != null){
                OutOfBoundsEvent event = new OutOfBoundsEvent(e, edge);
                currentOutOfBounds.add(event);
                if (!activeCollisions.contains(event)){
                    onOutOfBounds(event);
                }
            }
        }
        activeOutOfBounds = currentOutOfBounds;
    }

    private void checkCollisions() {
        Set<CollisionPair> currentCollisions = new HashSet<>();
        Entity[] entities = getEntities();

        for (int i = 0; i < entities.length; i++) {
            for (int j = i+1; j < entities.length; j++) {
                Entity a = entities[i];
                Entity b = entities[j];

                if (a.getHitBox().intersects(b.getHitBox())){
                    CollisionPair pair = new CollisionPair(a, b);
                    currentCollisions.add(pair);

                    if (!activeCollisions.contains(pair)){
                        onCollision(new CollisionEvent(pair));
                    }
                }
            }
        }
        activeCollisions = currentCollisions;
    }

    private void render(){
        gameView.render(getViews());
    }

    private void checkUserInteractions() {
        gameView.pollKeyboard();
    }

    private void processDeletions() {
        renderableMap.values().stream().filter(Renderable::shouldRemove).forEach(this::disposeRenderable);
    }

    private void disposeRenderable(Renderable renderable) {
        long key = renderable.getID();
        LOGGER.info("Dispose entity " + renderableMap.remove(key).getModel());
    }

    private void processAdditions() {
        pendingAdditions.forEach(r -> {
            renderableMap.put(r.getID(), r);
        });

        if (!pendingAdditions.isEmpty()){
            LOGGER.info("Added entities " + Arrays.toString(getEntities()));
        }

        pendingAdditions.clear();
    }

    public abstract void onLoopDone();

    public abstract void onCollision(CollisionEvent e);

    public abstract void onOutOfBounds(OutOfBoundsEvent e);

    public abstract void onKeyActivity(int[] keyCodes);

    public abstract void onMousePressed(MouseEvent e);

    public abstract void onMouseReleased(MouseEvent e);
}
