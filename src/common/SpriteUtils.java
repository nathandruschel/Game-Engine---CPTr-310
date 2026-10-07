package common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpriteUtils {

    private static final HashMap<String, BufferedImage[]> depot = new HashMap<>();

    public static BufferedImage[] processSpriteSheet(String resource) {

        resource += ".png";

        if (depot.containsKey(resource)) {
            return depot.get(resource);
        }

        BufferedImage source = null;

        int count = 0;

        try {
            // The ClassLoader.getResource() ensures we get the sprite
            // from the appropriate place, this helps with deploying the game
            // with things like webstart. You could equally do a file look
            // up here.

            count = validateFileName(resource);

            if (count == 0)
                throw new Exception("Sprite cannot have zero entries!");

            URL url = SpriteUtils.class.getResource("/images/" + resource);

            if (url == null) {
                System.err.println("Can't find image resource file: \"" + resource + "\"");
                System.exit(0);
            }

            // use ImageIO to read the image in
            source = ImageIO.read(url);
        } catch (IOException e) {
            System.err.println("Failed to load: \"" + resource + "\"");
            System.exit(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return toArray(source, count);
    }

    private static BufferedImage[] toArray(BufferedImage image, int count) {

        ArrayList<BufferedImage> sequence = new ArrayList<>();

        int width = image.getWidth();
        int height = image.getHeight();

        if (count > 1) {
            width = width / count;
        } else {
            count = 1;
        }

        for (int i = 0; i < count; i++) {
            BufferedImage croppedImage = getIndexedImage(image, width, height, i);
            sequence.add(croppedImage);
        }

        if (sequence.isEmpty()) {
            System.err.println("Error processing sprite sheet - no images loaded!");
            System.exit(0);
        }
        return sequence.toArray(new BufferedImage[0]);
    }

    private static BufferedImage getIndexedImage(BufferedImage from, int width, int height, int index) {

        int sx = index * width;

        BufferedImage croppedImage = from.getSubimage(sx, 0, width, height);

        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gc = bimg.createGraphics();
        gc.drawImage(croppedImage, 0, 0, null);
        gc.dispose();
        return bimg;
    }

    private static int validateFileName(String file) throws Exception {
        String regex = "^[a-zA-Z0-9]+-([0-9]+).png$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(file);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new Exception("Bad name \"" + file + "\"! Expected regex \"^[a-z0-9]+-[0-9]+.png$\"");
        }
    }

}

