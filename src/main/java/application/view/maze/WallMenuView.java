package application.view.maze;

import org.omg.Messaging.SyncScopeHelper;

import application.ImageLoader;
import application.controller.GameController;
import application.controller.MazeController;
import application.model.maze.Wall;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import jfxtras.scene.menu.CirclePopupMenu;

public class WallMenuView extends CirclePopupMenu {
  private MenuItem sellItem;
  private MenuItem normalTowerItem;

  public WallMenuView(Node parent) {
    super(parent,null);
    ImageView sell =  new ImageView(ImageLoader.sell);
    sell.setId("sell-item");
    sellItem = new MenuItem("Sell",sell);
    ImageView normalTower = new ImageView(ImageLoader.normalTower);
    normalTower.setId("normal-tower");
    normalTowerItem = new MenuItem("Normal Tower", normalTower);
    getItems().addAll(sellItem,normalTowerItem);
  }

  public void show(MouseEvent event, Wall wall, MazeController controller) {
    sellItem.setOnAction(e -> controller.sell(wall));
    normalTowerItem.setOnAction(e -> controller.buildTower(wall));
    super.show(event);
  }
}
