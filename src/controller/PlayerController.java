package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.ImageLoader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.player.PlayerModelInterface;

public class PlayerController extends HBox implements ModelHolder<PlayerModelInterface>{

	private static Logger LOG = Logger.getLogger(PlayerController.class.getName());

	@FXML Label money;
	@FXML Label lifes;
	@FXML ImageView moneyImage;
	@FXML ImageView lifesImage;
	
	private PlayerModelInterface player;
	private Parent view;
	
	public PlayerController() {
		FXMLLoader playerLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/PlayerView.fxml"));
		playerLoader.setRoot(this);
		playerLoader.setController(this);
        try {
        	LOG.fine("Loading PlayerView from fxml");
            playerLoader.load();
        } catch (IOException exception) {
        	LOG.log(Level.SEVERE, "Loading PlayerView.fxml failed", exception);
            throw new RuntimeException(exception);
        }
	}
	
	public void initialize() {
		moneyImage.setImage(ImageLoader.money);
		lifesImage.setImage(ImageLoader.lifes);
	}
	
	@Override
	public void initModel(PlayerModelInterface player) {
		this.player = player;
		money.textProperty().bind(this.player.moneyProperty().asString());
		lifes.textProperty().bind(this.player.lifesProperty().asString());
	}
	
	public Parent getView() {
		return view;
	}

}
