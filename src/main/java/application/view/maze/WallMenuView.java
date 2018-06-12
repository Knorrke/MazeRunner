package application.view.maze;

import application.ImageLoader;
import application.controller.MazeController;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.TowerType;
import application.view.FloatingLabel;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import jfxtras.scene.menu.CirclePopupMenu;

public class WallMenuView extends CirclePopupMenu {
  private MenuItem sellItem;
  private Label sellItemLabel;
  private MenuItem normalTowerItem;
  private Label normalTowerLabel;

  private DoubleBinding scaleX, scaleY;

  public WallMenuView(WallsView parent) {
    super(parent, null);
    super.setAnimationDuration(new Duration(200));
    scaleX = parent.xScalingProperty();
    scaleY = parent.yScalingProperty();
    sellItemLabel = new FloatingLabel("").getView();
    sellItem = createMenuItem("Sell", ImageLoader.sell, sellItemLabel);
    normalTowerLabel =
        new FloatingLabel("-$" + AbstractTower.create(TowerType.NORMAL).getCosts()).getView();
    normalTowerItem = createMenuItem("Normal Tower", ImageLoader.normalTower, normalTowerLabel);
  }

  private MenuItem createMenuItem(String hoverText, Image image, Label label) {
    ImageView view = new ImageView(image);
    String id = hoverText.toLowerCase().replace(' ', '-');
    view.setId(id);
    view.setPreserveRatio(true);
    view.fitWidthProperty().bind(scaleX.multiply(1.5));
    view.fitHeightProperty().bind(scaleY.multiply(1.5));
    label.setId(id + "-label");
    return new MenuItem(hoverText, new StackPane(view, label));
  }

  public void show(MouseEvent event, Wall wall, MazeController controller) {
    getItems().clear();
    sellItem.setOnAction(e -> controller.sell(wall));
    sellItemLabel.setText("+$" + wall.getCosts());
    getItems().add(sellItem);
    if (wall.getTower().getType() == TowerType.NO) {
      normalTowerItem.setOnAction(e -> controller.buildTower(wall, TowerType.NORMAL));
      getItems().add(normalTowerItem);
    }
    super.show(event);
  }
}
