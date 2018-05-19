package application.view.maze;

import application.ImageLoader;
import application.controller.GameController;
import application.controller.MazeController;
import application.model.maze.Wall;
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

    Image im = ImageLoader.wall;
    ImageView img = new ImageView(im);
    img.fitWidthProperty().bind(scaleX);
    img.fitHeightProperty().bind(scaleY);

    this.getChildren().add(img);
    this.layoutXProperty().bind(wall.xProperty().multiply(scaleX));
    this.layoutYProperty().bind(wall.yProperty().multiply(scaleY));
  }

  public void setMenu(WallMenuView wallMenu, MazeController controller) {
    this.wallMenu = wallMenu;
    addEventHandler(MouseEvent.MOUSE_CLICKED, event -> wallMenu.show(event, wall, controller));
  }
  
  public WallMenuView getWallMenu() {
    return wallMenu;
  }
}
