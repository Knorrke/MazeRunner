package view;

import org.testfx.framework.junit.ApplicationTest;

import controller.GameController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Game;
import model.GameModelInterface;
import model.level.LevelModelInterface;
import model.maze.MazeModelInterface;
import model.player.PlayerModelInterface;

public class AbstractViewTest extends ApplicationTest {
	GameModelInterface game;
	MazeModelInterface maze;
	LevelModelInterface level;
	PlayerModelInterface player;
	
	GameController gameController;

	@Override
    public void start(Stage stage) {
		game = new Game();
		gameController = new GameController();
		gameController.initModel(game);
		Scene scene = new Scene(gameController.getView());
		maze = game.getMaze();
		level = game.getLevel();
		player = game.getPlayer();
		stage.setScene(scene);
        stage.show();
    }
    
}
