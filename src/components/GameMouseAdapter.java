package components;

import common.Constants;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMouseAdapter extends MouseAdapter implements Constants {

    private MouseActivityListener listener;

    public GameMouseAdapter(MouseActivityListener listener){
        this.listener = listener;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        listener.onMousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        listener.onMouseReleased(e);
    }
}
