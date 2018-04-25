package application;
	
import controller.GameController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Game;


public class Launcher extends Application {
	GameController gameController;
	
	@Override
	public void start(Stage primaryStage) {
		new ImageLoader();
		Game game = new Game();
		gameController = new GameController();
		System.out.println("created GameController");
		gameController.initModel(game);
		Scene scene = new Scene(gameController.getView());
		scene.getStylesheets().add(getClass().getClassLoader().getResource("view/application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
