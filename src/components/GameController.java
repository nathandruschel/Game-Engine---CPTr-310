package components;

import common.Constants;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

public class GameController implements Constants, KeyActivityListener, MouseActivityListener {

    private GameView gameView;
    private Timer gameClock;
    private final Map<Long, Renderable> renderableMap;
    private final ArrayList<Renderable> pendingAdditions;

    public GameController(){
        renderableMap = new ConcurrentHashMap<>();
        pendingAdditions = new ArrayList<>();
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public void onKeyActivity(int[] keyCodes) {

    }


    @Override
    public void onMousePressed(MouseEvent e) {

    }

    @Override
    public void onMouseReleased(MouseEvent e) {

    }
}
