package application.view.maze;

import application.controller.WallController;
import application.model.maze.Wall;
import application.model.maze.tower.AbstractTower;
import application.model.maze.tower.TowerType;
import application.model.maze.tower.TowerUpgrade;
import application.util.ImageLoader;
import application.util.Util;
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
  private MenuItem upgradeItem;
  private Label upgradeItemLabel;

  private DoubleBinding scaleX, scaleY;

  public WallMenuView(WallView view, DoubleBinding scaleX, DoubleBinding scaleY) {
    super(view, null);
    super.setAnimationDuration(new Duration(200));
    this.scaleX = scaleX;
    this.scaleY = scaleY;
    sellItemLabel = new FloatingLabel("").getView();
    sellItem = createMenuItem("Sell", ImageLoader.sell, sellItemLabel);
    normalTowerLabel =
        new FloatingLabel(Util.moneyString(-1 * AbstractTower.create(TowerType.NORMAL).getCosts()))
            .getView();
    normalTowerItem = createMenuItem("Normal Tower", ImageLoader.normalTower, normalTowerLabel);
    upgradeItemLabel = new FloatingLabel("").getView();
    upgradeItem = createMenuItem("Upgrade Tower", ImageLoader.upgrade, upgradeItemLabel);
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

  public void show(MouseEvent event, Wall wall, WallController wallController) {
    getItems().clear();
    sellItem.setOnAction(e -> wallController.sell());
    sellItemLabel.setText(Util.moneyString(wall.getCosts()));
    getItems().add(sellItem);
    if (wall.getTower().getType() == TowerType.NO) {
      normalTowerItem.setOnAction(e -> wallController.buildTower(TowerType.NORMAL));
      getItems().add(normalTowerItem);
    } else {
      TowerUpgrade upgrade = wall.getTower().getNextUpgrade();
      if (upgrade != null) {
        upgradeItem.setOnAction(e -> wallController.upgradeTower());
        upgradeItemLabel.setText(Util.moneyString(-1 * upgrade.getCosts()));
        getItems().add(upgradeItem);
      }
    }
    super.show(event);
  }
}
