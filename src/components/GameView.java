package components;

import common.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class GameView extends JPanel implements Constants {

    private GameCanvas canvas;
    private final GameController controller;
    private GameKeyboardAdapter keyboardAdapter;


    public GameView(GameController controller){
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

    @Override
    public void addNotify() {
        controller.viewAdded();
        super.addNotify();
    }

    @Override
    public void removeNotify() {
        controller.viewRemoved();
        super.removeNotify();
    }

    public void render(final EntityView[] views){
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

}
