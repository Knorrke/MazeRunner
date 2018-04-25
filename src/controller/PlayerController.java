package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import model.player.PlayerModelInterface;

public class PlayerController extends HBox {
	@FXML Label money;
	@FXML Label lifes;
	
	private PlayerModelInterface player;
	private Parent view;
	
	public PlayerController() {
		System.out.println("New PlayerController");
		FXMLLoader playerLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/PlayerView.fxml"));
		playerLoader.setRoot(this);
		playerLoader.setController(this);
        try {
        	System.out.println("Loading View");
            playerLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
	}
	
	public void initModel(PlayerModelInterface player) {
		this.player = player;
		money.textProperty().bind(this.player.moneyProperty().asString());
		lifes.textProperty().bind(this.player.lifesProperty().asString());
	}
	
	public Parent getView() {
		return view;
	}

}
