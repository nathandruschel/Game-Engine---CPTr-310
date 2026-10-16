package components;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SceneryView extends SpriteView{

    public SceneryView(Entity entity, float canvasWidth, float canvasHeight, String resource) {
        super(entity, resource);
        fitSpriteToCanvas(canvasWidth, canvasHeight);
        setLayer(0);
    }

    private void fitSpriteToCanvas(float canvasWidth, float canvasHeight) {
        for (int i = 0; i < spriteSheet.size(); i++) {
            BufferedImage image = spriteSheet.remove();
            BufferedImage dst = new BufferedImage((int) canvasWidth, (int) canvasHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D gc = dst.createGraphics();
            gc.drawImage(image, 0, 0, (int) canvasWidth, (int) canvasHeight, 0, 0, image.getWidth(), image.getHeight(), null);
            gc.dispose();
            spriteSheet.add(dst);
        }
    }
}
