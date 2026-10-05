package components;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class GameKeyboardAdapter extends KeyAdapter {

    private KeyActivityListener listener;
    private final Set<Integer> activeKeys = Collections.synchronizedSet(new TreeSet<>());
    private boolean enabled = true;

    public GameKeyboardAdapter(KeyActivityListener listener){
        this.listener = listener;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
    }

    public int[] getActiveKeys(){
        if(activeKeys.isEmpty())
            return new int[] {KeyEvent.VK_UNDEFINED};
        return activeKeys.stream().mapToInt(e -> (int)e).toArray();
    }

    public boolean isEnable() {
        return enabled;
    }

    public void setEnable(boolean state) {
        this.enabled = state;
    }
    //called by game loop every cycle
    public void notifyKeyAction() {
        int[] keys = getActiveKeys();
        listener.onKeyActivity(keys);
    }
}
