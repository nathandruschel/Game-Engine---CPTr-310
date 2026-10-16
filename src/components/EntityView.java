package components;

import common.Constants;
import common.SpriteUtils;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class EntityView implements Constants {
    private final long id;
    protected BufferedImage image;
    protected final Entity model;
    protected boolean showHitbox;
    private int RADIOUS = 5; //5 pixels rand.nextInt(1,30)
    protected float scaling = 1.0f;
    private Color[] colors = {
            Color.WHITE,
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.ORANGE,
            Color.YELLOW,
            Color.CYAN
    };

    public EntityView(Entity model){
        this.id = model.getID();
        this.model = model;
        image = setView();
        model.setDimension(new Dimension(image.getWidth(), image.getHeight()));
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

    public float getScaling() {
        return scaling;
    }

    public void setScaling(float scaling) {
        this.scaling = scaling;
        image = SpriteUtils.scaleSprite(image, scaling);
        model.setDimension(new Dimension(image.getWidth(), image.getHeight()));
    }

    protected BufferedImage setView(){
        int index = rand.nextInt(0, 1000) % colors.length;
        BufferedImage image = new BufferedImage(2*RADIOUS, 2*RADIOUS, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gc = image.createGraphics();
        gc.setColor(colors[index]);
        gc.fillOval(0, 0, 2*RADIOUS, 2* RADIOUS);
        gc.dispose();
        return image;
    }

    public void render(Graphics2D gc){
        float xloc = model.getXloc() - image.getWidth()/2.0f;
        float yloc = model.getYloc() - image.getHeight()/2.0f;
        gc.drawImage(image, (int) xloc, (int) yloc, null);
        if (showHitbox){
            showHitBox(gc);
        }
    }

    public void renderWrapped(Graphics2D gc, int canvasWidth, int canvasHeight){
        double w = image.getWidth();
        double h = image.getHeight();
        float xloc = (float) (model.getXloc() - w/2.0f);
        float yloc = (float) (model.getYloc() - h/2.0f);
        //Always draw main image
        gc.drawImage(image, (int) xloc, (int) yloc, null);
        boolean left = xloc < 0;
        boolean right = xloc + w > canvasWidth;
        boolean top = yloc < 0;
        boolean bottom = yloc + h > canvasHeight;
        //horizontal copies
        if (left){
            gc.drawImage(image, (int) (xloc + canvasWidth), (int) yloc, null);
        }
        if (right){
            gc.drawImage(image, (int) (xloc - canvasWidth), (int) yloc, null);
        }
        //vertical copies
        if (top){
            gc.drawImage(image, (int) xloc, (int) (yloc + canvasHeight), null);
        }
        if (bottom){
            gc.drawImage(image, (int) xloc, (int) (yloc - canvasHeight), null);
        }
        //corner copies
        if (left && top){
            gc.drawImage(image, (int) (xloc + canvasWidth), (int) (yloc + canvasHeight), null);
        }
        if (left && bottom){
            gc.drawImage(image, (int) (xloc + canvasWidth), (int) (yloc - canvasHeight), null);
        }
        if (right && top){
            gc.drawImage(image, (int) (xloc - canvasWidth), (int) (yloc + canvasHeight), null);
        }
        if (right && bottom){
            gc.drawImage(image, (int) (xloc - canvasWidth), (int) (yloc - canvasHeight), null);
        }
        if (showHitbox){
            showHitBox(gc);
        }
    }

    protected void showHitBox(Graphics2D gc){
        Color oldColor = gc.getColor();
        gc.setColor(new Color(0xFF00FF));
        Rectangle2D.Float hitbox = model.getHitBox();
        gc.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        gc.setColor(oldColor);
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

    public Entity getModel() {
        return model;
    }
}
