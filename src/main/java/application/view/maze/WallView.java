package application.view.maze;

import application.ImageLoader;
import application.controller.MazeController;
import application.model.maze.Wall;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class WallView extends StackPane {
  private WallMenuView wallMenu;
  private Wall wall;
  private ImageView imgView;
  private TowerView towerView;

  public WallView(Wall wall, DoubleBinding scaleX, DoubleBinding scaleY) {
    this.wall = wall;
    this.getStyleClass().add("wall");

    imgView = new ImageView(ImageLoader.wall);
    imgView.fitWidthProperty().bind(scaleX);
    imgView.fitHeightProperty().bind(scaleY);
    imgView.getStyleClass().add("wall-image");

    towerView = new TowerView(wall.getTower(), scaleX, scaleY);

    wall.towerProperty()
        .addListener(
            (obj, oldTower, newTower) -> {
              setTowerView(new TowerView(newTower, scaleX, scaleY));
            });
    this.getChildren().addAll(imgView, towerView);
    this.layoutXProperty().bind(wall.xProperty().multiply(scaleX));
    this.layoutYProperty().bind(wall.yProperty().multiply(scaleY));
  }

  private void setTowerView(TowerView towerView) {
    this.getChildren().remove(this.towerView);
    this.towerView = towerView;
    this.getChildren().add(this.towerView);
  }

  public void setMenu(WallMenuView wallMenu, MazeController controller) {
    this.wallMenu = wallMenu;
    addEventHandler(MouseEvent.MOUSE_CLICKED, event -> wallMenu.show(event, wall, controller));
  }

  public WallMenuView getWallMenu() {
    return wallMenu;
  }
}
