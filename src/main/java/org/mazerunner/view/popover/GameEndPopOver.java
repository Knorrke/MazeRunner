package org.mazerunner.view.popover;

import org.mazerunner.controller.gameloop.GameLoop;
import org.mazerunner.model.GameState;
import org.mazerunner.util.FXMLLoaderUtil;
import org.mazerunner.util.ImageLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class GameEndPopOver extends Stage {
  private GameState state;

  @FXML private Label gameEndText;
  @FXML private ImageView gameEndImage;
  @FXML private StackPane pane;

  public GameEndPopOver(GameState state, Window window) {
    super(StageStyle.TRANSPARENT);
    initOwner(window);

    this.state = state;
    Parent root = FXMLLoaderUtil.load("EndModal.fxml", this);
    Scene dialogScene = new Scene(root);
    dialogScene.setFill(Color.TRANSPARENT);
    pane.setBackground(Background.EMPTY);
    setScene(dialogScene);
    widthProperty()
        .addListener(
            (obj, oldVal, newVal) -> {
              centerOnOwner();
            });
    show();
    GameLoop.executionSpeedUp = 0.3;
  }

  @FXML
  private void initialize() {
    gameEndText.setText(state.equals(GameState.WON) ? "You Win!" : "GAME OVER");
    gameEndImage.setImage(ImageLoader.endModalBackground);
  }

  public void centerOnOwner() {
    Window owner = getOwner();
    double windowCenterX = owner.getX() + owner.getWidth() / 2;
    double windowCenterY = owner.getY() + owner.getHeight() / 2;

    if (!Double.isNaN(windowCenterX)) {
      Platform.runLater(
          () -> {
            setX(windowCenterX - getWidth() / 2);
            setY(windowCenterY - getHeight() / 2);
          });
    } else {
      centerOnScreen();
    }
  }
}
