package components;

import common.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class GameView extends JPanel implements Constants {

    private GameCanvas canvas;
    private final Map<Long, EntityView> entityViews;
    private final GameController controller;
    private GameKeyboardAdapter keyboardAdapter;


    public GameView(GameController controller){
        entityViews = new ConcurrentHashMap<>();
        this.controller = controller;
        controller.setGameView(this);
        addComponents();
    }

    private void addComponents() {
        setLayout(new BorderLayout());
        canvas = new GameCanvas();
        canvas.addKeyListener(new GameKeyboardAdapter(controller));
        canvas.addMouseListener(new GameMouseAdapter(controller));
        //Add adapters to the canvas TODO
        add(canvas, BorderLayout.CENTER);
        canvas.setBackground(Color.BLACK);
    }

    public void render(){
        final EntityView[] views = entityViews.values().toArray(new EntityView[0]);
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                canvas.renderTask(views);
                return null;
            }
        };
        worker.execute();
    }

    public CanvasEdge findEdge(Rectangle2D.Float hitBox){
        CanvasEdge[] edges = canvas.getEdges();
        for (CanvasEdge edge : edges){
            Line2D.Float line = edge.getLine();
            if (line.intersects(hitBox))
                return edge;
        }
        return null;
    }

    public CanvasEdge getTopEdge() {
        return canvas.getTopEdge();
    }

    public CanvasEdge getRightEdge() {
        return canvas.getRightEdge();
    }

    public CanvasEdge getBottomEdge() {
        return canvas.getBottomEdge();
    }

    public CanvasEdge getLeftEdge() {
        return canvas.getLeftEdge();
    }

    public void pullKeyboard(){
        keyboardAdapter.notifyKeyAction();
    }

    public Dimension getDimension(){
        return new Dimension(getWidth(), getHeight());
    }

    public void addView(EntityView view){
        entityViews.put(view.getId(), view);
    }

    public void removeView(Long id){
        entityViews.remove(id);
    }
}
