package view;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.junit.BeforeClass;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.DebugUtils;

import application.ImageLoader;
import controller.GameController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Game;
import model.GameModelInterface;
import model.level.LevelModelInterface;
import model.maze.MazeModelInterface;
import model.player.PlayerModelInterface;

public abstract class AbstractViewTest extends ApplicationTest {
	protected GameModelInterface game;
	protected MazeModelInterface maze;
	protected LevelModelInterface level;
	protected PlayerModelInterface player;

	@BeforeClass
	public static void setupSpec() throws Exception {
	    if (Boolean.getBoolean("headless")) {
	        System.setProperty("testfx.robot", "glass");
	        System.setProperty("testfx.headless", "true");
	        System.setProperty("prism.order", "sw");
	        System.setProperty("prism.text", "t2k");
	        System.setProperty("java.awt.headless", "true");
	    }
	}
	
	@Override
    public void start(Stage stage) {
		ImageLoader.loadAll();
		game = new Game();
		GameController gameController = new GameController();
		gameController.initModel(game);
		Scene scene = new Scene(gameController.getView());
		scene.getStylesheets().add(getClass().getClassLoader().getResource("view/application.css").toExternalForm());
		maze = game.getMaze();
		level = game.getLevel();
		player = game.getPlayer();
		stage.setScene(scene);
        stage.show();
    }
    
	/** Debug helper */
	
	private final AtomicInteger screenshotNumber = new AtomicInteger(0);
	
	public Function<StringBuilder, StringBuilder> collectInfos() {
		String dirName = "failed-test-screenshots";
		File dir = new File(dirName);
		if (!dir.isDirectory()){
			dir.mkdir();
		}
		String testName = this.getClass().getCanonicalName();
		return DebugUtils.compose(
				DebugUtils.insertHeader("Context:"),
				DebugUtils.showKeysPressedAtTestFailure(this),
				DebugUtils.showMouseButtonsPressedAtTestFailure(this),
				DebugUtils.showFiredEvents(),
				DebugUtils.saveScreenshot(dirName+"/"+testName, screenshotNumber.getAndIncrement())
		);
	}
}
