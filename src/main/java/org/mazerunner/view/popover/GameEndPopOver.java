package org.mazerunner.view.popover;

import org.mazerunner.controller.gameloop.GameLoop;
import org.mazerunner.model.GameState;
import org.mazerunner.util.FXMLLoaderUtil;
import org.mazerunner.util.ImageLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GameEndPopOver extends Pane {
  private GameState state;

  @FXML private Label gameEndText;
  @FXML private ImageView gameEndImage;

  public GameEndPopOver(GameState state, Pane parent) {
    this.state = state;
    Parent endmodal = FXMLLoaderUtil.load("EndModal.fxml", this);
    parent.getChildren().add(endmodal);
    GameLoop.executionSpeedUp = 0.3;
  }

  @FXML
  private void initialize() {
    gameEndText.setText(state.equals(GameState.WON) ? "You Win!" : "GAME OVER");
    gameEndImage.setImage(ImageLoader.endModalBackground);
  }
}
