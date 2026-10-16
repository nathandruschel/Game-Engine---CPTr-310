package components;

import common.SpriteUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class SpriteView extends EntityView{
    protected Queue<BufferedImage> spriteSheet;
    protected int skipcount = 5; //clock ticks/frames
    protected int rollSprite = 0;

    public SpriteView(Entity model, String resource) {
        super(model);
        if (resource != null){
            image = setView(resource);
            model.setDimension(new Dimension(image.getWidth(), image.getHeight()));
        } else {
            LOGGER.warning("Resource file cannot be null");
            image = setView();
        }
        model.setDimension(new Dimension(image.getWidth(), image.getHeight()));
    }

    protected BufferedImage setView(String resource){
        spriteSheet = new LinkedList<>();
        BufferedImage[] images = SpriteUtils.processSpriteSheet(resource);
        Collections.addAll(spriteSheet, images);
        return spriteSheet.peek();
    }

    protected void rollSprite(){
        if (spriteSheet != null && spriteSheet.size() > 1){
            rollSprite++;
            rollSprite %= skipcount;
            if (rollSprite == 0){
                image = spriteSheet.remove();
                spriteSheet.add(image);
            }
        }
    }

    public void setSkipcount(int count){
        if (count > 0){
            this.skipcount = count;
        }
    }

    @Override
    public void setScaling(float scaling){
        this.scaling = scaling;
        for (int i = 0; i < spriteSheet.size(); i++) {
            image = spriteSheet.remove();
            image = SpriteUtils.scaleSprite(image, scaling);
            spriteSheet.add(image);
        }
        model.setDimension(new Dimension(image.getWidth(), image.getHeight()));
    }

    public void modifyView(String resource){
        image = setView(resource);
        model.setDimension(new Dimension(image.getWidth(), image.getHeight()));
    }

    @Override
    public void render(Graphics2D gc) {
        super.render(gc);
        rollSprite();
    }
}
