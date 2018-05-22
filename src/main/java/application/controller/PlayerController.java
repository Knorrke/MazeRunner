package application.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.ImageLoader;
import application.model.player.PlayerModelInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class PlayerController extends HBox implements ModelHolderInterface<PlayerModelInterface> {

  private static Logger LOG = Logger.getLogger(PlayerController.class.getName());

  @FXML Label money;
  @FXML Label lifes;
  @FXML ImageView moneyImage;
  @FXML ImageView lifesImage;

  private PlayerModelInterface player;
  private Parent view;

  public PlayerController() {
    FXMLLoader playerLoader =
        new FXMLLoader(getClass().getClassLoader().getResource("fxml/PlayerView.fxml"));
    playerLoader.setRoot(this);
    playerLoader.setController(this);
    try {
      LOG.fine("Loading PlayerView from fxml");
      playerLoader.load();
      LOG.fine("Loading PlayerView successfull");
    } catch (IOException exception) {
      LOG.log(Level.SEVERE, "Loading PlayerView.fxml failed", exception);
      throw new RuntimeException(exception);
    }
  }

  @FXML private void initialize() {
    LOG.fine("initializing PlayerView");
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
