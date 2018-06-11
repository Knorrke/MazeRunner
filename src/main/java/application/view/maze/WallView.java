package application.view.maze;

import application.ImageLoader;
import application.controller.MazeController;
import application.model.maze.Wall;
import application.model.maze.tower.TowerType;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class WallView extends StackPane {
  private WallMenuView wallMenu;
  private Wall wall;

  public WallView(Wall wall, DoubleBinding scaleX, DoubleBinding scaleY) {
    this.wall = wall;
    this.getStyleClass().add("wall");

    ImageView wallImg = createImageView(ImageLoader.wall, scaleX, scaleY, "wall-image");

    TowerType towerType = wall.getTower().getType();
    ImageView towerView =
        createImageView(
            getTowerImage(towerType), scaleX, scaleY, "tower-image", towerType.toString());
    wall.towerProperty()
        .addListener(
            (obj, oldTower, newTower) -> {
              towerView.setImage(getTowerImage(newTower.getType()));
              towerView.getStyleClass().removeAll(oldTower.getType().toString());
              towerView.getStyleClass().add(newTower.getType().toString());
            });

    this.getChildren().addAll(wallImg, towerView);
    this.layoutXProperty().bind(wall.xProperty().multiply(scaleX));
    this.layoutYProperty().bind(wall.yProperty().multiply(scaleY));
  }

  private ImageView createImageView(
      Image img, DoubleBinding scaleX, DoubleBinding scaleY, String... clazzes) {
    ImageView imgView = new ImageView(img);
    imgView.fitWidthProperty().bind(scaleX);
    imgView.fitHeightProperty().bind(scaleY);
    imgView.getStyleClass().addAll(clazzes);
    return imgView;
  }

  private Image getTowerImage(TowerType towerType) {
    switch (towerType) {
      case NORMAL:
        return ImageLoader.normalTower;
      case NO:
      default:
        return ImageLoader.noTower;
    }
  }

  public void setMenu(WallMenuView wallMenu, MazeController controller) {
    this.wallMenu = wallMenu;
    addEventHandler(MouseEvent.MOUSE_CLICKED, event -> wallMenu.show(event, wall, controller));
  }

  public WallMenuView getWallMenu() {
    return wallMenu;
  }
}
