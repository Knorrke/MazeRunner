package application;
	
import java.util.logging.Logger;

import controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Game;


public class Launcher extends Application {
	private static Logger LOG = Logger.getLogger(Launcher.class.getName());
	
	GameController gameController;
	
	@Override
	public void start(Stage primaryStage) {
		ImageLoader.loadAll();
		LOG.fine("creating model");
		Game game = new Game();
		LOG.fine("creating gameController");
		gameController = new GameController();
		LOG.fine("initializing Model");
		gameController.initModel(game);
		Scene scene = new Scene(gameController.getView());
		LOG.fine("loading stylesheet");
		scene.getStylesheets().add(getClass().getClassLoader().getResource("view/application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
