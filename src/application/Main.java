package application;
	
import controller.GameViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Game;
import model.GameModelInterface;
import model.player.PlayerModelInterface;
import view.MainView;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		new ImageLoader();
		try {
			FXMLLoader gameViewLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/GameView.fxml"));
			Scene scene = new MainView(gameViewLoader.load());
			scene.getStylesheets().add(getClass().getClassLoader().getResource("view/application.css").toExternalForm());
			GameViewController gameViewController = gameViewLoader.getController();
			
			GameModelInterface game = new Game();
			gameViewController.initGameModel(game);
			
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
