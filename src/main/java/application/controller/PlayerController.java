package application.controller;

import java.util.logging.Logger;
import application.model.player.PlayerModelInterface;
import application.util.ImageLoader;
import application.util.Util;
import javafx.fxml.FXML;
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

  @FXML
  private void initialize() {
    LOG.fine("initializing PlayerView");
    moneyImage.setImage(ImageLoader.money);
    lifesImage.setImage(ImageLoader.lifes);
  }

  @Override
  public void initModel(PlayerModelInterface player) {
    this.player = player;
    money.textProperty().bind(Util.moneyString(this.player.moneyProperty()));
    lifes.textProperty().bind(this.player.lifesProperty().asString());
  }
}
