package application.view.maze;

import application.ImageLoader;
import application.controller.MazeController;
import application.model.maze.Wall;
import application.model.maze.tower.TowerType;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import jfxtras.scene.menu.CirclePopupMenu;

public class WallMenuView extends CirclePopupMenu {
  private MenuItem sellItem;
  private MenuItem normalTowerItem;

  public WallMenuView(Node parent) {
    super(parent, null);
    sellItem = createMenuItem("Sell", ImageLoader.sell);
    normalTowerItem = createMenuItem("Normal Tower", ImageLoader.normalTower);
    getItems().addAll(sellItem, normalTowerItem);
  }

  private MenuItem createMenuItem(String string, Image image) {
    ImageView view = new ImageView(image);
    view.setId(string.toLowerCase().replace(' ', '-'));
    view.setPreserveRatio(true);
    view.setFitWidth(50);
    view.setFitHeight(50);
    return new MenuItem(string, view);
  }

  public void show(MouseEvent event, Wall wall, MazeController controller) {
    sellItem.setOnAction(e -> controller.sell(wall));
    normalTowerItem.setOnAction(e -> controller.buildTower(wall, TowerType.NORMAL));
    super.show(event);
  }
}
