package view;

import static org.junit.Assert.*;
import static org.testfx.api.FxAssert.verifyThat;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.util.DebugUtils;

import controller.GameController;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import model.Game;
import model.maze.MazeModelInterface;

public class MazeViewTest extends ApplicationTest {
	MazeModelInterface maze;
	GameController gameController;
	
    @Override
    public void start(Stage stage) {
		Game game = new Game();
		gameController = new GameController();
		gameController.initModel(game);
		Scene scene = new Scene(gameController.getView());
		maze = game.getMaze();
		stage.setScene(scene);
        stage.show();
    }
    
    @Test
    public void buildWallOnClick() {
    	clickOn("#maze");
    	assertEquals("There should be a wall now", 1, maze.getWalls().size());
    	verifyThat("#maze", NodeMatchers.hasChildren(1, ".wall"));
    	verifyThat("#maze .wall", NodeMatchers.hasChildren(1, ".image-view"));
    	//second click:
    	clickOn();

    	assertEquals("There should still be only one wall", 1, maze.getWalls().size());
    	verifyThat("#maze", NodeMatchers.hasChildren(1, ".wall"));
    	
    	//third click:
    	moveBy(20, 50);
    	clickOn();

    	assertEquals("There should be two walls now", 2, maze.getWalls().size());
    	verifyThat("#maze", NodeMatchers.hasChildren(2, ".wall"));
    	    	
    }
}
