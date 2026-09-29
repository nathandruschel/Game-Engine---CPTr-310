package components;

import common.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class EntityView implements Constants {
    private final long id;
    protected BufferedImage image;
    private float xloc, yloc;
    private final Entity model;
    private boolean showHitbox;
    private final int RADIOUS = 5; //5 pixels
    private Color[] colors = {
            Color.WHITE,
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.ORANGE,
            Color.YELLOW,
            Color.CYAN,
            new Color(211, 175, 55, 1)
    };

    public EntityView(Entity model){
        this.id = model.getID();
        this.model = model;
        image = setView();
        update();
    }

    //Getters and Setters
    public long getId() {
        return id;
    }

    public boolean isShowHitbox() {
        return showHitbox;
    }

    public void setShowHitbox(boolean showHitbox) {
        this.showHitbox = showHitbox;
    }

    public float getYloc() {
        return yloc;
    }

    public void setYloc(float yloc) {
        this.yloc = yloc;
    }

    public float getXloc() {
        return xloc;
    }

    public void setXloc(float xloc) {
        this.xloc = xloc;
    }

    //Methods - everything else besides getters and setters
    public void update(){
        xloc = model.getXloc() - image.getWidth()/2.0f;
        yloc = model.getYloc() - image.getHeight()/2.0f;
    }

    private BufferedImage setView(){
        int index = rand.nextInt(0, 1000) % colors.length;
        BufferedImage image = new BufferedImage(2*RADIOUS, 2*RADIOUS, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gc = image.createGraphics();
        gc.setColor(colors[index]);
        gc.fillOval(0, 0, 2*RADIOUS, 2* RADIOUS);
        gc.dispose();
        return image;
    }

    public void render(Graphics2D gc){
        gc.drawImage(image, (int) xloc, (int) yloc, null);
        if (showHitbox){
            Color oldColor = gc.getColor();
            gc.setColor(new Color(0xFF00FF));
            Rectangle2D.Float hitbox = model.getHitBox();
            gc.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
            gc.setColor(oldColor);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EntityView that = (EntityView) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
