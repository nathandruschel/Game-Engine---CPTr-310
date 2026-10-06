package game0;

import common.Constants;
import components.GameView;

import javax.swing.*;
import java.awt.*;

public class GameApp extends JFrame {

    public GameApp(){
        setTitle("Game 0");
        moveToCenter();
        GameLogic controller = addComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        controller.startGame();
    }

    private GameLogic addComponents() {
        GameLogic controller = new GameLogic();
        GameView gameView = new GameView(controller);
        gameView.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        add(gameView, BorderLayout.CENTER);
        return controller;
    }

    private void moveToCenter(){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dimension.width - Constants.WIDTH)/2;
        int y = (dimension.height - Constants.HEIGHT)/2;
        setLocation(x, y);
    }

    static void main(String[] args) {
        SwingUtilities.invokeLater(GameApp::new);
    }

}
