package components;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.util.Arrays;

public class GameCanvas extends Canvas {
    private CanvasEdge[] edges;

    public GameCanvas(){
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                initializeCanvas();
                super.componentResized(e);
            }
        });
    }

    private void initializeCanvas() {
        createBufferStrategy(2);
        edges = computeEdges();
        setFocusable(true);
        requestFocus();
    }

    private CanvasEdge[] computeEdges() {
        Dimension dimension = getSize();
        Point2D.Float p1 = new Point2D.Float(0,0);
        Point2D.Float p2 = new Point2D.Float(dimension.width,0);
        Point2D.Float p3 = new Point2D.Float(dimension.width, dimension.height);
        Point2D.Float p4 = new Point2D.Float(0, dimension.height);
        CanvasEdge top = new CanvasEdge(p1, p2);
        CanvasEdge right = new CanvasEdge(p2, p3);
        CanvasEdge bottom = new CanvasEdge(p3, p4);
        CanvasEdge left = new CanvasEdge(p4, p1);

        return new CanvasEdge[] {top, right, bottom, left};
    }

    public CanvasEdge[] getEdges(){
        return edges;
    }

    public CanvasEdge getTopEdge(){
        return edges[0];
    }

    public CanvasEdge getRightEdge(){
        return edges[1];
    }

    public CanvasEdge getBottomEdge(){
        return edges[2];
    }

    public CanvasEdge getLeftEdge(){
        return edges[3];
    }

    public void renderTask(EntityView[] views){
        BufferStrategy bufferStrategy = getBufferStrategy();
        do{
            do{
                //draw all views
                Graphics2D gc = (Graphics2D) bufferStrategy.getDrawGraphics();
                int w = getWidth();
                int h = getHeight();
                gc.setClip(0,0, w, h);
                //draw black background
                gc.setBackground(Color.BLACK);
                gc.clearRect(0, 0, w, h);
                Arrays.stream(views).forEach(v -> v.render(gc));
                gc.dispose();
            } while (bufferStrategy.contentsRestored());
            //swap the buffers
            bufferStrategy.show();
        } while (bufferStrategy.contentsLost());
        getToolkit().sync();
    }

}
