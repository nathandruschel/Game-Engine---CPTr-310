package common;

import java.util.Random;
import java.util.logging.Logger;

public interface Constants {
    int WIDTH = 800;
    int HEIGHT = 600;
    long PERIOD = 17L; // in milliseconds
    long START_DELAY = 100L;
    int HITBOX = 20; //Hitbox edge in pixels
    Logger LOGGER = Logger.getLogger(Constants.class.getName()); //Logs info like printing to the terminal - do not want to print to any terminal
    Random rand = new Random();
}
